package com.capacity.planner.engine.impl;


import Jama.Matrix;
import com.capacity.planner.engine.Engine;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Arrays.stream;

/**
 * Created by ankush.a on 15/03/17.
 */
public class ResourceAllocationEngine implements Engine {


    public static void main(String[] args){
        new ResourceAllocationEngine().distribute(1L);
    }

    @Override
    public void distribute(Long requestId) {
        //get all jobs based on request id
        //get all sources based on request id
        //grouping of jobs based on filters constraint of sources
        //allocation logic

        //input needed
        //work array, supply array, cost matrix needed
        int[] demand = {10,10,10,10};
        int[] supply = {12,17,11};
        int[][] costs = {{500,750,300,450}, {650,800,400,600},
                {400,700,500,550}};
        try {
            int[][] result = new VogelMethod().executeVogelMethod(demand, supply, costs);
            stream(result).forEach(a -> System.out.println(Arrays.toString(a)));

            int[][] ModiResult = new ModiMethod().optimizeAllocations(result,costs,demand,supply);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
