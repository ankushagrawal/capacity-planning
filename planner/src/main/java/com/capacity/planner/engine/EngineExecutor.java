package com.capacity.planner.engine;


import com.capacity.planner.engine.impl.ResourceAllocationEngine;

/**
 * Created by ankush.a on 15/03/17.
 */
public class EngineExecutor {

    public void execute(String engineClassName,Long planId){
        if(engineClassName.equalsIgnoreCase("ResourceAllocationEngine")){
            new ResourceAllocationEngine().distribute(planId);
        }
    }
}
