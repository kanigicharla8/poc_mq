package com.bnsf.drools.poc.tranformer;

import com.google.common.base.Splitter;

import com.bnsf.drools.poc.events.GPSLocoEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.joda.time.DateTime;

import java.util.Iterator;


public class GPSTransformation {

    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String strEvent = (String) in.getBody();

        // Handling incoming message

        //split the message
        Splitter pipeSplitter = Splitter.on('|').trimResults();
        Iterable<String> tokens = pipeSplitter.split(strEvent);
        Iterator<String> tokenIterator = tokens.iterator();

        //populate the event
        GPSLocoEvent gpsEvent = new GPSLocoEvent();

        String inDate = tokenIterator.next();
        DateTime dt = new DateTime(inDate.substring(0, 28));

        gpsEvent.setEventTime(new DateTime().toDate());
        gpsEvent.setLocomotiveId(tokenIterator.next());
        double latitude = Double.valueOf(tokenIterator.next());
        double longitude = Double.valueOf(tokenIterator.next());
        gpsEvent.setLatitude(latitude);
        gpsEvent.setLongitude(longitude);

        //Publish the GPS Event
        exchange.getOut().setBody(gpsEvent);

    }

}
