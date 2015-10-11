/**
 * 
 */
package com.bnsf.drools.poc.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rakesh
 *
 */
public abstract class AbstractBNSFModel<ID extends Serializable> implements BNSFModel<ID>{
	protected String trainId;
	protected Date createdTimestamp;
	protected Date lastUpdatedTimestamp;

	//for optimistic locking
	protected int recordVersionNumber;
	
	public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Date getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public int getRecordVersionNumber() {
		return recordVersionNumber;
	}

	public void setRecordVersionNumber(int recordVersionNumber) {
		this.recordVersionNumber = recordVersionNumber;
	}

	
}
