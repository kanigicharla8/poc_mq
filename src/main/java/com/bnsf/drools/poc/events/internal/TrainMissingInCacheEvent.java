/**
 * 
 */
package com.bnsf.drools.poc.events.internal;

import com.bnsf.drools.poc.events.AbstractBNSFEvent;

/**
 * Represents train missing in the cache
 * 
 * @author rakesh
 *
 */
public class TrainMissingInCacheEvent extends AbstractBNSFEvent {

	private String trainId;

	public TrainMissingInCacheEvent(String trainId) {
		super();
		this.trainId = trainId;
	}

	public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
	
	
	
}
