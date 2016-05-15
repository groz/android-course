package tm.hackweek.arithmetictrainer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FlowLayout mAnswersContainer;
    TextView mProblemText;
    TextView mTimerText;
    TextView mStatsText;

    // TODO: save state for the items below to get around rotations (Singleton?)
    ProblemGenerator mProblemGenerator;
    int mTimeLeftMillis = 30000;
    int mCorrect;
    int mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        mTimerText = (TextView) findViewById(R.id.timer_text);
        mStatsText = (TextView) findViewById(R.id.stats_text);
        mProblemText = (TextView) findViewById(R.id.problem_text);
        mAnswersContainer = (FlowLayout) findViewById(R.id.answers_container);

        final NumberGenerator numGen = new RandomNumberGenerator(10, new Number(0), new Number(100),
                false);
        final int nAnswersCount = 8;

        mProblemGenerator = new CompositeProblemGenerator(new ArrayList<ProblemGenerator>(){{
            add(new BinaryOpProblemGenerator(
                    numGen,
                    nAnswersCount,
                    new BinaryOperation() {
                        @Override
                        public Number execute(Number lhs, Number rhs) {
                            return lhs.add(rhs);
                        }

                        @Override
                        public String text() {
                            return "+";
                        }
                    }
            ));

            add(new BinaryOpProblemGenerator(
                    numGen,
                    nAnswersCount,
                    new BinaryOperation() {
                        @Override
                        public Number execute(Number lhs, Number rhs) {
                            return lhs.multiply(rhs);
                        }

                        @Override
                        public String text() {
                            return "*";
                        }
                    }
            ));

            add(new BinaryOpProblemGenerator(
                    numGen,
                    nAnswersCount,
                    new BinaryOperation() {
                        @Override
                        public Number execute(Number lhs, Number rhs) {
                            return lhs.subtract(rhs);
                        }

                        @Override
                        public String text() {
                            return "-";
                        }
                    }
            ));

            add(new BinaryOpProblemGenerator(
                    numGen,
                    nAnswersCount,
                    new BinaryOperation() {
                        @Override
                        public Number execute(Number lhs, Number rhs) {
                            return lhs.divide(rhs);
                        }

                        @Override
                        public String text() {
                            return "/";
                        }
                    }
            ));
        }}, 14);

        new CountDownTimer(mTimeLeftMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTimerText.setText("done!");
            }
        }.start();

        renderProblem(mProblemGenerator.next());
    }

    private void renderProblem(Problem problem) {
        mProblemText.setText(problem.getProblemText());
        mAnswersContainer.removeAllViews();

        ++mTotal;
        List<Number> answers = problem.getAnswers();

        for (int i = 0; i < answers.size(); ++i) {
            renderAnswer(answers.get(i).toString(), i, problem.getCorrectIndex());
        }

        mStatsText.setText(String.format("Stats: %s / %s", mCorrect, mTotal));
    }

    private void renderAnswer(String answer, final int answerIndex, final int correctIndex) {
        final Button answerButton = new Button(this);
        answerButton.setText(answer);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: check isEditable? flag to get around errors caused by multiple fast clicks

                if (answerIndex == correctIndex) {
                    ++mCorrect;
                    answerButton.setBackgroundColor(Color.GREEN);
                } else {
                    answerButton.setBackgroundColor(Color.RED);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        renderProblem(mProblemGenerator.next());
                    }
                }, 100);
            }
        });

        mAnswersContainer.addView(answerButton);
    }
}
