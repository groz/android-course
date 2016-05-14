package tm.hackweek.arithmetictrainer;

import java.util.ArrayList;

public class DummyProblemGenerator extends ProblemGenerator {
    int i = 0;

    @Override
    public Problem next() {
        return new Problem("2 x " + i++, new ArrayList<Number>() {{
            add(new Number(0));
            add(new Number(1));
            add(new Number(2));
            add(new Number(3));
            add(new Number(4));
            add(new Number(5));
        }}, 4);
    }
}
