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
        int[] demand = {500,300,400};
        int[] supply = {400,200,600};
        int[][] costs = {{15,17,10000}, {10000,16,20},
                {14,18,19}};
        try {
            int[][] result = new VogelMethod().executeVogelMethod(demand, supply, costs);
            stream(result).forEach(a -> System.out.println(Arrays.toString(a)));

            int[][] ModiResult = executeModiMethod(result,costs,demand,supply);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int[][] executeModiMethod(int[][] result, int[][] costs, int[] demand, int[] supply) {
        System.out.println("Initializing Modi method.....");
        final int nRows = supply.length;
        final int nCols = demand.length;

        if(canProceed(result,nRows,nCols)){
            System.out.println("Modi can be proceeded...");
            calculateUVValues(result,costs,nRows,nCols);
        }
        return new int[0][];
    }

    private void calculateUVValues(int[][] result, int[][] costs, int nRows, int nCols) {
        int[] colmnU = new int[nCols];
        int[] rowsV = new int[nRows];

        for(int i =0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(result[i][j]!=0){
                    //allocated cell
                    //compute u-v
//                    Matrix lhs = new Matrix(lhsArray);

                }

            }
        }

    }

    private boolean canProceed(int[][] result, int nRows, int nCols) {
        //check rows+colmn-1 = cells allocated condition
        //find how many cells are allocated
        int allocatedCells = 0;

        for(int i =0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(result[i][j]!=0)
                    allocatedCells++;
            }
        }
        if(allocatedCells == (nRows+nCols-1))
            return true;
        return false;
    }


}
