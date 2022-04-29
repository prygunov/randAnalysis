package net.artux;

public class MyRandom {

    public int a = 25173;
    public int c = 65536;
    public int b = 13849;
    public long seed;

    public MyRandom(long seed) {
        this.seed = seed;
    }

    public double nextDouble(){
        seed = (a * seed + b) % c; // РЗ 16 бит
        return seed / (double) c;
        //[0, 1)
    }

    public int nextInt(int bound) {
        //[0,n-1]
        double next = nextDouble();
        return (int)(next * bound);
    }

    public double nextNormal(int n){
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nextDouble();
        }
        sum /=n;
        return sum * 6 - 3;
    }

    public int nextNormal(int n, int m, int sigma){
        return (int)Math.floor(m + sigma * nextNormal(n));
    }
}