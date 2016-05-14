package tm.hackweek.arithmetictrainer;

import java.util.ArrayList;
import java.util.List;

public class Problem {
    private final String mProblemText;
    private final List<Number> mAnswers;
    private final int mCorrectIndex;

    public Problem(String problemText, List<Number> answers, int correctIndex) {
        mProblemText = problemText;
        mAnswers = answers;
        mCorrectIndex = correctIndex;
    }

    public String getProblemText() {
        return mProblemText;
    }

    public List<Number> getAnswers() {
        return mAnswers;
    }

    public int getCorrectIndex() {
        return mCorrectIndex;
    }
}
