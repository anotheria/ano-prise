package net.anotheria.anoprise.eventserviceV2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

	//private ConcurrentMap<String, EventChannelPushConsumerProxy>	pushConsumerProxies;
	//private ConcurrentMap<String, EventChannelPushSupplierProxy>	pushSupplierProxies;
	private ConcurrentMap<String, RemotePushConsumerProxy>	remotePushConsumerProxies;
	private ConcurrentMap<String, RemotePushSupplierProxy>	remotePushSupplierProxies;

	private List<EventServiceListener> listeners;
		
	private EventServiceRegistry eventServiceRegistry = null;

	/**
	 * Protected constructor for EventServiceFactory and unit tests. 
	 */
	@SuppressWarnings("unchecked")
	protected EventServiceImpl() {
		//pushConsumerProxies = new ConcurrentHashMap<String, EventChannelPushConsumerProxy>(10);
		//pushSupplierProxies = new ConcurrentHashMap<String, EventChannelPushSupplierProxy>(10);
		remotePushConsumerProxies = new ConcurrentHashMap<String, RemotePushConsumerProxy>(10);
		remotePushSupplierProxies = new ConcurrentHashMap<String, RemotePushSupplierProxy>(10);

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
	public synchronized EventChannelForRemotePushSupplier obtainEventChannelForRemotePushSupplier(String channelName) 
		throws EventServiceException {
		// Remote push supplier need only access RemotePushSupporter part of RemotePushConsumerProxy
		return (EventChannelForRemotePushSupplier) obtainRemotePushConsumerProxy(channelName);
	}

	@Override
	public synchronized EventChannelForRemotePushConsumer obtainEventChannelForRemotePushConsumer(
			String channelName) 
		throws EventServiceException {
		// Remote push consumer need only access LocalParticipantsCollector<LocalPushConsumer> part of RemotePushSupplierProxy
		return (EventChannelForRemotePushConsumer) obtainRemotePushSupplierProxy(channelName);
	}

		
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
	 * Obtain RemotePushConsumerProxy. New will be created and connected if not exists one.
	 * 
	 * @param channelName
	 * @return
	 */
	private RemotePushConsumerProxy obtainRemotePushConsumerProxy(String channelName) 
		throws EventServiceException {
		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		// Try to get existed proxy
		RemotePushConsumerProxy proxy = (RemotePushConsumerProxy) remotePushConsumerProxies.get(channelName);
		if (proxy != null)
			return proxy;			
		
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
		log.info("New push consumer proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Obtain RemotePushSupplierProxy. New will be created and connected if not exists one.
	 * 
	 * @param channelName
	 * @throws EventServiceException
	 */
	private RemotePushSupplierProxy obtainRemotePushSupplierProxy(String channelName) 
		throws EventServiceException {
		// Init eventServiceRegistry if it is not inited yet.
		initRegistry();
		
		// Try to get existed proxy
		RemotePushSupplierProxy proxy = (RemotePushSupplierProxy) remotePushSupplierProxies.get(channelName);
		if (proxy != null)
			return proxy;			
		
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
		log.info("New push supplier proxy for channel created and connected. channelName: " + channelName + " proxy: " + proxy);
		logDump();
		
		return proxy;
	}
	
	/**
	 * Print dump to log.
	 */
	private void logDump() {
		log.debug("============ EventService Dump: ===================");
		//log.debug("Push consumer proxies: " + pushConsumerProxies);
		//log.debug("Push supplier proxies: " + pushSupplierProxies);
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
		
	/*
	public RemoteEventChannel obtainEventChannel(String channelName, EventChannelParticipant participant) {

		ProxyType type = ProxyType.NONE;
		if (participant instanceof LocalPushConsumer)
			type = ProxyType.PUSH_CONSUMER_PROXY;
		if (participant instanceof EventServicePushSupplier)
			type = ProxyType.PUSH_SUPPLIER_PROXY;

		if (participant instanceof RemoteEventServiceConsumer)
			type = ProxyType.REMOTE_PUSH_CONSUMER_PROXY;

		if (participant instanceof RemoteEventServiceSupplier)
			type = ProxyType.REMOTE_PUSH_SUPPLIER_PROXY;

		if (type == ProxyType.NONE)
			throw new RuntimeException("Unsupported participant type: " + participant);

		return obtainEventChannel(channelName, type);
	}

	public synchronized RemoteEventChannel obtainEventChannel(String channelName, ProxyType proxyType) {
		RemoteEventChannel ret = null;
		log.debug("Creating event channel: " + channelName + " of type " + proxyType);
		int triesNotifyChannelCreationCount = 10;
		switch (proxyType) {
			case PUSH_CONSUMER_PROXY:
				ret = _obtainPushConsumerProxy(channelName);
				triesNotifyChannelCreationCount = 10;
				while (triesNotifyChannelCreationCount > 0) {
					try {
						notifyChannelCreation(channelName, proxyType);
						triesNotifyChannelCreationCount = 0;
					} catch (RuntimeException e) {
						log.warn("obtainEventChannel : failed to obtain channel: "+channelName +", tries: " + triesNotifyChannelCreationCount );
						if (e.getMessage().startsWith("Service failed: client timeout reached")) {
							triesNotifyChannelCreationCount--;
						} else {
							throw e;
						}
						if (triesNotifyChannelCreationCount == 0) {
							throw e;
						}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {

						}
					}
				}
				break;
			case PUSH_SUPPLIER_PROXY:
				ret = _obtainPushSupplierProxy(channelName);
				triesNotifyChannelCreationCount = 10;
				while (triesNotifyChannelCreationCount > 0) {
					try {
						notifyChannelCreation(channelName, proxyType);
						triesNotifyChannelCreationCount = 0;
					} catch (RuntimeException e) {
						log.warn("obtainEventChannel : failed to obtain channel: "+channelName +", tries: " + triesNotifyChannelCreationCount );
						if (e.getMessage().startsWith("Service failed: client timeout reached")) {
							triesNotifyChannelCreationCount--;
						} else {
							throw e;
						}
						if (triesNotifyChannelCreationCount == 0) {
							throw e;
						}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {

						}
					}
				}
				break;
			case REMOTE_PUSH_CONSUMER_PROXY:
				//ret = _obtainRemoteConsumerProxy(channelName);
				break;
			case REMOTE_PUSH_SUPPLIER_PROXY:
				//ret = _obtainRemoteSupplierProxy(channelName);
				break;
			default:
				throw new RuntimeException("Unsupported proxy type: " + proxyType);
		}
		dump();
		return ret;
	}
	*/
	
	/*
	 * New, separately for remote 
	 *
	public synchronized RemoteProxy obtainRemoteEventChannelProxy(
			String channelName, ProxyType proxyType) {
		RemoteProxy ret = null;
		switch (proxyType) {
		case REMOTE_PUSH_CONSUMER_PROXY:
			ret = _obtainRemoteConsumerProxy(channelName);
			break;
		case REMOTE_PUSH_SUPPLIER_PROXY:
			ret = _obtainRemoteSupplierProxy(channelName);
			break;
		default:
			throw new RuntimeException("Unsupported remote proxy type: " + proxyType);
		}
		logDump();
		return ret;
	}
	
	private RemoteProxy _obtainRemoteConsumerProxy(String channelName) {
		RemotePushConsumerProxy proxy = (RemotePushConsumerProxy) remotePushConsumerProxies.get(channelName);
		if (proxy != null)
			return proxy;			
		
		proxy = RemoteSupportFactory.createRemotePushConsumerProxy(channelName);
		
		// Add remote suppliers proxys from registry
		List<RemoteProxy> remoteSupplierProxys;		
		try {
			remoteSupplierProxys = eventServiceRegistry.getSuppliersForChannel(channelName);
		} catch (EventServiceException e) {
			log.error("Can't get supplier proxys for channel: " + channelName, e);
			throw new RuntimeException("Can't get supplier proxys for channel: " + channelName);
		}
		for(RemoteProxy remoteProxy : remoteSupplierProxys) {
			try {
				proxy.remoteAdd((RemotePushSupplierProxy) remoteProxy);
			} catch (RemoteException e) {
				log.warn("Skip adding remote supplier proxy for the new remote consumer for channel: " + channelName, e);
			}
		}
		
		// Export remote proxy		
		RemotePushConsumerProxy exportedProxy;
		try {
			exportedProxy = (RemotePushConsumerProxy) UnicastRemoteObject.exportObject(proxy, 0);
		} catch (RemoteException e) {
			log.error("Can't export consumer proxy for channel: " + channelName, e);
			throw new RuntimeException("Can't export consumer proxy for channel: " + channelName);
		}
		
		// Register new proxy in Reg
		try {
			eventServiceRegistry.registerConsumer(channelName, exportedProxy);
		} catch (EventServiceException e) {
			log.error("Can't register new consumer proxy in registry for channel: " + channelName, e);
			throw new RuntimeException("Can't register new consumer proxy in registry for channel: " + channelName);
		}
		
		// Add new remote proxy to local storage
		remotePushConsumerProxies.put(channelName, proxy);
		
//		proxy = createRemoteConsumerProxy(channelName);		
		return proxy;
	}

	private RemoteProxy _obtainRemoteSupplierProxy(String channelName) {
		RemotePushSupplierProxy proxy = (RemotePushSupplierProxy) remotePushSupplierProxies.get(channelName);
		if (proxy != null)
			return proxy;			
		
		proxy = remoteSupportFactory.createRemotePushSupplierProxy(channelName);		
		
		// Export remote proxy		
		RemotePushSupplierProxy exportedProxy;
		try {
			exportedProxy = (RemotePushSupplierProxy) UnicastRemoteObject.exportObject(proxy, 0);
		} catch (RemoteException e) {
			log.error("Can't export supplier proxy for channel: " + channelName, e);
			throw new RuntimeException("Can't export supplier proxy for channel: " + channelName);
		}
		
		// Add new supplier proxy for existed consumer proxys
		List<RemoteProxy> remoteConsumerProxys;		
		try {
			remoteConsumerProxys = eventServiceRegistry.getConsumersForChannel(channelName);
		} catch (EventServiceException e) {
			log.error("Can't get consumer proxys for channel: " + channelName, e);
			throw new RuntimeException("Can't get consumer proxys for channel: " + channelName);
		}
		for(RemoteProxy remoteProxy : remoteConsumerProxys) {
			try {
				((RemotePushConsumerProxy) remoteProxy).addRemoteSupplierProxy(exportedProxy);
			} catch (RemoteException e) {
				log.warn("Skip adding new supplier proxy for existed consumer proxy for channel: " + channelName, e);
			}
		}
		
		// Register new proxy in Reg
		try {
			eventServiceRegistry.registerSupplier(channelName, exportedProxy);
		} catch (EventServiceException e) {
			log.error("Can't register new supplier proxy in registry for channel: " + channelName, e);
			throw new RuntimeException("Can't register new supplier proxy in registry for channel: " + channelName);
		}
		
		// Add new remote proxy to local storage
		remotePushSupplierProxies.put(channelName, proxy);
		
//		proxy = createRemoteSuppliserProxy(channelName);		
		return proxy;
	}
	
	private RemoteEventChannel _obtainPushConsumerProxy(String channelName) {
		EventChannelPushConsumerProxy proxy = (EventChannelPushConsumerProxy) pushConsumerProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = createPushConsumerProxy(channelName);
		pushConsumerProxies.put(channelName, proxy);
		return proxy;
	}

	private RemoteEventChannel _obtainPushSupplierProxy(String channelName) {
		EventChannelPushSupplierProxy proxy = (EventChannelPushSupplierProxy) pushSupplierProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = createPushSupplierProxy(channelName);
		pushSupplierProxies.put(channelName, proxy);
		return proxy;
	}

	

//	private RemotePushSupplierProxy createRemoteSupplierProxy(String channelName) {
//		RemotePushSupplierProxy proxy = remoteSupportFactory.createRemoteEventChannelSupplierProxy(channelName);
//		log.debug("Created " + proxy);
//		connectSupplierProxy(channelName, proxy);
//		return proxy;
//	}

	private EventChannelPushSupplierProxy createPushSupplierProxy(String channelName) {
		EventChannelPushSupplierProxy proxy = new EventChannelPushSupplierProxy(channelName);
		log.debug("Created " + proxy);
		connectSupplierProxy(channelName, proxy);
		return proxy;
	}

	private void connectSupplierProxy(String channelName, EventChannelSupplierProxy proxy) {
		List<EventChannelConsumerProxy> consumers = getConsumerProxies(channelName);
		log.debug("Connecting " + consumers + " to " + proxy);
		for (int i = 0, n = consumers.size(); i < n; i++) {
			log.debug("connecting " + consumers.get(i));
			proxy.addConsumerProxy(consumers.get(i));
		}
	}

	private EventChannelPushConsumerProxy createPushConsumerProxy(String channelName) {
		EventChannelPushConsumerProxy proxy = new EventChannelPushConsumerProxy(channelName);
		log.debug("created " + proxy);
		connectConsumerProxy(channelName, proxy);
		return proxy;
	}

//	private RemotePushConsumerProxy createRemoteConsumerProxy(String channelName) {
//		RemotePushConsumerProxy proxy = remoteSupportFactory.createRemoteEventChannelConsumerProxy(channelName);
//		log.debug("Created " + proxy);
//		connectRemoteConsumerProxy(channelName, proxy);
//		return proxy;
//	}

//	private void connectRemoteConsumerProxy(String channelName, RemotePushConsumerProxy proxy) {
//		List<EventChannelSupplierProxy> suppliers = getSupplierProxies(channelName);
//		log.debug("connecting " + proxy + " to " + suppliers);
//		for (int i = 0, n = suppliers.size(); i < n; i++) {
//			log.debug("connecting " + proxy + " to " + suppliers.get(i));
//			suppliers.get(i).addConsumerProxy(proxy);
//		}
//	}
	
	private void connectConsumerProxy(String channelName, EventChannelConsumerProxy proxy) {
		List<EventChannelSupplierProxy> suppliers = getSupplierProxies(channelName);
		log.debug("connecting " + proxy + " to " + suppliers);
		for (int i = 0, n = suppliers.size(); i < n; i++) {
			log.debug("connecting " + proxy + " to " + suppliers.get(i));
			suppliers.get(i).addConsumerProxy(proxy);
		}
	}

	private List<EventChannelConsumerProxy> getConsumerProxies(String channelName) {
		List<EventChannelConsumerProxy> ret = new ArrayList<EventChannelConsumerProxy>();
		EventChannelConsumerProxy pushConsumerProxy = (EventChannelConsumerProxy) pushConsumerProxies.get(channelName);
		if (pushConsumerProxy != null)
			ret.add(pushConsumerProxy);

		EventChannelConsumerProxy remoteConsumerProxy = (EventChannelConsumerProxy) remotePushConsumerProxies.get(channelName);
		if (remoteConsumerProxy != null)
			ret.add(remoteConsumerProxy);

		return ret;
	}

	private List<EventChannelSupplierProxy> getSupplierProxies(String channelName) {
		List<EventChannelSupplierProxy> ret = new ArrayList<EventChannelSupplierProxy>();
		EventChannelSupplierProxy pushSupplierProxy = (EventChannelSupplierProxy) pushSupplierProxies.get(channelName);
		if (pushSupplierProxy != null)
			ret.add(pushSupplierProxy);

		EventChannelSupplierProxy remoteSupplierProxy = (EventChannelSupplierProxy) remotePushSupplierProxies.get(channelName);
		if (remoteSupplierProxy != null)
			ret.add(remoteSupplierProxy);

		return ret;
	}
*/
	
	
}
