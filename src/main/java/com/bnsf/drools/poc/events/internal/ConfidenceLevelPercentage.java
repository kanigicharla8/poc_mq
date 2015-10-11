package com.bnsf.drools.poc.events.internal;

/**
 * Created by rakesh on 9/16/15.
 */
public enum ConfidenceLevelPercentage {
    GPSLocoEvent(10,-5), AEIEvent(5,-2);

    private double positivePercentageLevel;
    private double negativePercentageLevel;

    ConfidenceLevelPercentage(double positivePercentageLevel, double negativePercentageLevel){
        this.positivePercentageLevel = positivePercentageLevel;
        this.negativePercentageLevel = negativePercentageLevel;
    }

    public double getPositivePercentageLevel() {
        return positivePercentageLevel;
    }

    public double getNegativePercentageLevel() {
        return negativePercentageLevel;
    }
}
