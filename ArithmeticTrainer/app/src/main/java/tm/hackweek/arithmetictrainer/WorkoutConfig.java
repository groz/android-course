package tm.hackweek.arithmetictrainer;

import java.io.Serializable;

public class WorkoutConfig implements Serializable {
    private boolean mWithFractions;
    private int mOperations;
    private int mNumberOfAnswers;
    private int mNumRange;

    public WorkoutConfig(boolean withFractions, int operations, int numberOfAnswers, int numRange) {
        mWithFractions = withFractions;
        mOperations = operations;
        mNumberOfAnswers = numberOfAnswers;
        mNumRange = numRange;
    }

    public int getNumRange() {
        return mNumRange;
    }

    public boolean isWithFractions() {
        return mWithFractions;
    }

    public int getOperations() {
        return mOperations;
    }

    public int getNumberOfAnswers() {
        return mNumberOfAnswers;
    }

    public void setWithFractions(boolean withFractions) {
        mWithFractions = withFractions;
    }

    public void setOperations(int operations) {
        mOperations = operations;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        mNumberOfAnswers = numberOfAnswers;
    }

    public void setNumRange(int numRange) {
        mNumRange = numRange;
    }

    @Override
    public String toString() {
        return String.format("%s-%s-%s-%s",
                mWithFractions,
                mOperations,
                mNumberOfAnswers,
                mNumRange
        );
    }

    public static final WorkoutConfig EASY = new WorkoutConfig(
            false,
            OpCode.MULTIPLY | OpCode.ADD | OpCode.SUBTRACT,
            4,
            15);

    public static final WorkoutConfig MEDIUM = new WorkoutConfig(
            false,
            OpCode.MULTIPLY | OpCode.ADD | OpCode.SUBTRACT,
            8,
            29);

    public static final WorkoutConfig HARD = new WorkoutConfig(
            true,
            OpCode.ALL,
            12,
            99);
}
