package com.bnsf.drools.poc.channel;

import com.bnsf.drools.poc.events.internal.ConfidenceLevelChangeEvent;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rakesh on 9/17/15.
 */
public class ConfidenceLevelChangeEventChannel extends AbstractBNSFChannel<ConfidenceLevelChangeEvent>{
    private static final String GPS_OUTBOUND_QUEUE = "activemq:queue:queue.event.gpsupdate.out";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    private ProducerTemplate camelTemplate = null;

    @Override
    public void handle(ConfidenceLevelChangeEvent obj) {
        logger.info("Confidence level changed for {} from {} to {}",obj.getTrain().getTrainId(), obj.getOldPercentage(), obj.getNewPercentage());
        //check for unit tests
        if(camelTemplate != null){
            camelTemplate.sendBody(GPS_OUTBOUND_QUEUE, obj.toString());
        }
    }

    public ProducerTemplate getCamelTemplate() {
        return camelTemplate;
    }

    public void setCamelTemplate(ProducerTemplate camelTemplate) {
        this.camelTemplate = camelTemplate;
    }
}
