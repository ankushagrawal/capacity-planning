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
            Matrix uvValues = calculateUVValues(result,costs,nRows,nCols);
            double[][] unallocatedMatrix = calculateCostOfUnallocatedCell(uvValues, result, costs, nRows, nCols);

            System.out.println("Unallocated cost matrix");
            stream(unallocatedMatrix).forEach(a -> System.out.println(Arrays.toString(a)));

            //check if all cost penalty is negative?
            int[] index = checkAndReturnMaximumPenaltyCell(unallocatedMatrix,nRows,nCols);
            if(index[0] != -1){
                //then proceed
                System.out.println("Optimal Solution Not achieved , create close loop and do allocation....");
                result = reallocateJobs(unallocatedMatrix, result,index,nRows,nCols);
                executeModiMethod(result,costs,demand,supply);
            }
            else{
                //got an optimal solution
                System.out.println("Optimal Solution achieved....");
                return result;
            }
        }
        return new int[0][0];
    }

    private int[][] reallocateJobs(double[][] unallocatedMatrix, int[][] result,int[] index,int nRows, int nCols) {
        //paths available are RTLD, RDRU , LTRD, LDLU

        int[] newIndex = new int[2];
        newIndex[0] = index[0];
        newIndex[1] = index[1];

        if(index[1] == 0){
            //it is first column, only movements are RIGHT viz. RTLD, RDRU
            for(int step = 1;step<=nCols-index[1];step++) {
                newIndex = checkRightMovement(step, unallocatedMatrix);
            }
        }
        else if(index[1] == nCols-1){
            //it is last column, only movements are LEFT viz.  LTRD, LDLU

        }
        else{
            //all possible movements
        }



        return new int[0][];
    }

    private int[] checkRightMovement(int step, double[][] unallocatedMatrix) {
        return new int[0];
    }

    private int[] checkAndReturnMaximumPenaltyCell(double[][] unallocatedMatrix,int nRows, int nCols) {
        int[] index = new int[]{-1,-1};
        double smallestValue = 0;
        for(int i =0;i<nRows;i++){
            for(int j=0;j<nCols;j++) {
                if(unallocatedMatrix[i][j] < smallestValue){
                    smallestValue = unallocatedMatrix[i][j];
                    index[0] = i;
                    index[1] = j;
                }
            }
        }
        return index;
    }

    private double[][] calculateCostOfUnallocatedCell(Matrix uvValues, int[][] result, int[][] costs, int nRows, int nCols) {
        double[][] unallocatedCost = new double[nRows][nCols];
        for(int i =0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(result[i][j] == 0){
                    //unallocated cell
                    unallocatedCost[i][j] = costs[i][j] - (uvValues.get(i,0) + uvValues.get(j+nRows,0));

                }
            }

        }
        return unallocatedCost;
    }

    private Matrix calculateUVValues(int[][] result, int[][] costs, int nRows, int nCols) {

        int size = nRows+nCols;

        double[][] lhsArray = new double[size][size];
        double[] cost = new double[size];
        lhsArray[0][0] = 1;
        cost[0]=0;
        int equationNumber = 1;
        for(int i =0;i<nRows;i++){
            for(int j=0;j<nCols;j++){
                if(result[i][j]!=0){
                    //allocated cell
                    //compute u-v
                    lhsArray[equationNumber][nRows+j]=1;
                    lhsArray[equationNumber][i]=1;
                    cost[equationNumber] = costs[i][j];
                    equationNumber++;

                }
            }
        }
        System.out.println("Printing lhs array");
        stream(lhsArray).forEach(a -> System.out.println(Arrays.toString(a)));



        Matrix lhs = new Matrix(lhsArray);
        Matrix rhs = new Matrix(cost, cost.length);
        Matrix ans = lhs.solve(rhs);
        for(int i =0;i<nRows;i++){
            System.out.println("value of u" +(i+1)+" : "+ ans.get(i,0));
        }

        for(int i =nRows;i<nRows+nCols;i++){
            System.out.println("value of v" +(i-nRows+1)+" : "+ ans.get(i,0));
        }

        return ans;


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
