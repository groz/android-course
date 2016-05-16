package tm.hackweek.arithmetictrainer;

import java.util.Random;

public class RandomNumberGenerator implements NumberGenerator {
    private final Random rng;
    private final Number mMin;
    private final Number mMax;
    private final boolean mFractional;

    public RandomNumberGenerator(int seed, Number min, Number max, boolean fractional) {
        mMin = min;
        mMax = max;
        mFractional = fractional;
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
        int num = Utils.randomInRange(rng, minNum, maxNum);

        if (mFractional) {
            return new Number(num, den).simplify();
        } else {
            double res = num / (double) den;
            return new Number((int) res);
        }

    }

    @Override
    public boolean isFractional() {
        return mFractional;
    }
}
