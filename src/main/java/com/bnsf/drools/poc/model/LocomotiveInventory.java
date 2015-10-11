package com.bnsf.drools.poc.model;

/**
 * 
 * @author rakesh
 *
 */
public class LocomotiveInventory extends AbstractBNSFModel<String>{

	private String locomotiveId;

	/**
	 * @see com.bnsf.drools.poc.model.BNSFModel#getId()
	 */
	public String getId() {
		return getLocomotiveId();
	}
	
	/*
	 * getters and setters
	 */
	public String getLocomotiveId() {
		return locomotiveId;
	}

	public void setLocomotiveId(String locomotiveId) {
		this.locomotiveId = locomotiveId;
	}

}
