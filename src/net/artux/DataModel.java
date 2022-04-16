package net.artux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataModel {

    private HashMap<Integer, Integer> values = new HashMap<>();
    private Random random = new Random();
    private MyRandom myRandom = new MyRandom(System.currentTimeMillis());

    private float lastAverage;
    private float lastDispersion;

    HashMap<Integer, Integer> getValues(int type, int n, int rn, int m, int sigma) {
        values = new HashMap<>();

        if (type == 0)
            for (int i = 1; i < n; i++) {
                int v = random.nextInt(100);
                if (values.containsKey(v))
                    values.put(v, values.get(v) + 1);
                else
                    values.put(v, 0);
            }
        else if (type == 1)
            for (int i = 1; i < n; i++) {
                int v = random.nextInt(100);
                if (values.containsKey(v))
                    values.put(v, values.get(v) + 1);
                else
                    values.put(v, 0);
            }
        else{
            for (int i = 1; i < n; i++) {
                int v = myRandom.nextNormal(rn, m, sigma);

                if (values.containsKey(v))
                    values.put(v, values.get(v) + 1);
                else
                    values.put(v, 0);
            }
        }
        updateValues(n);

        return values;
    }

    public MyRandom getMyRandom() {
        return myRandom;
    }

    public void updateValues(int n){
        lastAverage = 0; lastDispersion = 0;
        for (Map.Entry<Integer, Integer> i : values.entrySet())
        {
            lastAverage += (float)i.getKey() * i.getValue();
        }
        lastAverage /= n;
        for (Map.Entry<Integer, Integer> i : values.entrySet())
        {
            float underSqr = (float)i.getKey() - lastAverage;
            lastDispersion += underSqr * underSqr * i.getValue();
        }
        lastDispersion /= n;
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
}
