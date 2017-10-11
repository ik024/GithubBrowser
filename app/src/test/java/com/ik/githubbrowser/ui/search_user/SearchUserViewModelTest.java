package com.ik.githubbrowser.ui.search_user;

import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.db.entity.UserInfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserViewModelTest {

    private RepositoryImpl repository;
    private SearchUserViewModel viewModel;

    @BeforeClass
    public static void before(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }


    @Before
    public void setup(){

        MockitoAnnotations.initMocks(this);
        repository = mock(RepositoryImpl.class);
        viewModel = new SearchUserViewModel(repository);
    }

    @Test
    public void getUserInfo_returns_true(){
        UserInfo userInfo = new UserInfo();
        userInfo.setName("");
        when(repository.getUserInfo(anyString())).thenReturn(Observable.just(userInfo));
        TestObserver<Boolean> testObserver = new TestObserver<>();
        viewModel.getUserInfo(anyString()).subscribe(testObserver);
        testObserver.assertValue(true);
    }

    @Test
    public void getUserInfo_returns_false(){
        UserInfo userInfo = new UserInfo();
        when(repository.getUserInfo(anyString())).thenReturn(Observable.just(userInfo));
        TestObserver<Boolean> testObserver = new TestObserver<>();
        viewModel.getUserInfo(anyString()).subscribe(testObserver);
        testObserver.assertValue(false);
    }

    @After
    public void teardown(){
        viewModel = null;
    }

    @AfterClass
    public static void after(){
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

}
