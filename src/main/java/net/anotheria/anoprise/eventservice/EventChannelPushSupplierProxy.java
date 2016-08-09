package net.anotheria.anoprise.eventservice;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class EventChannelPushSupplierProxy extends AbstractEventChannel 
		implements EventChannelSupplierProxy{

	private List<EventChannelConsumerProxy> consumerProxies;
	
	
	public EventChannelPushSupplierProxy(String name){
		super(name);
		consumerProxies = new CopyOnWriteArrayList<>();
	}
	
	

	public void addConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("addConsumer");
	}

	public void push(Event e) {
		push(e, false);			
	}
	
	protected void push(Event e, boolean localOnly){
		for (EventChannelConsumerProxy proxy : consumerProxies){
			if (localOnly && (!proxy.isLocal())){
                log.debug("Skiping proxy: {}", proxy);
			}else{
				//log.debug("delivering to proxy: "+proxy);
				try{
					proxy.pushEvent(e); 
				}catch(RuntimeException ex){
                    log.error("pushEvent("+e+ ')', ex);
				}
			}
		}
	}

	public void removeConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("removeConsumer");
	}
	

	public void addConsumerProxy(EventChannelConsumerProxy proxy) {
		out("Added consumer proxy: "+proxy);
		consumerProxies.add(proxy);
	}

	public void removeConsumerProxy(EventChannelConsumerProxy proxy) {
		consumerProxies.remove(proxy);
	}
	
	@Override
	public String toString(){
		return "PushSupplierProxy "+getName()+", connected to:"+consumerProxies;
	}
	

	public boolean isLocal() {
		return true;
	}

}
