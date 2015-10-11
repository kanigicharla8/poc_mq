/**
 * 
 */
package com.bnsf.drools.poc.cep.consumer.jms;

import com.bnsf.drools.poc.drools.DroolsSessionManager;
import com.bnsf.drools.poc.events.GPSLocoEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GPSEventCepConsumer {

	private final transient Logger logger = LoggerFactory.getLogger(getClass());

	DroolsSessionManager sessionManager;

	public void consumeEvent(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
	    GPSLocoEvent gpsEvent = (GPSLocoEvent)in.getBody();

		KieSession session = getSessionManager().getSession();
		EntryPoint gpsHarvestStream = session.getEntryPoint("GPS Harvest Stream");
		//insert event
		gpsHarvestStream.insert(gpsEvent);

	}

	public DroolsSessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(DroolsSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
