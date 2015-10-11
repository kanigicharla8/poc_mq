package com.bnsf.drools.poc.tranformer;

import com.google.common.base.Splitter;

import com.bnsf.drools.poc.events.AEIEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.joda.time.DateTime;

import java.util.Iterator;


public class AEITransformation {

    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String strEvent = (String) in.getBody();

        // Handling incoming message
        //split the message
        Splitter pipeSplitter = Splitter.on('|').trimResults();
        Iterable<String> tokens = pipeSplitter.split(strEvent);
        Iterator<String> tokenIterator = tokens.iterator();

        AEIEvent aeiEvent = new AEIEvent();
        aeiEvent.setAEIReaderId(tokenIterator.next());
        aeiEvent.setLocomotiveId(tokenIterator.next());
        DateTime dt = new DateTime(tokenIterator.next().substring(0, 28));
        aeiEvent.setEventTime(dt.toDate());

        //Publish the AEI Event
        exchange.getOut().setBody(aeiEvent);
    }

}
