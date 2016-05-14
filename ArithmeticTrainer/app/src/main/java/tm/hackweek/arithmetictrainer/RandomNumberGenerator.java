package tm.hackweek.arithmetictrainer;

import java.util.Random;

public class RandomNumberGenerator implements NumberGenerator {
    private final Random rng;
    private final Number mMin;
    private final Number mMax;
    private final boolean mAllowFractions;

    public RandomNumberGenerator(int seed, Number min, Number max, boolean allowFractions) {
        mMin = min;
        mMax = max;
        mAllowFractions = allowFractions;
        rng = new Random(seed);
    }

    @Override
    public Number generate() {
        // TODO: implement this correctly accounting for min, max, allowFractions
        int num = rng.nextInt();
        int den = rng.nextInt();
        return new Number(num, den);
    }
}
