package com.ik.githubbrowser.ui.home.followers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FollowersFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Follower>> followers = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private Repository repository;
    private String username;

    public FollowersFragmentViewModel(Repository repository, String username) {
        this.repository = repository;
        this.username = username;
    }

    public LiveData<List<Follower>> getFollowers() {
        fetchFollowers();
        return followers;
    }

    private void fetchFollowers() {
        FollowerObserver observer = new FollowerObserver();
        repository.getUserFollowers(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        disposable.add(observer);
    }

    public void clear() {
        disposable.clear();
    }

    private class FollowerObserver extends DisposableObserver<List<Follower>>{

        @Override
        public void onNext(List<Follower> fetchedFollowers) {
            followers.setValue(fetchedFollowers);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
