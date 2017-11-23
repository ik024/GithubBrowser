package com.ik.githubbrowser.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

import com.ik.githubbrowser.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<HomeActivity>(HomeActivity.class, false, false);

    Intent intent = new Intent();


    @Before
    public void setup() {
        Bundle bundle  = new Bundle();
        bundle.putString(KEY_USERNAME, "ik024");
        intent.putExtras(bundle);

    }


    @Test
    public void closes_activity_on_invalid_intent(){
        Intent invalidIntent = new Intent();
        Bundle bundle  = new Bundle();
        bundle.putString(KEY_USERNAME, null);
        invalidIntent.putExtras(bundle);
        mActivityRule.launchActivity(invalidIntent);

        Activity activity = mActivityRule.getActivity();
        onView(withText(R.string.invalid_username))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}