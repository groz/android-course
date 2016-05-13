package tm.hackweek.arithmetictrainer;

import java.util.ArrayList;

public class ProblemModel {
    final String mProblemText;
    final ArrayList<String> mAnswers;

    public ProblemModel(String problemText, ArrayList<String> answers) {
        mProblemText = problemText;
        mAnswers = answers;
    }

    public String getProblemText() {
        return mProblemText;
    }

    public ArrayList<String> getAnswers() {
        return mAnswers;
    }
}
