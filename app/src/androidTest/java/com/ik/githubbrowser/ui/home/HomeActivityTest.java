package com.ik.githubbrowser.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HomeActivityTest {

    @Rule
    ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<HomeActivity>(HomeActivity.class, false, false);

    @Before
    public void setup() {
        Intent intent = new Intent();
        Bundle bundle  = new Bundle();
        bundle.putString(KEY_USERNAME, "ik024");
        intent.putExtras(bundle);
        mActivityRule.launchActivity(intent);
    }



}