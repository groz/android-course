package tm.hackweek.arithmetictrainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FlowLayout mAnswersContainer;
    TextView mProblemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        renderProblem(new ProblemModel("2 x 2", new ArrayList<String>() {{
            add("0");
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
        }}));
    }

    private void setupUI() {
        mProblemText = (TextView) findViewById(R.id.problem_text);
        mAnswersContainer = (FlowLayout) findViewById(R.id.answers_container);
    }

    private void renderProblem(ProblemModel problemModel) {
        mProblemText.setText( problemModel.getProblemText() );
        mAnswersContainer.removeAllViews();

        for (String answer : problemModel.getAnswers()) {
            renderAnswer(answer);
        }
    }

    private void renderAnswer(String answer) {
        Button answerButton = new Button(this);
        answerButton.setText(answer);
        mAnswersContainer.addView(answerButton);
    }
}
