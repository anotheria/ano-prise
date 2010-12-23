package net.anotheria.anoprise.processor;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class MultiProcessor<T>{
	
	/**
	 * The log for this processor.
	 */
	private static final Logger log = Logger.getLogger(MultiProcessor.class);
	
	/**
	 * Number of processors.
	 */
	private final int channelsNumber;
	
	private ExecutorService executorsPool;
	
	/**
	 * A worker which processes elements in the queue.
	 */
	private PackageWorker<T> worker;
	
	List<WorkProcessingListener<T>> listeners;
	
	public MultiProcessor(int aChannelsNumber, PackageWorker<T> aWorker){
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
		listeners = new CopyOnWriteArrayList<WorkProcessingListener<T>>();
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
				} catch (Exception e) {
					fireWorkInterrupted(elementsPackage);
//					stats.addError();
					log.error("Failure while working under element: ", e);
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
