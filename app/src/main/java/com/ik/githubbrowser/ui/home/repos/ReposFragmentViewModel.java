package com.ik.githubbrowser.ui.home.repos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class ReposFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
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

    public LiveData<List<Repo>> getUserRepos() {
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

    private class ReposObserver extends DisposableObserver<List<Repo>> {

        @Override
        public void onNext(List<Repo> fetchedRepos) {
            repos.setValue(fetchedRepos);
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e.getLocalizedMessage());
            messages.setValue(e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {

        }
    }
}
