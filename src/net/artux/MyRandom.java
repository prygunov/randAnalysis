package net.artux;

public class MyRandom {

    private int a = 16807;
    private int m = 2147483647;
    private int q = 127773;
    private int r = 2836;
    private int seed;

    public MyRandom(int seed) {
        if (seed <= 0 || seed == Integer.MAX_VALUE)
            throw new RuntimeException("Bad seed");
        this.seed = seed;
    }

    public int next() {
        int hi = seed / q;
        int lo = seed % q;
        seed = (a * lo) - (r * hi);
        if (seed <= 0)
            seed = seed + m;
        return (int) (((seed * 1.0) / m)* 100);
    }
}
