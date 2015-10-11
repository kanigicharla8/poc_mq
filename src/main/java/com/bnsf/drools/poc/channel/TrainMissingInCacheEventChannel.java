package com.bnsf.drools.poc.channel;

import com.bnsf.drools.poc.events.internal.TrainMissingInCacheEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rakesh on 9/16/15.
 */
public class TrainMissingInCacheEventChannel extends AbstractBNSFChannel<TrainMissingInCacheEvent>{
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(TrainMissingInCacheEvent obj) {
        logger.error("Train is missing in the cache {}",obj);
    }
}
