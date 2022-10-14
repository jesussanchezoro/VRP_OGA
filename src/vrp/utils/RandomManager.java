package vrp.utils;

import java.util.Random;

public abstract class RandomManager {

    private static Random rnd;

    public static void init(int seed) {

        rnd = new Random(seed);
    }

    public static Random getRnd() {
        return rnd;
    }
}
