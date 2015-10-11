package com.bnsf.drools.poc.events.internal;

import com.bnsf.drools.poc.events.BNSFEvent;

/**
 * Created by rakesh on 9/23/15.
 */
public class TrainUpdatedWithGPSLocoDataEvent implements BNSFEvent {
    protected String locomotiveId;
    protected String trainId;

    public TrainUpdatedWithGPSLocoDataEvent(String locomotiveId, String trainId) {
        this.locomotiveId = locomotiveId;
        this.trainId = trainId;
    }

    public String getLocomotiveId() {
        return locomotiveId;
    }

    public void setLocomotiveId(String locomotiveId) {
        this.locomotiveId = locomotiveId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
}
