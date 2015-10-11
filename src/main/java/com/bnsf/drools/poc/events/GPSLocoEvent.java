package com.bnsf.drools.poc.events;

/**
 * 
 * @author rakesh
 *
 */
public class GPSLocoEvent extends AbstractBNSFEvent{
	private double latitude;
	private double longitude;
	protected String locomotiveId;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLocomotiveId() {
		return locomotiveId;
	}

	public void setLocomotiveId(String locomotiveId) {
		this.locomotiveId = locomotiveId;
	}
}
