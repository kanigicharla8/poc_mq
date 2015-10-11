package com.bnsf.drools.poc.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rakesh on 9/16/15.
 */
public class GPSLocoEventChannel extends AbstractBNSFChannel<GPSLocoEventChannel>{
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(GPSLocoEventChannel obj) {
        logger.info("Received event from working mem {}",obj);
    }
}
