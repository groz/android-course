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
        rng = seed == 0 ? new Random() : new Random(seed);
    }

    @Override
    public Number generate() {
        // get bounds to common denominator
        // generate random number between nominators
        // return nom/denom
        int den = mMin.getDenominator() * mMax.getDenominator();
        int minNum = mMin.getNumerator() * mMax.getDenominator();
        int maxNum = mMax.getNumerator() * mMin.getDenominator();
        int num = Utils.randInt(rng, minNum, maxNum);
        return new Number(num, den).simplify();
    }
}
