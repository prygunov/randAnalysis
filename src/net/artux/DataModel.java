package net.artux;

import org.jfree.data.xy.XYDataItem;

import java.util.Arrays;
import java.util.Random;

public class DataModel {

    private int value[] = new int[100];
    private Random random = new Random();
    private MyRandom myRandom = new MyRandom(System.currentTimeMillis());

    private double lastAverage;
    private double lastDispersion;

    int[] getValues(boolean internalRandom, int n){
        Arrays.fill(value, 0);

        if (internalRandom)
            for(int i = 1; i < n; i++)
                value[random.nextInt(100)] += 1;
        else
            for(int i = 1; i < n; i++)
                value[myRandom.nextInt(100)] += 1;

        double average = 0d;
        double d = 0d;
        for (int i = 0; i < value.length; i++) {
            float p = value[i] / (float)n;
            average += i * p; //мат ожидание


        }

        for (int i = 0; i < value.length; i++) {
            float p = value[i] / (float)n;
            d += Math.pow(i - average, 2) * p; //считаем дисперсию
        }

        lastAverage = average;
        lastDispersion = d;

        return value;
    }

    public double getLastAverage() {
        return lastAverage;
    }

    public double getLastDispersion() {
        return lastDispersion;
    }

    public double getLastDeviation(){
        return Math.sqrt(lastDispersion);
    }
}
