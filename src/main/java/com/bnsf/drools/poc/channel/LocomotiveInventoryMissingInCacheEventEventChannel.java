package com.bnsf.drools.poc.channel;

import com.bnsf.drools.poc.events.internal.LocomotiveInventoryMissingInCacheEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rakesh on 9/16/15.
 */
public class LocomotiveInventoryMissingInCacheEventEventChannel extends AbstractBNSFChannel<LocomotiveInventoryMissingInCacheEvent>{
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(LocomotiveInventoryMissingInCacheEvent obj) {
        logger.error("Loco Inventory is missing in the cache {}",obj);
    }
}
