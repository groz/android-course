package tm.hackweek.arithmetictrainer;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorkoutFactory {
    @SuppressLint("UseSparseArrays")
    static HashMap<Integer, BinaryOperation> sOperations = new HashMap<Integer, BinaryOperation>() {{
        put(OpCode.ADD, new BinaryOperation() {
            @Override
            public Number execute(Number lhs, Number rhs) {
                return lhs.add(rhs);
            }

            @Override
            public String text() {
                return "+";
            }
        });

        put(OpCode.MULTIPLY, new BinaryOperation() {
            @Override
            public Number execute(Number lhs, Number rhs) {
                return lhs.multiply(rhs);
            }

            @Override
            public String text() {
                return "x";
            }
        });

        put(OpCode.SUBTRACT, new BinaryOperation() {
            @Override
            public Number execute(Number lhs, Number rhs) {
                return lhs.subtract(rhs);
            }

            @Override
            public String text() {
                return "-";
            }
        });

        put(OpCode.DIVIDE, new BinaryOperation() {
            @Override
            public Number execute(Number lhs, Number rhs) {
                return lhs.divide(rhs);
            }

            @Override
            public String text() {
                return "/";
            }
        });
    }};

    public static ProblemGenerator createWorkout(WorkoutConfig config) {
        int rngSeed = new Random().nextInt();

        final NumberGenerator numGen = new RandomNumberGenerator(
                rngSeed,
                new Number(1),
                new Number(config.getNumRange()),
                config.isWithFractions());

        int nAnswersCount = config.getNumberOfAnswers();
        List<ProblemGenerator> gens = new ArrayList<ProblemGenerator>();

        for (Map.Entry<Integer, BinaryOperation> entry : sOperations.entrySet()) {
            Integer code = entry.getKey();
            if ((code & config.getOperations()) != 0) {
                BinaryOperation op = entry.getValue();
                ProblemGenerator gen = new BinaryOpProblemGenerator(numGen, nAnswersCount, op);
                gens.add(gen);
            }
        }

        return new CompositeProblemGenerator(gens, rngSeed);
    }
}
