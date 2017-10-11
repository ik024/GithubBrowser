package com.ik.githubbrowser.ui.search_user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.Repository;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserActivityTest {

    @Rule
    public ActivityTestRule<SearchUserActivity> activityTestRule = new ActivityTestRule<SearchUserActivity>(SearchUserActivity.class);

    private static SearchUserViewModel viewModel;
    private static SearchViewModelFactory factory;
    private static MutableLiveData<Boolean> userExists = new MutableLiveData<>();
    private static final String USER_NAME = "ik024";


    @BeforeClass
    public static void beforeClass(){
        Repository repository = mock(Repository.class);
        factory = new SearchViewModelFactory(repository);
        viewModel = mock(SearchUserViewModel.class);
        when(viewModel.doesUserExists(USER_NAME)).thenReturn(userExists);
        factory.setViewModel(viewModel);
    }

    @Before
    public void setup(){
    }

    @Test
    public void isCorrectLayoutDisplayed() {
        onView(ViewMatchers.withId(R.id.et_username)).check(matches(isDisplayed()));
    }

    @Test
    public void check_invalidUserErrorMsgShown_onInvalidInput() {
        onView(withId(R.id.et_username)).perform(typeText(USER_NAME));
        onView(withId(R.id.bt_login)).perform(click());
        userExists.postValue(false);
        onView(withId(R.id.tv_error)).check(matches(isDisplayed()));
    }

    @Test
    public void check_noInvalidUserErrorMsgShown_onValidInput() {
        onView(withId(R.id.et_username)).perform(typeText(USER_NAME));
        onView(withId(R.id.bt_login)).perform(click());
        userExists.postValue(true);
        onView(withId(R.id.tv_error)).check(matches(not(isDisplayed())));
    }


    @AfterClass
    public void tearDown() {
        viewModel = null;
        factory.setViewModel(viewModel);
    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }
}