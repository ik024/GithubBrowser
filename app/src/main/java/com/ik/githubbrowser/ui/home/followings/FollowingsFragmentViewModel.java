package com.ik.githubbrowser.ui.home.followings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.followings.Following;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FollowingsFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Following>> mFollowings = new MutableLiveData<List<Following>>();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private final Repository mRepository;
    private final String mUsername;

    public FollowingsFragmentViewModel(Repository repository, String username) {

        mRepository = repository;
        mUsername = username;

    }

    public LiveData<List<Following>> getFollowings() {
        fetchingFollowings();
        return mFollowings;
    }

    private void fetchingFollowings() {
        FollowingObserver observer = new FollowingObserver();
        mRepository.getUserFollowings(mUsername)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        mDisposable.add(observer);
    }

    public void clear () {
        mDisposable.clear();
    }

    private class FollowingObserver extends DisposableObserver<List<Following>> {

        @Override
        public void onNext(List<Following> fetchedFollowings) {
            mFollowings.setValue(fetchedFollowings);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
