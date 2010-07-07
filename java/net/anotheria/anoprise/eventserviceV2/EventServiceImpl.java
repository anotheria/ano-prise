package net.anotheria.anoprise.eventserviceV2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumerProxy;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushSupplierProxy;
import net.anotheria.anoprise.eventserviceV2.local.LocalSupportFactory;
import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistry;
import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistryException;
import net.anotheria.anoprise.eventserviceV2.remote.RemoteProxy;
import net.anotheria.anoprise.eventserviceV2.remote.RemotePushConsumerProxy;
import net.anotheria.anoprise.eventserviceV2.remote.RemotePushSupplierProxy;
import net.anotheria.anoprise.eventserviceV2.remote.RemotePushSupporter;
import net.anotheria.anoprise.eventserviceV2.remote.RemoteSupportFactory;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anoprise.metafactory.ServiceFactory;

import org.apache.log4j.Logger;

/**
 * The implementation of the event service.
 * 
 * For unit tests: EventServiceRegistry is used in this impl. To overwrite it with the local one, you
 * need to overwrite MetaFactory.addFactoryClass(EventServiceRegistry.class, Extension.REMOTE,...)
 * between EventService instance creation and any first remote channel obtainment from this instance.  
 * 
 * @author lrosenberg, vkazhdan
 */
public class EventServiceImpl implements EventService {

	private static Logger log = Logger.getLogger(EventServiceImpl.class);

	/*
	* Maps synchronized by modifying only from synchronized methods.
	*/
	private Map<String, LocalPushConsumerProxy> localPushConsumerProxies;
	private Map<String, LocalPushSupplierProxy> localPushSupplierProxies;
	private Map<String, RemotePushConsumerProxy> remotePushConsumerProxies;
	private Map<String, RemotePushSupplierProxy> remotePushSupplierProxies;

	/*
	 * Listeners synchronized with help of CopyOnWriteArrayList impl.
	 */
	private List<EventServiceListener> listeners;
		
	/**
	 * Single instance of EventServiceRegistry. 
	 * Inited after the first remote channel obtainment or channel unavailability notification.
	 */
	private EventServiceRegistry eventServiceRegistry = null;

	/**
	 * Protected constructor for EventServiceFactory and unit tests. 
	 */
	@SuppressWarnings("unchecked") // For adding registry factory class
	protected EventServiceImpl() {
		localPushConsumerProxies = new HashMap<String, LocalPushConsumerProxy>();
		localPushSupplierProxies = new HashMap<String, LocalPushSupplierProxy>();
		remotePushConsumerProxies = new HashMap<String, RemotePushConsumerProxy>();
		remotePushSupplierProxies = new HashMap<String, RemotePushSupplierProxy>();

		listeners = new CopyOnWriteArrayList<EventServiceListener>();
		
		// Init registry factory
		try {
			MetaFactory.addFactoryClass(EventServiceRegistry.class, Extension.REMOTE, (Class<ServiceFactory<EventServiceRegistry>>)Class.forName("net.anotheria.anoprise.eventserviceV2.registry.generated.RemoteEventServiceRegistryFactory"));
		} catch(ClassNotFoundException e) {
			log.fatal("Can't init EventServiceRegistry factory. Cause: " + e.getMessage(), e);			
		}		
	}
	
	/**
	 * Init eventServiceRegistry if it is not inited yet.
	 */
	private void initRegistry() {
		if (eventServiceRegistry == null) {
			try {
				eventServiceRegistry = MetaFactory.get(EventServiceRegistry.class, Extension.REMOTE);
			} catch (MetaFactoryException e) {
				log.fatal("Can't init EventServiceRegistry, maybe it is not up and ready. Cause: " + e.getMessage(), e);
			}
		}
	}
			
	@Override
	public EventChannelForLocalPushSupplier obtainEventChannelForLocalPushSupplier(String channelName) {
		// Local push supplier need only access RemotePushSupporter part of LocalPushSupplierProxy
		LocalPushSupplierProxy proxy = localPushSupplierProxies.get(channelName);
		if (proxy == null) {
			// Now call synchronized method.
			proxy = obtainLocalPushSupplierProxy(channelName);	
		}
		
		return /*(EventChannelForLocalPushSupplier)*/ proxy;
	}

