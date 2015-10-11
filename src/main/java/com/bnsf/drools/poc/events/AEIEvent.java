package com.bnsf.drools.poc.events;

/**
 * 
 * @author rakesh
 *
 */
public class AEIEvent extends AbstractBNSFEvent {
	private String AEIReaderId;
	protected String locomotiveId;

	public String getAEIReaderId() {
		return AEIReaderId;
	}

	public void setAEIReaderId(String AEIReaderId) {
		this.AEIReaderId = AEIReaderId;
	}

	public String getLocomotiveId() {
		return locomotiveId;
	}

	public void setLocomotiveId(String locomotiveId) {
		this.locomotiveId = locomotiveId;
	}
}
