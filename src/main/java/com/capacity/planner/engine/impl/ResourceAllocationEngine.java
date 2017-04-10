package com.capacity.planner.engine.impl;


import Jama.Matrix;
import com.capacity.planner.engine.Engine;

import java.util.Arrays;

import static java.util.Arrays.stream;

/**
 * Created by ankush.a on 15/03/17.
 */
public class ResourceAllocationEngine implements Engine {

    private VogelMethod vogelMethod;
    private ModiMethod modiMethod;

    public ResourceAllocationEngine() {
        vogelMethod = new VogelMethod();
        modiMethod = new ModiMethod();
    }

    public static void main(String[] args){
        new ResourceAllocationEngine().distribute(1L);
    }

    @Override
    public void distribute(Long requestId) {}

    public int[][] distribute(Long requestId,int[] demand, int[] supply, int[][] costs) {
        //get all jobs based on request id
        //get all sources based on request id
        //grouping of jobs based on filters constraint of sources
        //allocation logic

        //input needed
        //work array, supply array, cost matrix needed
//        int[] demand = {200,100,300};
//        int[] supply = {150,175,275};
//        int[][] costs = {{6,8,10}, {7,11,11},
//                {4,5,12}};

//        int[] demand = {10,10,10,10};
//        int[] supply = {12,17,11};
//        int[][] costs = {{500,750,300,450}, {650,800,400,600},
//                {400,700,500,550}};
        try {
            int[][] result = vogelMethod.executeVogelMethod(demand, supply, costs);
            stream(result).forEach(a -> System.out.println(Arrays.toString(a)));

            int[][] modiResult = modiMethod.optimizeAllocations(result,costs,demand,supply);
            printCost(result, costs, demand, supply);
            stream(modiResult).forEach(a -> System.out.println(Arrays.toString(a)));
            return modiResult;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void printCost(int[][] result, int[][] costs,int[] demand, int[] supply) {
        long cost = 0;
        for(int i =0;i<supply.length;i++){
            for(int j = 0; j<demand.length;j++){
                if(result[i][j]>0) {
                    cost = cost + costs[i][j] * result[i][j];
                }
            }
        }
        System.out.println("Cost is : "+cost);
    }


}
