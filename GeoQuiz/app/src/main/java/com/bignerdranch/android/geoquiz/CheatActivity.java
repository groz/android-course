package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";

    private static final String CHEATER_STATE = "cheater_state";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private Boolean mIsCheater;
    private TextView mApiLevelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mIsCheater = savedInstanceState != null && savedInstanceState.getBoolean(CHEATER_STATE);
        Log.d(TAG, String.format("Cheater: %b", mIsCheater));
        setAnswerShownResult(mIsCheater);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_cheat_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mApiLevelTextView = (TextView) findViewById(R.id.api_level_view);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mIsCheater = true;
                setAnswerShownResult(true);
            }
        });

        String str = getString(R.string.api_level_text);
        mApiLevelTextView.setText(String.format(str, Build.VERSION.SDK_INT));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CHEATER_STATE, mIsCheater);
    }

    private void setAnswerShownResult(boolean answerShown) {
        Intent i = new Intent();
        i.putExtra(EXTRA_ANSWER_SHOWN, answerShown);
        setResult(RESULT_OK, i);
    }

    public static Intent newIntent(Context context, boolean isAnswerTrue) {
        Intent i = new Intent(context, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result != null && result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

}
