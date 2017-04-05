package com.capacity.planner.engine.impl;

/**
 * Created by ankush.a on 17/03/17.
 */
public class LoopDirection {

    private Coordinate startPosition;
    public Coordinate getFirstStop() {
        return firstStop;
    }

    public void setFirstStop(Coordinate firstStop) {
        this.firstStop = firstStop;
    }

    public Coordinate getSecondStop() {
        return secondStop;
    }

    public void setSecondStop(Coordinate secondStop) {
        this.secondStop = secondStop;
    }

    public Coordinate getThirdStop() {
        return thirdStop;
    }

    public void setThirdStop(Coordinate thirdStop) {
        this.thirdStop = thirdStop;
    }

    private Coordinate firstStop;
    private Coordinate secondStop;
    private Coordinate thirdStop;

    public Coordinate getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Coordinate startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public String toString() {
        return "LoopDirection{" +
                "startPosition=" + startPosition +
                ", firstStop=" + firstStop +
                ", secondStop=" + secondStop +
                ", thirdStop=" + thirdStop +
                '}';
    }
}
