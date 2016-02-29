package com.bignerdranch.android.geoquiz;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizActivityTest {

    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule(QuizActivity.class);

    @Test
    public void questionWithAnswerOptionsAreShown() {
        onView(withText(R.string.question_text)).check(matches(isDisplayed()));
        onView(withText(R.string.true_button)).check(matches(isDisplayed()));
        onView(withText(R.string.false_button)).check(matches(isDisplayed()));
    }
}