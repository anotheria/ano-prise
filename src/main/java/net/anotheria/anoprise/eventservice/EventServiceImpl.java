package net.anotheria.anoprise.eventservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The implementation of the event service.
 * 
 * @author lrosenberg Created on 22.09.2004
 */
public class EventServiceImpl implements EventService {

	private static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
	private static EventServiceImpl instance = new EventServiceImpl();

	private ConcurrentMap<String, EventChannelPushConsumerProxy>	pushConsumerProxies;
	private ConcurrentMap<String, EventChannelPushSupplierProxy>	pushSupplierProxies;
	private ConcurrentMap<String, RemoteEventChannelConsumerProxy>	remoteConsumerProxies;
	private ConcurrentMap<String, RemoteEventChannelSupplierProxy>	remoteSupplierProxies;

	private List<EventServiceListener> listeners;
	
	private RemoteEventChannelSupportFactory remoteSupportFactory;

	private EventServiceImpl() {
		init();
	}
	
	private void init(){
		pushConsumerProxies = new ConcurrentHashMap<>(10);
		pushSupplierProxies = new ConcurrentHashMap<>(10);
		remoteConsumerProxies = new ConcurrentHashMap<>(10);
		remoteSupplierProxies = new ConcurrentHashMap<>(10);

		listeners = new CopyOnWriteArrayList<>();
	}
	
	public void resetForUnitTesting(){
		init();
	}

	
	
	public static EventService getInstance() {
		return instance;
	}

	public EventChannel obtainEventChannel(String channelName, EventServiceParticipant participant) {

		ProxyType type = ProxyType.NONE;
		if (participant instanceof EventServicePushConsumer)
			type = ProxyType.PUSH_CONSUMER_PROXY;
		if (participant instanceof EventServicePushSupplier)
			type = ProxyType.PUSH_SUPPLIER_PROXY;

		if (participant instanceof RemoteEventServiceConsumer)
			type = ProxyType.REMOTE_CONSUMER_PROXY;

		if (participant instanceof RemoteEventServiceSupplier)
			type = ProxyType.REMOTE_SUPPLIER_PROXY;

		if (type == ProxyType.NONE)
			throw new IllegalArgumentException("Unsupported participant type: " + participant);

		return obtainEventChannel(channelName, type);
	} 

	public  EventChannel obtainEventChannel(String channelName, ProxyType proxyType) {
        log.debug("Creating event channel: {} of type {}", channelName, proxyType);
        EventChannel ret;
        switch (proxyType) {
			case PUSH_CONSUMER_PROXY:
				ret = _obtainPushConsumerProxy(channelName);
				break;
			case PUSH_SUPPLIER_PROXY:
				ret = _obtainPushSupplierProxy(channelName);
				break;
			case REMOTE_CONSUMER_PROXY:
				ret = _obtainRemoteConsumerProxy(channelName);
				break;
			case REMOTE_SUPPLIER_PROXY:
				ret = _obtainRemoteSupplierProxy(channelName);
				break;
			default:
				throw new IllegalArgumentException("Unsupported proxy type: " + proxyType);
		}
		dump();
		return ret;
	}

	private EventChannel _obtainRemoteConsumerProxy(String channelName) {
		RemoteEventChannelConsumerProxy proxy = remoteConsumerProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = remoteSupportFactory.createRemoteEventChannelConsumerProxy(channelName);
		RemoteEventChannelConsumerProxy old = remoteConsumerProxies.putIfAbsent(channelName, proxy);
		if (old!=null)
			return old;
        log.debug("Created {}", proxy);
		connectConsumerProxy(channelName, proxy);
		return proxy;
	}

	private EventChannel _obtainPushConsumerProxy(String channelName) {
		EventChannelPushConsumerProxy proxy = pushConsumerProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = new EventChannelPushConsumerProxy(channelName);
		EventChannelPushConsumerProxy old = pushConsumerProxies.putIfAbsent(channelName, proxy);
		if (old!=null)
			return old;

        log.debug("Created {}", proxy);
		connectConsumerProxy(channelName, proxy);
		notifyChannelCreation(channelName, ProxyType.PUSH_CONSUMER_PROXY);
		return proxy;
	}