	@Override
	public EventChannelForLocalPushConsumer obtainEventChannelForLocalPushConsumer(String channelName) {
		// Local push consumer need only access LocalParticipantsCollector<LocalPushConsumer> part of LocalPushConsumerProxy
		LocalPushConsumerProxy proxy = localPushConsumerProxies.get(channelName);
		if (proxy == null) {
			// Now call synchronized method.
			proxy = obtainLocalPushConsumerProxy(channelName);	
		}
		
		return /*(EventChannelForLocalPushConsumer)*/ proxy;
	}

	@Override
	public EventChannelForRemotePushSupplier obtainEventChannelForRemotePushSupplier(String channelName) 
		throws EventServiceException {
		// Remote push supplier need only access RemotePushSupporter part of RemotePushConsumerProxy
		RemotePushConsumerProxy proxy = remotePushConsumerProxies.get(channelName);
		if (proxy == null) {
			// Now call synchronized method.
			proxy = obtainRemotePushConsumerProxy(channelName);	
		}
		
		return /*(EventChannelForRemotePushSupplier)*/ proxy;
	}

	@Override
	public EventChannelForRemotePushConsumer obtainEventChannelForRemotePushConsumer(String channelName) 
		throws EventServiceException {
		// Remote push consumer need only access RemoteParticipantsCollector<LocalPushConsumer> part of RemotePushSupplierProxy
		RemotePushSupplierProxy proxy = remotePushSupplierProxies.get(channelName);
		if (proxy == null) {
			// Now call synchronized method.
			proxy = obtainRemotePushSupplierProxy(channelName);	
		}
		
		return /*(EventChannelForRemotePushConsumer)*/ proxy;		
	}

	/*
	 * Synchronized method.
	 */	
	@Override
	public synchronized void notifyEventChannelUnavailable(EventChannelForRemotePushConsumer eventChannel, String channelName) {
		log.info("Notifying event channel for remote push consumer unavailable: " + eventChannel);
		
		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		if (eventChannel instanceof RemotePushSupplierProxy) {
			// Notify registry
			try {
				eventServiceRegistry.notifyRemoteSupplierProxyUnavailable((RemotePushSupplierProxy)eventChannel);
			} catch (EventServiceRegistryException e) {
				log.warn("Can't notify registry about RemoteSupplierProxy unavailable. Cause: " + e.getMessage());
			}
			
			// Remote supplier proxy from existed consumer proxys
			List<RemoteProxy> remoteConsumerProxies;		
			try {
				remoteConsumerProxies = eventServiceRegistry.getRemotePushConsumerProxys(channelName);
			} catch (EventServiceRegistryException e) {
				log.warn("Can't remove unavailable supplier proxy from existed consumer proxys, because can't get consumer proxys for channel: " + channelName
						+ ". Cause: " + e.getMessage());
				return;
			}
			for(RemoteProxy remoteProxy : remoteConsumerProxies) {
				try {
					((RemotePushConsumerProxy) remoteProxy).remoteRemove((RemotePushSupplierProxy)eventChannel);
				} catch (RemoteException e) {
					log.warn("Can't remove unavailable supplier proxy from existed consumer proxy for channel: " + channelName
							+ ". Cause: " + e.getMessage());
				}
			}
			
			// Remove proxy locally if exists
			remotePushSupplierProxies.remove((RemotePushSupplierProxy)eventChannel);
		}
		
	}
	
	/*
	 * Synchronized method.
	 */
	@Override
	public synchronized void notifyEventChannelUnavailable(EventChannelForRemotePushSupplier eventChannel, String channelName) {
		log.info("Notifying event channel for remote push supplier unavailable: " + eventChannel);
		
		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		if (eventChannel instanceof RemotePushConsumerProxy) {
			// Notify registry
			try {
				eventServiceRegistry.notifyRemoteConsumerProxyUnavailable((RemotePushConsumerProxy)eventChannel);
			} catch (EventServiceRegistryException e) {
				log.warn("Can't notify registry about RemoteConsumerProxy unavailable. Cause: " + e.getMessage());
			}
			
			// Remove proxy locally if exists
			remotePushConsumerProxies.remove((RemotePushConsumerProxy)eventChannel);
		}
	}

