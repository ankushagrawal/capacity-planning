package com.capacity.planner.engine.impl;

import Jama.Matrix;

import java.util.Arrays;
import java.util.Queue;

import static java.util.Arrays.stream;

/**
 * Created by ankush.a on 16/03/17.
 */
public class ModiMethod {

    public int[][] optimizeAllocations(int[][] result, int[][] costs, int[] demand, int[] supply) {
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
                optimizeAllocations(result,costs,demand,supply);
            }
            else{
                //got an optimal solution
                System.out.println("Optimal Solution achieved....");
                return result;
            }
        }
        return result;
    }

    private int[][] reallocateJobs(double[][] unallocatedMatrix, int[][] result,int[] index,int nRows, int nCols) {
        //paths available are RTLD, RDRU , LTRD, LDLU

        if(index[1] == 0){
            //it is first column, only movements are RIGHT viz. RTLD, RDRU
            int step = 1;

            if(!doRTLD(result,index,step,unallocatedMatrix)){
                doRDRU(result,index,step,unallocatedMatrix);
            }
        }
        else if(index[1] == nCols-1){
            //it is last column, only movements are LEFT viz.  LTRD, LDLU
            int step = 1;
            if(!doLTRD(result, index, step, unallocatedMatrix)){
                doLDLU(result, index, step, unallocatedMatrix);
            }
        }
        else{
            //all possible movements
            int step = 1;

            if(!doRTLD(result,index,step,unallocatedMatrix)){
                if(!doRDRU(result,index,step,unallocatedMatrix)){
                    if(!doLTRD(result, index, step, unallocatedMatrix)){
                        if(!doLDLU(result, index, step, unallocatedMatrix)){
                            //TODO: now we have to chnage step length....
                        }
                    }
                }
            }

        }
        return result;
    }

    private boolean doLDLU(int[][] result, int[] index, int step, double[][] unallocatedMatrix) {
        int[] newIndex = initializeNewIndex(index);
        LoopDirection direction = initializeLoopDirection(index);
        if(checkLeftMovement(step, unallocatedMatrix, newIndex)) {
            newIndex[1] = newIndex[1] - step;
            setDirection(direction, newIndex,1);

            if(checkDownMovement(step, unallocatedMatrix, newIndex)){
                newIndex[0] = newIndex[0] + step;
                setDirection(direction, newIndex,2);
                if(checkLeftMovement(step,unallocatedMatrix,newIndex)){
                    newIndex[1] =  newIndex[1] - step;
                    setDirection(direction, newIndex,3);
                    //found a close loop
                    //path is RTLD with step 1
                    result = redistribute(direction, result);
                    return true;

                }
                else
                    return false;
            }
            else
                return false;
        }
        else{
            return false;
        }
    }

    private boolean doLTRD(int[][] result, int[] index, int step, double[][] unallocatedMatrix) {
        int[] newIndex = initializeNewIndex(index);
        LoopDirection direction = initializeLoopDirection(index);
        if(checkLeftMovement(step, unallocatedMatrix, newIndex)) {
            newIndex[1] = newIndex[1] - step;
            setDirection(direction, newIndex,1);

            if(checkTopMovement(step, unallocatedMatrix, newIndex)){
                newIndex[0] = newIndex[0] - step;
                setDirection(direction, newIndex,2);
                if(checkRightMovement(step,unallocatedMatrix,newIndex)){
                    newIndex[1] =  newIndex[1] + step;
                    setDirection(direction, newIndex,3);
                    //found a close loop
                    //path is RTLD with step 1
                    result = redistribute(direction, result);
                    return true;

                }
                else
                    return false;
            }
            else
                return false;
        }
        else{
            return false;
        }
    }

    private boolean doRDRU(int[][] result, int[] index, int step, double[][] unallocatedMatrix) {
        int[] newIndex = initializeNewIndex(index);
        LoopDirection direction = initializeLoopDirection(index);
        if(checkRightMovement(step, unallocatedMatrix,newIndex)) {
            newIndex[1] = step + newIndex[1];
            setDirection(direction, newIndex,1);

            if(checkDownMovement(step,unallocatedMatrix,newIndex)){
                newIndex[0] = newIndex[0] + step;
                setDirection(direction, newIndex,2);
                if(checkRightMovement(step,unallocatedMatrix,newIndex)){
                    newIndex[1] =  newIndex[1] + step;
                    setDirection(direction, newIndex,3);
                    //found a close loop
                    //path is RTLD with step 1
                    result = redistribute(direction, result);
                    return true;

                }
                else
                    return false;
            }
            else
                return false;
        }
        else{
            return false;
        }
    }

    private boolean doRTLD(int[][] result, int[] index,int step,double[][] unallocatedMatrix) {
        int[] newIndex = initializeNewIndex(index);
        LoopDirection direction = initializeLoopDirection(index);
        if(checkRightMovement(step, unallocatedMatrix,newIndex)) {
            newIndex[1] = step + newIndex[1];
            setDirection(direction, newIndex,1);

            if(checkTopMovement(step,unallocatedMatrix,newIndex)){
                newIndex[0] = newIndex[0] - step;
                setDirection(direction, newIndex,2);
                if(checkLeftMovement(step,unallocatedMatrix,newIndex)){
                    newIndex[1] =  newIndex[1] - step;
                    setDirection(direction, newIndex,3);
                    //found a close loop
                    //path is RTLD with step 1
                    result = redistribute(direction, result);
                    return true;

                }
                else
                    return false;
            }
            else
                return false;
        }
        else{
            return false;
        }
    }

    private int[] initializeNewIndex(int[] index) {
        int[] newIndex = new int[2];
        newIndex[0] = index[0];
        newIndex[1] = index[1];
        return newIndex;
    }

    private LoopDirection initializeLoopDirection(int[] index) {
        LoopDirection direction = new LoopDirection();
        Coordinate coordinate = new Coordinate();
        coordinate.setRowIndex(index[0]);
        coordinate.setColumnIndex(index[1]);
        direction.setStartPosition(coordinate);
        return direction;
    }

    private int[][] redistribute(LoopDirection direction, int[][] result) {
        //get minimumn from 1st and 3rd position
        System.out.println(" loop direction : " + direction);
        if(result[direction.getFirstStop().getRowIndex()][direction.getFirstStop().getColumnIndex()] <
                result[direction.getThirdStop().getRowIndex()][direction.getThirdStop().getColumnIndex()]){
            //1st position has minimum allocation
            int minCapacity = result[direction.getFirstStop().getRowIndex()][direction.getFirstStop().getColumnIndex()];
            result[direction.getFirstStop().getRowIndex()][direction.getFirstStop().getColumnIndex()] = 0;
            result[direction.getThirdStop().getRowIndex()][direction.getThirdStop().getColumnIndex()] =
                    result[direction.getThirdStop().getRowIndex()][direction.getThirdStop().getColumnIndex()] - minCapacity;

            result[direction.getStartPosition().getRowIndex()][direction.getStartPosition().getColumnIndex()] = minCapacity;

            result[direction.getSecondStop().getRowIndex()][direction.getSecondStop().getColumnIndex()] =
                    result[direction.getSecondStop().getRowIndex()][direction.getSecondStop().getColumnIndex()] + minCapacity;


        }
        else{
            int minCapacity = result[direction.getThirdStop().getRowIndex()][direction.getThirdStop().getColumnIndex()];
            result[direction.getThirdStop().getRowIndex()][direction.getThirdStop().getColumnIndex()] = 0;

            result[direction.getFirstStop().getRowIndex()][direction.getFirstStop().getColumnIndex()] =
                    result[direction.getFirstStop().getRowIndex()][direction.getFirstStop().getColumnIndex()] - minCapacity;

            result[direction.getStartPosition().getRowIndex()][direction.getStartPosition().getColumnIndex()] = minCapacity;

            result[direction.getSecondStop().getRowIndex()][direction.getSecondStop().getColumnIndex()] =
                    result[direction.getSecondStop().getRowIndex()][direction.getSecondStop().getColumnIndex()] + minCapacity;

        }
        return result;
    }

    private void setDirection(LoopDirection direction, int[] newIndex, int stop) {
        Coordinate coordinate = new Coordinate();
        coordinate.setRowIndex(newIndex[0]);
        coordinate.setColumnIndex(newIndex[1]);
        if(stop == 1)
            direction.setFirstStop(coordinate);
        else if(stop == 2)
            direction.setSecondStop(coordinate);
        else direction.setThirdStop(coordinate);
    }

    private boolean checkLeftMovement(int step, double[][] unallocatedMatrix, int[] index) {
        try {
            if (unallocatedMatrix[index[0]][index[1] - step] == 0) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    private boolean checkDownMovement(int step, double[][] unallocatedMatrix, int[] index) {
        try {
            if (unallocatedMatrix[index[0]+step][index[1] ] == 0) {
                return true;
            }
        }catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean checkTopMovement(int step, double[][] unallocatedMatrix, int[] index) {
        try {
            if (unallocatedMatrix[index[0]-step][index[1] ] == 0) {
                return true;
            }
        }catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean checkRightMovement(int step, double[][] unallocatedMatrix,int[] index) {
        try {
            if (unallocatedMatrix[index[0]][index[1] + step] == 0) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
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
