package tm.hackweek.arithmetictrainer;

import java.util.Random;

public final class Utils {
    public static int randInt(Random rng, int min, int max) {
        return rng.nextInt(max - min) + min;
    }
}
