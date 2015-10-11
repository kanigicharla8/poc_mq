package com.bnsf.drools.poc.events.internal;

import com.bnsf.drools.poc.events.BNSFEvent;
import com.bnsf.drools.poc.events.GPSLocoEvent;

/**
 * Created by rakesh on 9/23/15.
 */
public class MissingAEIEvent implements BNSFEvent {
    private GPSLocoEvent gpsLocoEvent;

    public MissingAEIEvent(GPSLocoEvent gpsLocoEvent) {
        this.gpsLocoEvent = gpsLocoEvent;
    }

    public GPSLocoEvent getGpsLocoEvent() {
        return gpsLocoEvent;
    }

    public void setGpsLocoEvent(GPSLocoEvent gpsLocoEvent) {
        this.gpsLocoEvent = gpsLocoEvent;
    }
}
