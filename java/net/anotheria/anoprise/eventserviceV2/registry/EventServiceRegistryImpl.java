package net.anotheria.anoprise.eventserviceV2.registry;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.remote.RemoteProxy;

import org.apache.log4j.Logger;



public class EventServiceRegistryImpl implements EventServiceRegistry {
       
        private Map<String,ChannelEntry> channels;
        
        private static final Logger log = Logger.getLogger(EventServiceRegistryImpl.class);;
        
        public EventServiceRegistryImpl(){
                channels = new ConcurrentHashMap<String,ChannelEntry>();
        }
            
        @Override
        public List<String> getChannelNames() 
			throws EventServiceRegistryException {
        	return Arrays.asList(channels.keySet().toArray(new String[]{}));
        }
        
        
        @Override
		public List<RemoteProxy> getRemotePushConsumerProxys(String channelName)
				throws EventServiceRegistryException {
        	return getChannelEntry(channelName).pushConsumerProxys;
		}

		@Override
		public List<RemoteProxy> getRemotePushSupplierProxys(String channelName)
			throws EventServiceRegistryException {
			return getChannelEntry(channelName).pushSupplierProxys;
		}

		@Override
		public void registerRemoteConsumerProxy(String channelName, RemoteProxy remoteProxy)
				throws EventServiceRegistryException {
			log.debug("Registering new consumer proxy for channel: " + channelName + " proxy: "+remoteProxy);
            ChannelEntry entry = getChannelEntry(channelName);
            entry.addConsumerProxy(remoteProxy);
		}

		@Override
		public void registerRemoteSupplierProxy(String channelName, RemoteProxy remoteProxy)
				throws EventServiceRegistryException {
			log.debug("Registering new supplier for channel: " + channelName + " proxy: " + remoteProxy);
            ChannelEntry entry = getChannelEntry(channelName);
            entry.addSupplierProxy(remoteProxy);
		}

		@Override
		public void notifyRemoteConsumerProxyUnavailable(RemoteProxy remoteProxy)
				throws EventServiceRegistryException {
			log.debug("Removing unavailable consumer proxy: " + remoteProxy);
						
            for (Iterator<ChannelEntry> it = channels.values().iterator(); it.hasNext();){
                    it.next().removeConsumerProxy(remoteProxy);
            }
		}

		@Override
		public void notifyRemoteSupplierProxyUnavailable(RemoteProxy remoteProxy)
				throws EventServiceRegistryException {
			log.debug("Removing unavailable supplier proxy: " + remoteProxy);

            for (Iterator<ChannelEntry> it = channels.values().iterator(); it.hasNext();) {
                    it.next().removeSupplierProxy(remoteProxy);
            }
		}

		private ChannelEntry getChannelEntry(String channelName){
                ChannelEntry entry = channels.get(channelName);
                if (entry == null) {
                        entry = new ChannelEntry(channelName);
                        channels.put(channelName, entry);
                }                
                return entry;
        }
        
        /**
         * Channel entry inner class.
         */
        private class ChannelEntry {
                private String channelName;
                private List<RemoteProxy> pushSupplierProxys;
                private List<RemoteProxy> pushConsumerProxys;
                
                private ChannelEntry(String channelName){
                        this.channelName = channelName;
                        pushSupplierProxys = new CopyOnWriteArrayList<RemoteProxy>();
                        pushConsumerProxys = new CopyOnWriteArrayList<RemoteProxy>();
                }
                                
                private void addSupplierProxy(RemoteProxy remoteProxy){
                	((CopyOnWriteArrayList<RemoteProxy>) pushSupplierProxys).addIfAbsent(remoteProxy);                	
                }
                
                private void addConsumerProxy(RemoteProxy remoteProxy){
                	((CopyOnWriteArrayList<RemoteProxy>) pushConsumerProxys).addIfAbsent(remoteProxy);
                }
                
                private void removeSupplierProxy(RemoteProxy remoteProxy){
                        pushSupplierProxys.remove(remoteProxy);
                }

                private void removeConsumerProxy(RemoteProxy remoteProxy){
                        pushConsumerProxys.remove(remoteProxy);
                }

				@Override
				public String toString() {
					return "ChannelEntry [channelName=" + channelName
							+ ", pushSupplierProxys=" + pushSupplierProxys
							+ ", pushConsumerProxys=" + pushConsumerProxys
							+ "]";
				}
                
        }
       
        
        
}
