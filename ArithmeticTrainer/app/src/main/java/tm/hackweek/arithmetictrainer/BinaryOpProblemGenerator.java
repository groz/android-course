package tm.hackweek.arithmetictrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryOpProblemGenerator extends ProblemGenerator {
    private final NumberGenerator mNumberGenerator;
    private final int mAnswersCount;
    private final BinaryOperation mBinaryOperation;

    public BinaryOpProblemGenerator(NumberGenerator numberGenerator, int answersCount,
                                    BinaryOperation binaryOperation) {
        mNumberGenerator = numberGenerator;
        mAnswersCount = answersCount;
        mBinaryOperation = binaryOperation;
    }

    @Override
    public Problem next() {
        Number lhs = mNumberGenerator.generate();
        Number rhs = mNumberGenerator.generate();
        Number correctResult = mBinaryOperation.execute(lhs, rhs);

        List<Number> answers = new ArrayList<>();
        answers.add(correctResult);

        for (int i = 1; i < mAnswersCount; ++i) {
            Number result;

            do {
                result = mNumberGenerator.generate();
            } while (answers.contains(result));

            answers.add(result);
        }
        Collections.shuffle(answers);

        String problemText = String.format("%s %s %s", lhs, mBinaryOperation.text(), rhs);
        return new Problem(problemText, answers, answers.indexOf(correctResult));
    }
}
