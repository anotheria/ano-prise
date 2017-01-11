package net.anotheria.anoprise.eventservice.util;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.ProxyType;
import net.anotheria.util.queue.IQueue;
import net.anotheria.util.queue.QueueOverflowException;
import net.anotheria.util.queue.StandardQueueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility helps to push events away without waiting for their delivery. Usually if you push an event into the channel you will have to wait atleast until the event 
 * leaves the channel. By inserting a queue between the intention of event sending and the push itself you decouple both and make yourself independent from
 * processing peaks.
 * @author lrosenberg
 *
 */
public class QueuedEventSender extends Thread {
	/**
	 * Number of sent events. 
	 */
	private int counter;
	/**
	 * Name of the Sender.
	 */
	private String name;
	/**
	 * Logger to log to.
	 */
	private Logger log;
	/**
	 * Default logger which is used if no custom logger is provided.
	 */
	private static Logger defLogger;
	/**
	 * Underlying queue.
	 */
	private IQueue<Event> queue;
	/**
	 * Target channel.
	 */
	private EventChannel myChannel;
	/**
	 * Default sleep time before trying to resend an event.
	 */
	public static final long DEF_SLEEP_TIME = 50;
	/**
	 * Default queue size if not specified in constructor.
	 */
	public static final int DEF_QUEUE_SIZE = 1000;
	
	private long sleepTime;
	/**
	 * Number of queue overflows.
	 */
	private int overflowCount;
	/**
	 * Number of thrown events.
	 */
	private int throwAwayCount;
	
	private volatile boolean started = false;
	
	/**
	 * This is a special mode for unittesting, in this mode events are delivered directly without queue. Use only in testing.
	 */
	private volatile boolean synchedMode = false;
	
	static{
		defLogger = LoggerFactory.getLogger(QueuedEventSender.class);
	}
	
	public QueuedEventSender(String aName, EventChannel aChannel, int queueSize, long aSleepTime, Logger aLog ){
		super(aName);
		setDaemon(true);
		name = aName;
		myChannel = aChannel;
		queue = new StandardQueueFactory<Event>().createQueue(queueSize);
		log = aLog;
		if (log==null){
			defLogger.warn("Tried to assign null logger, switching to defLogger");
			log = defLogger;
		}
		sleepTime = aSleepTime;
	}

	public QueuedEventSender(String aName, String channelName, int queueSize, long aSleepTime, Logger aLog ){
		this(aName, EventServiceFactory.createEventService().obtainEventChannel(channelName, ProxyType.PUSH_SUPPLIER_PROXY), queueSize, aSleepTime, aLog);
	}
	
	public QueuedEventSender(String name, EventChannel channel, Logger log){
		this(name, channel, DEF_QUEUE_SIZE, DEF_SLEEP_TIME, log);
	}

	
	public QueuedEventSender(String name, String channelName, Logger log){
		this(name, channelName, DEF_QUEUE_SIZE, DEF_SLEEP_TIME, log);
	}
	
	public QueuedEventSender(String name, EventChannel channel){
		this(name, channel, defLogger);
	}

	public QueuedEventSender(String name, String channelName){
		this(name, channelName, defLogger);
	}
	
	public void push(Event event) throws QueueFullException{
		//this is the special testing mode. In this mode no queuing takes places. Also, the queue processor can remain unstarted in this mode.
		if (synchedMode){
			myChannel.push(event);
			return;
		}
		if (!started)
			throw new IllegalStateException("Can't push into not started event sender");
		try{
			queue.putElement(event);
		}catch(QueueOverflowException e1){
			overflowCount++;
			//ok, first exception, we try to recover
			try{
				Thread.sleep(sleepTime);
			}catch(Exception ignored){}
			try{
				queue.putElement(event);
			}catch(QueueOverflowException e2){
				throwAwayCount++;
				log.error("couldn't recover from queue overflow, throwing away "+event);
				throw new QueueFullException(event,"Stats:"+getStatsString());
			}
			
		}
	}
	
	@Override public void start(){
		started = true;
		super.start();
	}
	
	@Override
	public void run(){
		try{
		
			//ThreadController.getInstance().addThreadAs(this, name);
			counter = 0;
			
			while(true){
				//ThreadController.getInstance().notifyThreadActivity(this);
				if (queue.hasElements()){
					counter++;
					if ((counter/100*100)==counter){
						logOutInfo();
					}
					try{
						Event event = queue.nextElement();
						if (event==null){
							log.error("Event to send is NULL, skipped.");
						}else{
							myChannel.push(event);
						}
					}catch(Exception e){
						log.error("myChannel.push", e);
					}
				}else{
					try{
						sleep(50);
					}catch(InterruptedException ignored){}
				}
			}
		}catch(Throwable ttt){
			try{
				log.error("run ", ttt);
			}catch(Exception e){
				System.out.println(QueuedEventSender.class+" Can't log!!!");
				ttt.printStackTrace();
			}
		}
	}
	/**
	 * @return
	 */
	public int getOverflowCount() {
		return overflowCount;
	}

	/**
	 * @return
	 */
	public int getThrowAwayCount() {
		return throwAwayCount;
	}
	
	public String getStatsString(){
		return counter+" elements sent, queue: "+queue.toString()+", OC:"+overflowCount+", TAC:"+throwAwayCount;		
	}
	
	public void logOutInfo(){
		log.info(name+": "+counter+" elements done, stat: "+queue.toString()+", OC:"+overflowCount+", TAC:"+throwAwayCount);
	}
	
	public boolean hasUnsentElements(){
		return queue.hasElements();
	}

	public boolean isSynchedMode() {
		return synchedMode;
	}

	public void setSynchedMode(boolean synchedMode) {
		this.synchedMode = synchedMode;
	}
}
