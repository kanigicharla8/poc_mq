/**
 * 
 */
package com.bnsf.drools.poc.events.internal;

import com.bnsf.drools.poc.events.AbstractBNSFEvent;

/**
 * Represents LocomotiveInventory missing in the cache
 * 
 * @author rakesh
 *
 */
public class LocomotiveInventoryMissingInCacheEvent extends AbstractBNSFEvent {
    protected String locomotiveId;

    public String getLocomotiveId() {
        return locomotiveId;
    }

    public void setLocomotiveId(String locomotiveId) {
        this.locomotiveId = locomotiveId;
    }
}
