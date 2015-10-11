package com.bnsf.drools.poc.events.internal;

import com.google.common.base.MoreObjects;

import com.bnsf.drools.poc.events.BNSFEvent;
import com.bnsf.drools.poc.model.Train;

/**
 * Created by rakesh on 9/17/15.
 */
public class ConfidenceLevelChangeEvent implements BNSFEvent {
    private double oldPercentage;
    private double newPercentage;
    //could be train id instead
    private Train train;

    public ConfidenceLevelChangeEvent(double oldPercentage, double newPercentage, Train train) {
        this.oldPercentage = oldPercentage;
        this.newPercentage = newPercentage;
        this.train = train;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("train", train)
                .add("newPercentage", newPercentage)
                .add("oldPercentage", oldPercentage)
                .omitNullValues()
                .toString();
    }

    public double getOldPercentage() {
        return oldPercentage;
    }

    public void setOldPercentage(double oldPercentage) {
        this.oldPercentage = oldPercentage;
    }

    public double getNewPercentage() {
        return newPercentage;
    }

    public void setNewPercentage(double newPercentage) {
        this.newPercentage = newPercentage;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
