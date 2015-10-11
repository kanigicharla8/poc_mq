/**
 * 
 */
package com.bnsf.drools.poc.cep.consumer.jms;

import com.bnsf.drools.poc.drools.DroolsSessionManager;
import com.bnsf.drools.poc.events.AEIEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AEIEventCepConsumer {

	private final transient Logger logger = LoggerFactory.getLogger(getClass());

	DroolsSessionManager sessionManager;
	
	public void consumeEvent(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
	    AEIEvent aeiEvent = (AEIEvent)in.getBody();

		KieSession session = getSessionManager().getSession();
		EntryPoint aeiHarvestStream = session.getEntryPoint("AEI Harvest Stream");
		aeiHarvestStream.insert(aeiEvent);
	}

	public DroolsSessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(DroolsSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
}
