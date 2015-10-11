package com.bnsf.drools.poc.events;

import java.util.Date;

/**
 * 
 * @author rakesh
 *
 */
public abstract class AbstractBNSFEvent implements BNSFEvent{

	protected Date eventTime;

	/*
	 * getters/setters
	 */
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
}
