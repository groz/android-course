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

        Number a = correctResult.multiply(new Number(1, 2));
        Number b = correctResult.multiply(new Number(3, 2));

        Number min, max;
        if (a.compareTo(b) < 0) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }
        min = min.subtract(new Number(5));
        max = max.add(new Number(5));
        NumberGenerator answerGenerator = new RandomNumberGenerator(
                0, min, max, mNumberGenerator.isFractional());

        for (int i = 1; i < mAnswersCount; ++i) {
            Number result;

            do {
                result = answerGenerator.generate();
            } while (answers.contains(result));

            answers.add(result);
        }
        Collections.shuffle(answers);

        String problemText = String.format("%s %s %s", lhs, mBinaryOperation.text(), rhs);
        return new Problem(problemText, answers, answers.indexOf(correctResult));
    }
}
