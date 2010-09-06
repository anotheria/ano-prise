package net.anotheria.anoprise.eventservice;

import net.anotheria.anoprise.eventservice.util.QueuedEventSenderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={TestLocalEvents.class, TestEventChannelPushConsumerProxy.class, TestEventChannelPushSupplierProxy.class, QueuedEventSenderTest.class} )
public class EventServiceTestSuite {

}
