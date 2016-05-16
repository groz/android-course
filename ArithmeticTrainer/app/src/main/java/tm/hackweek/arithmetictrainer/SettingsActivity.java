package tm.hackweek.arithmetictrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

public class SettingsActivity extends AppCompatActivity {
    private final static String TAG = "SettingsActivity";

    private CheckBox mFractionsCheckbox;
    private CheckBox mAdditionCheckbox;
    private CheckBox mMultiplicationCheckbox;
    private CheckBox mSubtractionCheckbox;
    private CheckBox mDivisionCheckbox;
    private NumberPicker mNumberRangePicker;
    private NumberPicker mAnswersCountPicker;
    private Button mEasyButton;
    private Button mMediumButton;
    private Button mHardButton;
    private WorkoutConfig mWorkoutConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();
    }

    private void initUI() {
        Log.d(TAG, "initUI");

        mFractionsCheckbox = (CheckBox)findViewById(R.id.fractions_checkbox);

        mAdditionCheckbox = (CheckBox)findViewById(R.id.addition_checkbox);
        mMultiplicationCheckbox = (CheckBox)findViewById(R.id.multiply_checkbox);
        mDivisionCheckbox = (CheckBox)findViewById(R.id.division_checkbox);
        mSubtractionCheckbox = (CheckBox)findViewById(R.id.subtract_checkbox);

        mNumberRangePicker = (NumberPicker)findViewById(R.id.number_range);
        mAnswersCountPicker = (NumberPicker)findViewById(R.id.answers_number);

        mEasyButton = (Button)findViewById(R.id.easy_settings_button);
        mMediumButton = (Button)findViewById(R.id.medium_settings_button);
        mHardButton = (Button)findViewById(R.id.hard_settings_button);

        mWorkoutConfig = Global.Instance.getWorkoutConfig();

        renderConfig(mWorkoutConfig);

        mFractionsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mWorkoutConfig.setWithFractions(b);
            }
        });

        mAdditionCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int code = OpCode.reset(mWorkoutConfig.getOperations(), OpCode.ADD, b);
                mWorkoutConfig.setOperations(code);
            }
        });

        mMultiplicationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int code = OpCode.reset(mWorkoutConfig.getOperations(), OpCode.MULTIPLY, b);
                mWorkoutConfig.setOperations(code);
            }
        });

        mSubtractionCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int code = OpCode.reset(mWorkoutConfig.getOperations(), OpCode.SUBTRACT, b);
                mWorkoutConfig.setOperations(code);
            }
        });

        mDivisionCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int code = OpCode.reset(mWorkoutConfig.getOperations(), OpCode.DIVIDE, b);
                mWorkoutConfig.setOperations(code);
            }
        });

        mNumberRangePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mWorkoutConfig.setNumRange(newVal);
            }
        });

        mAnswersCountPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mWorkoutConfig.setNumberOfAnswers(newVal);
            }
        });

        mEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkoutConfig = WorkoutConfig.EASY;
                Global.Instance.setWorkoutConfig(mWorkoutConfig);
                renderConfig(mWorkoutConfig);
            }
        });

        mMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkoutConfig = WorkoutConfig.MEDIUM;
                Global.Instance.setWorkoutConfig(mWorkoutConfig);
                renderConfig(mWorkoutConfig);
            }
        });

        mHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkoutConfig = WorkoutConfig.HARD;
                Global.Instance.setWorkoutConfig(mWorkoutConfig);
                renderConfig(mWorkoutConfig);
            }
        });
    }

    private void renderConfig(WorkoutConfig workoutConfig) {
        Log.d(TAG, "renderConfig");

        mFractionsCheckbox.setChecked(workoutConfig.isWithFractions());
        mAdditionCheckbox.setChecked((workoutConfig.getOperations() & OpCode.ADD) != 0);
        mDivisionCheckbox.setChecked((workoutConfig.getOperations() & OpCode.DIVIDE) != 0);
        mSubtractionCheckbox.setChecked((workoutConfig.getOperations() & OpCode.SUBTRACT) != 0);
        mMultiplicationCheckbox.setChecked((workoutConfig.getOperations() & OpCode.MULTIPLY) != 0);

        mNumberRangePicker.setMinValue(2);
        mNumberRangePicker.setMaxValue(999);
        mNumberRangePicker.setValue(workoutConfig.getNumRange());

        mAnswersCountPicker.setMinValue(2);
        mAnswersCountPicker.setMaxValue(16);
        mAnswersCountPicker.setValue(workoutConfig.getNumberOfAnswers());
    }

}
