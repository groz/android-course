package com.bignerdranch.android.geoquiz;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityTest {

    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule(QuizActivity.class);

    private ViewInteraction questionView, trueButton, falseButton, nextButton, prevButton,
            cheatButton;

    private ViewInteraction cheatText;

    @Before
    public void setUp() {
        questionView = onView(withId(R.id.question_text_view));
        trueButton = onView(withId(R.id.true_button));
        falseButton = onView(withId(R.id.false_button));
        nextButton = onView(withId(R.id.next_button));
        prevButton = onView(withId(R.id.prev_button));
        cheatButton = onView(withId(R.id.cheat_button));
        cheatText = onView(withId(R.id.cheat_text_view));
    }

    @Test
    public void formIsShown() {
        questionView.check(matches(isDisplayed()));
        trueButton.check(matches(isDisplayed()));
        falseButton.check(matches(isDisplayed()));
        nextButton.check(matches(isDisplayed()));
        prevButton.check(matches(isDisplayed()));
        cheatButton.check(matches(isDisplayed()));
        questionView.check(matches(withText(R.string.question_first)));
    }

    @Test
    public void questionChangedOnNextClick() {
        nextButton.perform(click());
        questionView.check(matches(withText(R.string.question_oceans)));
    }

    @Test
    public void questionChangedOnTextViewClick() {
        questionView.perform(click());
        questionView.check(matches(withText(R.string.question_oceans)));
    }

    @Test
    public void prevQuestionShownOnPrevClick() {
        prevButton.perform(click());
        questionView.check(matches(withText(R.string.question_asia)));
    }

    @Test
    public void questionIndexSavedOnRotate() {
        nextButton.perform(click());
        questionView.check(matches(withText(R.string.question_oceans)));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        questionView.check(matches(withText(R.string.question_oceans)));

        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        questionView.check(matches(withText(R.string.question_oceans)));
    }

    @Test
    public void cheatButtonShowsCheatActivity() {
        cheatButton.perform(click());
        cheatText.check(matches(isDisplayed()));
    }
}
