package net.anotheria.anoprise.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author denis
 *
 */
public class MultiProcessor<T>{
	
	/**
	 * The log for this processor.
	 */
	private Logger log;
	
	/**
	 * Number of processors.
	 */
	private final int channelsNumber;
	
	private ExecutorService executorsPool;
	
	/**
	 * A worker which processes elements in the queue.
	 */
	private PackageWorker<T> worker;
	
	private List<WorkProcessingListener<T>> listeners;

	
//	public MultiProcessor(int aChannelsNumber, PackageWorker<T> aWorker){
//		this(aChannelsNumber, aWorker, null);
//	}
	
	/**
	 * @param aLog
	 *            logger for output. If null default will be used.
	 */
	public MultiProcessor(int aChannelsNumber, PackageWorker<T> aWorker, Logger aLog){
		log = aLog != null? aLog: LoggerFactory.getLogger(MultiProcessor.class);
		channelsNumber = aChannelsNumber;
		executorsPool = new ThreadPoolExecutor(aChannelsNumber, aChannelsNumber, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(aChannelsNumber * 2){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean offer(Runnable command) {
				try {
					put(command);
				} catch (InterruptedException e) {
					return false;
				}
				return true;
			}
		});
		worker = aWorker; 
		listeners = new CopyOnWriteArrayList<>();
	}
	
	
	public void process(final List<T> elementsPackage){

		executorsPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					fireWorkStarted(elementsPackage);
					long workingStart = System.nanoTime();
					worker.doWork(elementsPackage);
					fireWorkFinished(elementsPackage, System.nanoTime() - workingStart);
				} catch (Throwable t) {
					try {
						fireWorkInterrupted(elementsPackage);
						log.error("Failure while working under element: ", t);
					} catch (Exception e) {
						System.out.println(QueuedMultiProcessor.class + " Can't log!!!");
						t.printStackTrace();
					}
				}
			}
		});
	}

	public int getChannelsNumber() {
		return channelsNumber;
	}

	public void shutdown(){
		if(!executorsPool.isShutdown())
			executorsPool.shutdown();
	}
	
	public boolean isFinished(){
		return executorsPool.isTerminated();
	}
	
	public void addListener(WorkProcessingListener<T> l){
		listeners.add(l);
	}
	
	public boolean removeListener(WorkProcessingListener<T> l){
		return listeners.remove(l);
	}
	
	private void fireWorkStarted(List<T> elementsPackage){
		for(WorkProcessingListener<T> l: listeners){
			try{
				l.workStarted(elementsPackage);
			}catch(Exception e){
				log.warn("Could not fire workStarted to listener: ", e);
			}
		}
	}
	
	private void fireWorkFinished(List<T>  elementsPackage, long workDuration){
		for(WorkProcessingListener<T> l: listeners){
			try{
				l.workFinished(elementsPackage, workDuration);
			}catch(Exception e){
				log.warn("Could not fire workFinished to listener: ", e);
			}
		}
	}
	
	private void fireWorkInterrupted(List<T>  elementsPackage){
		for(WorkProcessingListener<T> l: listeners){
			try{
				l.workInterrupted(elementsPackage);
			}catch(Exception e){
				log.warn("Could not fire workInterrupted to listener: ", e);
			}
		}
	}

}
