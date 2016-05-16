package tm.hackweek.arithmetictrainer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private FlowLayout mAnswersContainer;
    private TextView mProblemText;
    private TextView mTimerText;
    private TextView mStatsText;

    // TODO: save state for the items below to get around rotations (Singleton?)
    private ProblemGenerator mProblemGenerator;
    private int mTimeLeftMillis = 30000;
    private int mCorrect = 0;
    private int mTotal = 0;
    private CountDownTimer mTimer;
    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.settings_menuitem:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        mTimer.cancel();
        isActive = false;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        initUI();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    private void initUI() {
        Log.d(TAG, "initUI");
        mCorrect = 0;
        mTotal = 0;
        isActive = true;

        mTimerText = (TextView) findViewById(R.id.timer_text);
        mStatsText = (TextView) findViewById(R.id.stats_text);
        mProblemText = (TextView) findViewById(R.id.problem_text);
        mAnswersContainer = (FlowLayout) findViewById(R.id.answers_container);

        mTimer = new CountDownTimer(mTimeLeftMillis, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = "" + millisUntilFinished / 1000;
                mTimerText.setText(text);
            }

            public void onFinish() {
                mTimerText.setText(R.string.timer_done);
                isActive = false;
            }
        }.start();

        mProblemGenerator = WorkoutFactory.createWorkout(Global.Instance.getWorkoutConfig());
        renderProblem(mProblemGenerator.next());
    }

    private void renderProblem(Problem problem) {
        Log.d(TAG, "renderProblem");

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
        Log.d(TAG, "renderAnswer");

        final Button answerButton = new Button(this);
        answerButton.setText(answer);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isActive)
                    return;
                isActive = false;

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
                        isActive = true;
                    }
                }, 100);
            }
        });

        mAnswersContainer.addView(answerButton);
    }
}
