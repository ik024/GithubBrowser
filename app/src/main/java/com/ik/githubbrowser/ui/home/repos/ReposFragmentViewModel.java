package com.ik.githubbrowser.ui.home.repos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.RepoItem;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class ReposFragmentViewModel extends ViewModel {

    private MutableLiveData<List<RepoItem>> repos = new MutableLiveData<>();
    private MutableLiveData<String> messages = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    private Repository repository;
    private String username;

    public ReposFragmentViewModel(Repository repository, String username) {
        this.repository = repository;
        this.username = username;
    }

    public LiveData<String> observerMessages() {
        return messages;
    }

    public LiveData<List<RepoItem>> getUserRepos() {
        if (repos.getValue() == null || repos.getValue().size() == 0)
            fetchUserRepos();
        return repos;
    }

    private void fetchUserRepos() {
        ReposObserver observer = new ReposObserver();
        repository.getUserRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        disposable.add(observer);
    }

    public void clear() {
        disposable.clear();
    }

    private class ReposObserver extends DisposableObserver<List<RepoItem>> {

        @Override
        public void onNext(List<RepoItem> fetchedRepos) {
            repos.setValue(fetchedRepos);
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e.getLocalizedMessage());
            messages.setValue(e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            messages.setValue("");
        }
    }
}