	private EventChannel _obtainPushSupplierProxy(String channelName) {
		EventChannelPushSupplierProxy proxy = pushSupplierProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = new EventChannelPushSupplierProxy(channelName);
		EventChannelPushSupplierProxy old = pushSupplierProxies.putIfAbsent(channelName, proxy);
		if (old!=null)
			return old;

        log.debug("Created {}", proxy);
		connectSupplierProxy(channelName, proxy);
		notifyChannelCreation(channelName, ProxyType.PUSH_SUPPLIER_PROXY);
		return proxy;
	}

	private EventChannel _obtainRemoteSupplierProxy(String channelName) {
		RemoteEventChannelSupplierProxy proxy = remoteSupplierProxies.get(channelName);
		if (proxy != null)
			return proxy;
		proxy = remoteSupportFactory.createRemoteEventChannelSupplierProxy(channelName);
		RemoteEventChannelSupplierProxy old = remoteSupplierProxies.putIfAbsent(channelName, proxy);
		if (old!=null)
			return old;
        log.debug("Created {}", proxy);
		connectSupplierProxy(channelName, proxy);
		return proxy;
	}


	private void connectSupplierProxy(String channelName, EventChannelSupplierProxy proxy) {
		List<EventChannelConsumerProxy> consumers = getConsumerProxies(channelName);
        log.debug("Connecting {} to {}", consumers, proxy);
		for (int i = 0, n = consumers.size(); i < n; i++) {
            log.debug("Connecting {}", consumers.get(i));
			proxy.addConsumerProxy(consumers.get(i));
		}
	}

	private void connectConsumerProxy(String channelName, EventChannelConsumerProxy proxy) {
		List<EventChannelSupplierProxy> suppliers = getSupplierProxies(channelName);
        log.debug("Connecting {} to {}", proxy, suppliers);
		for (int i = 0, n = suppliers.size(); i < n; i++) {
            log.debug("Connecting {} to {}", proxy, suppliers.get(i));
			suppliers.get(i).addConsumerProxy(proxy);
		}
	}

	private List<EventChannelConsumerProxy> getConsumerProxies(String channelName) {
		List<EventChannelConsumerProxy> ret = new ArrayList<>();
		EventChannelConsumerProxy pushConsumerProxy = pushConsumerProxies.get(channelName);
		if (pushConsumerProxy != null) 
			ret.add(pushConsumerProxy);

		EventChannelConsumerProxy remoteConsumerProxy = remoteConsumerProxies.get(channelName);
		if (remoteConsumerProxy != null)
			ret.add(remoteConsumerProxy);
 
		return ret;
	}

	private List<EventChannelSupplierProxy> getSupplierProxies(String channelName) {
		List<EventChannelSupplierProxy> ret = new ArrayList<>();
		EventChannelSupplierProxy pushSupplierProxy = pushSupplierProxies.get(channelName);
		if (pushSupplierProxy != null)
			ret.add(pushSupplierProxy);

		EventChannelSupplierProxy remoteSupplierProxy = remoteSupplierProxies.get(channelName);
		if (remoteSupplierProxy != null)
			ret.add(remoteSupplierProxy);

		return ret;
	}

	private void dump() {
        log.debug("Consumer proxies: {}", pushConsumerProxies);
        log.debug("Supplier proxies: {}", pushSupplierProxies);
        log.debug("Remote Consumer proxies: {}", remoteConsumerProxies);
        log.debug("Remote Supplier proxies: {}", remoteSupplierProxies);

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
			}catch(RuntimeException e){
                log.error("Unexcepted exception in listener '"+listener+"', in call notifyChannelCreation("+channelName+", "+type+ ')', e);
			}
		}
	}
	
	public void setRemoteSupportFactory(RemoteEventChannelSupportFactory aFactory){
		remoteSupportFactory = aFactory;
	}
}
