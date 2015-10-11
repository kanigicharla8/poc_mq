package com.bnsf.drools.poc.events.internal;

import com.bnsf.drools.poc.events.BNSFEvent;

/**
 * Created by rakesh on 9/23/15.
 */
public class TrainUpdatedWithAEIDataEvent implements BNSFEvent {
    private String AEIReaderId;
    protected String locomotiveId;
    protected String trainId;

    public TrainUpdatedWithAEIDataEvent(String AEIReaderId, String locomotiveId, String trainId) {
        this.AEIReaderId = AEIReaderId;
        this.locomotiveId = locomotiveId;
        this.trainId = trainId;
    }

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

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
}
