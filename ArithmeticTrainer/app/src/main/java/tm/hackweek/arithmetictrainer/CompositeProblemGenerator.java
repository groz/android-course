package tm.hackweek.arithmetictrainer;

import java.util.List;
import java.util.Random;

public class CompositeProblemGenerator extends ProblemGenerator {

    private final List<ProblemGenerator> mGenerators;
    private final int mRngSeed;
    private final Random rng;
    private int mCurrent = 0;

    public CompositeProblemGenerator(List<ProblemGenerator> generators) {
        this(generators, 0);
    }

    public CompositeProblemGenerator(List<ProblemGenerator> generators, int rngSeed) {
        mGenerators = generators;
        mRngSeed = rngSeed;
        rng = new Random(mRngSeed);
    }

    @Override
    public Problem next() {
        mCurrent = mRngSeed == 0
                ? (mCurrent + 1) % mGenerators.size()
                : rng.nextInt(mGenerators.size());

        return mGenerators.get(mCurrent).next();
    }
}
