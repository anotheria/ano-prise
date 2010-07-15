package net.anotheria.anoprise.eventserviceV2.registry;

import java.util.List;

import net.anotheria.anoprise.eventserviceV2.remote.RemoteProxy;
import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.DistributeMe;


/**
 * Event service distributed proxys storage.
 * SHOULD BE up and ready before remote channel obtaining from event service.
 * 
 * @author vkazhdan
 */
@DistributeMe(
		initcode={"MetaFactory.addFactoryClass(net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistry.class, Extension.LOCAL, net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistryFactory.class);"}
)
public interface EventServiceRegistry extends Service {

	/**
	 * Get all channel names, registered till current time.
	 * 
	 * @return Channel names list.
	 * @throws EventServiceRegistryException.
	 */
	List<String> getChannelNames() 
		throws EventServiceRegistryException;
	
	/**
	 * Get all registered push consumer proxys for the channel.
	 * 
	 * @param channelName
	 * @return Proxys list.
	 * @throws EventServiceRegistryException
	 */
	List<RemoteProxy> getRemotePushConsumerProxys(String channelName)
		throws EventServiceRegistryException;

	/**
	 * Get all registered push supplier proxys for the channel.
	 * 
	 * @param channelName
	 * @return Proxys list.
	 * @throws EventServiceRegistryException
	 */
	List<RemoteProxy> getRemotePushSupplierProxys(String channelName)
		throws EventServiceRegistryException;

	/**
	 * Register remote consumer proxy in the channel.
	 * 
	 * @param channelName
	 * @param remoteProxy
	 * @throws EventServiceRegistryException
	 */
	public void registerRemoteConsumerProxy(String channelName, RemoteProxy remoteProxy)
		throws EventServiceRegistryException;

	/**
	 * Register remote supplier proxy in the channel.
	 * 
	 * @param channelName
	 * @param remoteProxy
	 * @throws EventServiceRegistryException
	 */
	public void registerRemoteSupplierProxy(String channelName, RemoteProxy remoteProxy)
		throws EventServiceRegistryException;
	
	/**
	 * Notify about consumer proxy unavailability. 
	 * 
	 * @param remoteProxy
	 * @throws EventServiceRegistryException
	 */
	public void notifyRemoteConsumerProxyUnavailable(RemoteProxy remoteProxy)
		throws EventServiceRegistryException;

	/**
	 * Notify about supplier proxy unavailability. 
	 * 
	 * @param remoteProxy
	 * @throws EventServiceRegistryException
	 */
	public void notifyRemoteSupplierProxyUnavailable(RemoteProxy remoteProxy)
		throws EventServiceRegistryException;
}
