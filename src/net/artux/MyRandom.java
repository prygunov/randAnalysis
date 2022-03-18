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
        seed = (a * seed + b) % c;
        return seed / (double) c;
        //[0, 1)
    }

    public int getRand(int n) {
        //[0,n-1]
        double next = nextDouble();
        return (int) (next * n);
    }
}