	/**
	 * Obtain LocalPushConsumerProxy. New will be created and connected if not exists one.
	 * Synchronized method.
	 * 
	 * @param channelName
	 * @return
	 */
	private synchronized LocalPushConsumerProxy obtainLocalPushConsumerProxy(String channelName) {		
		// Try to get existed proxy
		LocalPushConsumerProxy proxy = localPushConsumerProxies.get(channelName);
		if (proxy != null) {
			return proxy;
		}
		
		// Create new one if absent
		proxy = LocalSupportFactory.createLocalPushConsumerProxy(channelName);
		
		// Add new push consumer proxy to supplier proxy of the same channel
		LocalPushSupplierProxy supplierProxy = localPushSupplierProxies.get(channelName);
		if (supplierProxy != null) {
			supplierProxy.add(proxy);
		}
				
		// Add new proxy to local storage
		localPushConsumerProxies.put(channelName, proxy);

		// Notify new channel creation
		notifyChannelCreation(channelName, ProxyType.LOCAL_PUSH_CONSUMER_PROXY);
		
		// Print logs
		log.info("New local push consumer proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Obtain LocalPushSupplierProxy. New will be created and connected if not exists one.
	 * Synchronized method.
	 * 
	 * @param channelName
	 * @return
	 */
	private synchronized LocalPushSupplierProxy obtainLocalPushSupplierProxy(String channelName) {		
		// Try to get existed proxy
		LocalPushSupplierProxy proxy = localPushSupplierProxies.get(channelName);
		if (proxy != null) {
			return proxy;
		}
		
		// Create new one if absent
		proxy = LocalSupportFactory.createLocalPushSupplierProxy(channelName);
		
		// Add all existed push push consumer proxy of the same channel to new supplier proxy
		LocalPushConsumerProxy consumerProxy = localPushConsumerProxies.get(channelName);
		if (consumerProxy != null) {
			proxy.add(consumerProxy);
		}
				
		// Add new proxy to local storage
		localPushSupplierProxies.put(channelName, proxy);

		// Notify new channel creation
		notifyChannelCreation(channelName, ProxyType.LOCAL_PUSH_SUPPLIER_PROXY);
		
		// Print logs
		log.info("New local push supplier proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Obtain RemotePushConsumerProxy. New will be created and connected if not exists one.
	 * Synchronized method.
	 * 
	 * @param channelName
	 * @return
	 */
	private synchronized RemotePushConsumerProxy obtainRemotePushConsumerProxy(String channelName) 
		throws EventServiceException {
		// Try to get existed proxy
		RemotePushConsumerProxy proxy = remotePushConsumerProxies.get(channelName);
		if (proxy != null) {
			return proxy;	
		}

		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		// Create new one if absent
		proxy = RemoteSupportFactory.createRemotePushConsumerProxy(channelName, this);
		
		// Add remote suppliers proxys from registry
		List<RemoteProxy> remoteSupplierProxys;		
		try {
			remoteSupplierProxys = eventServiceRegistry.getRemotePushSupplierProxys(channelName);
		} catch (EventServiceRegistryException e) {
			throw new EventServiceException("Can't get supplier proxys from registry for channel: " + channelName 
					+ ". Cause: " + e.getMessage(), e);
		}
		for(RemoteProxy remoteProxy : remoteSupplierProxys) {
			try {
				proxy.remoteAdd((RemotePushSupplierProxy) remoteProxy);
			} catch (RemoteException e) {
				log.warn("Skip adding remote supplier proxy for the new remote consumer for channel: " + channelName
						 + ". Cause: " + e.getMessage(), e);
			}
		}
		
		// Export remote proxy		
		RemotePushConsumerProxy exportedProxy;
		try {
			exportedProxy = (RemotePushConsumerProxy) UnicastRemoteObject.exportObject(proxy, 0);
		} catch (RemoteException e) {
			throw new EventServiceException("Can't export consumer proxy for channel: " + channelName
					 + ". Cause: " + e.getMessage(), e);
		}
		
		// Register new proxy in registry
		try {
			eventServiceRegistry.registerRemoteConsumerProxy(channelName, exportedProxy);
		} catch (EventServiceRegistryException e) {
			throw new EventServiceException("Can't register new consumer proxy in registry for channel: " + channelName
					+ ". Cause: " + e.getMessage(), e);
		}
		
		// Add new remote proxy to local storage
		remotePushConsumerProxies.put(channelName, proxy);

		// Notify new channel creation
		notifyChannelCreation(channelName, ProxyType.REMOTE_PUSH_CONSUMER_PROXY);
		
		// Print logs
		log.info("New lremote push consumer proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Obtain RemotePushSupplierProxy. New will be created and connected if not exists one.
	 * Synchronized method.
	 * 
	 * @param channelName
	 * @throws EventServiceException
	 */
	private synchronized RemotePushSupplierProxy obtainRemotePushSupplierProxy(String channelName) 
		throws EventServiceException {
		// Try to get existed proxy
		RemotePushSupplierProxy proxy = remotePushSupplierProxies.get(channelName);
		if (proxy != null) {
			return proxy;	
		}
		
		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		// Create new one if absent
		proxy = RemoteSupportFactory.createRemotePushSupplierProxy(channelName);		
		
		// Export remote proxy			
		RemotePushSupplierProxy exportedProxy;
		try {
			exportedProxy = (RemotePushSupplierProxy) UnicastRemoteObject.exportObject((RemotePushSupporter) proxy, 0);
		} catch (RemoteException e) {
			throw new EventServiceException("Can't export supplier proxy for channel: " + channelName
					+ ". Cause: " + e.getMessage());
		}
		
		// Add new supplier proxy for existed consumer proxys
		List<RemoteProxy> remoteConsumerProxys;		
		try {
			remoteConsumerProxys = eventServiceRegistry.getRemotePushConsumerProxys(channelName);
		} catch (EventServiceRegistryException e) {
			throw new EventServiceException("Can't get consumer proxys for channel: " + channelName
					+ ". Cause: " + e.getMessage());
		}
		for(RemoteProxy remoteProxy : remoteConsumerProxys) {
			try {
				((RemotePushConsumerProxy) remoteProxy).remoteAdd(exportedProxy);
			} catch (RemoteException ex) {
				// Double check and then notify proxy unavailable
				log.warn("Error was during remotely adding new supplier proxy to existed consumer proxy. Try to repeat call in 2 sec. proxy: " + remoteProxy 
						+ ". Cause: " + ex.getMessage());				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ignored) { }
				
				try {					
					((RemotePushConsumerProxy) remoteProxy).remoteAdd(exportedProxy);
				} catch (RemoteException ex2) {
					log.warn("Error was during second remotely adding new supplier proxy to existed consumer proxy. Notify consumer proxy unavailable. proxy: " + remoteProxy 
							+ ". Cause: " + ex.getMessage());
					notifyEventChannelUnavailable((RemotePushConsumerProxy) remoteProxy, channelName);
				}				
				
			}
		}
		
		// Register new proxy in registry
		try {
			eventServiceRegistry.registerRemoteSupplierProxy(channelName, exportedProxy);
		} catch (EventServiceRegistryException e) {
			throw new EventServiceException("Can't register new supplier proxy in registry for channel: " + channelName
					+ ". Cause: " + e.getMessage());
		}
		
		// Add new remote proxy to local storage
		remotePushSupplierProxies.put(channelName, proxy);
		
		
		// Print logs
		log.info("New remote push supplier proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Print dump to log.
	 */
	private void logDump() {
		log.debug("============ EventService Dump: ===================");
		log.debug("Local push consumer proxies: " + localPushConsumerProxies);
		log.debug("Local push supplier proxies: " + localPushSupplierProxies);
		log.debug("Remote push consumer proxies: " + remotePushConsumerProxies);
		log.debug("Remote push supplier proxies: " + remotePushSupplierProxies);
	}

	public void addListener(EventServiceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(EventServiceListener listener) {
		listeners.remove(listener);
	}

	private void notifyChannelCreation(String channelName, ProxyType type) {
		for (EventServiceListener listener : listeners){
			try{
				listener.channelCreated(channelName, type);
			}catch(Exception e){
				log.error("Unexpected exception in listener " + listener + ", in call notifyChannelCreation(" + channelName+ ", "+ type+")", e);
			}
		}
	}
		
}
