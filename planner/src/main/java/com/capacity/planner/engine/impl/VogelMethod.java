package com.capacity.planner.engine.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.Arrays.stream;

/**
 * Created by ankush.a on 16/03/17.
 */
public class VogelMethod {
    static ExecutorService es = Executors.newFixedThreadPool(2);

    public int[][] executeVogelMethod(int[] demand, int[] supply, int[][] costs) throws Exception{
        final int nRows = supply.length;
        final int nCols = demand.length;

        boolean[] rowDone = new boolean[nRows];
        boolean[] colDone = new boolean[nCols];
        int[][] result = new int[nRows][nCols];

        int supplyLeft = stream(supply).sum();
        int totalCost = 0;

        while (supplyLeft > 0) {
            int[] cell = nextCell(nRows,nCols,rowDone,colDone,costs);
            int r = cell[0];
            int c = cell[1];

            int quantity = Math.min(demand[c], supply[r]);
            demand[c] -= quantity;
            if (demand[c] == 0)
                colDone[c] = true;

            supply[r] -= quantity;
            if (supply[r] == 0)
                rowDone[r] = true;

            result[r][c] = quantity;
            supplyLeft -= quantity;

            totalCost += quantity * costs[r][c];
        }
        stream(result).forEach(a -> System.out.println(Arrays.toString(a)));
        System.out.println("Total cost: " + totalCost);

        es.shutdown();
        return result;
    }

    int[] nextCell(int nRows,int nCols,boolean[] rowDone, boolean[] colDone, int[][] costs) throws Exception {
        Future<int[]> f1 = es.submit(() -> maxPenalty(nRows, nCols, true,rowDone,colDone,costs));
        Future<int[]> f2 = es.submit(() -> maxPenalty(nCols, nRows, false,rowDone,colDone,costs));

        int[] res1 = f1.get();
        int[] res2 = f2.get();

        if (res1[3] == res2[3])
            return res1[2] < res2[2] ? res1 : res2;

        return (res1[3] > res2[3]) ? res2 : res1;
    }

    int[] diff(int j, int len, boolean isRow, boolean[] rowDone, boolean[] colDone, int[][] costs) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        int minP = -1;
        for (int i = 0; i < len; i++) {
            if (isRow ? colDone[i] : rowDone[i])
                continue;
            int c = isRow ? costs[j][i] : costs[i][j];
            if (c < min1) {
                min2 = min1;
                min1 = c;
                minP = i;
            } else if (c < min2)
                min2 = c;
        }
        return new int[]{min2 - min1, min1, minP};
    }

    int[] maxPenalty(int len1, int len2, boolean isRow, boolean[] rowDone, boolean[] colDone, int[][] costs) {
        int md = Integer.MIN_VALUE;
        int pc = -1, pm = -1, mc = -1;
        for (int i = 0; i < len1; i++) {
            if (isRow ? rowDone[i] : colDone[i])
                continue;
            int[] res = diff(i, len2, isRow, rowDone, colDone,costs);
            if (res[0] > md) {
                md = res[0];  // max diff
                pm = i;       // pos of max diff
                mc = res[1];  // min cost
                pc = res[2];  // pos of min cost
            }
        }
        return isRow ? new int[]{pm, pc, mc, md} : new int[]{pc, pm, mc, md};
    }
}
