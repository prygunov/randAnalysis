package net.artux;

import java.util.Arrays;
import java.util.Random;

public class DataModel {

    private int value[] = new int[100];
    private Random random = new Random();
    private MyRandom myRandom = new MyRandom(System.currentTimeMillis());

    private double lastAverage;
    private double lastDispersion;
    private String interval;

    int[] getValues(boolean internalRandom, int n) {
        Arrays.fill(value, 0);

        if (internalRandom)
            for (int i = 1; i < n; i++)
                value[random.nextInt(100)] += 1;
        else
            for (int i = 1; i < n; i++)
                value[myRandom.nextInt(100)] += 1;

        updateValues(n);

        return value;
    }

    int[] getNormalValues(int n, int rn, int m, int sigma) {
        Arrays.fill(value, 0);

        for (int i = 1; i < n; i++) {
            int v = myRandom.nextNormal(rn, m, sigma);
            if (v >= 0 && v < 100)
                value[v] += 1;
        }
        updateValues(n);


        return value;
    }

    public MyRandom getMyRandom() {
        return myRandom;
    }

    public void updateValues(int n){
        int min = 99;
        int max = 0;

        for (int i = 0; i < 100; i++) {
            if (value[i] != 0 && i < min)
                min = i;
            if (value[i] != 0 && i > max)
                max = i;
        }

        interval = "(" + min +", " + max+")";

        double average = 0d;
        double d = 0d;
        for (int i = 0; i < value.length; i++) {
            float p = value[i] / (float) n;
            average += i * p; //мат ожидание
        }

        for (int i = 0; i < value.length; i++) {
            float p = value[i] / (float) n;
            d += Math.pow(i - average, 2) * p; //считаем дисперсию
        }

        lastAverage = average;
        lastDispersion = d;
    }

    public double getNextDouble() {
        return myRandom.nextDouble();
    }

    public double getLastAverage() {
        return lastAverage;
    }

    public double getLastDispersion() {
        return lastDispersion;
    }

    public double getLastDeviation() {
        return Math.sqrt(lastDispersion);
    }

    public String getInterval() {
        return interval;
    }
}
