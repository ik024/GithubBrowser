package com.ik.githubbrowser.ui.search_user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.UserInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchUserViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<Boolean> userExists = new MutableLiveData<>();
    private CompositeDisposable disposable;

    public SearchUserViewModel(Repository repository) {
        this.repository = repository;
        this.disposable = new CompositeDisposable();
    }

    LiveData<Boolean> doesUserExists(String userName) {
        UserExistsObservable subscriber = new UserExistsObservable();
        getUserInfo(userName).subscribeWith(subscriber);
        disposable.add(subscriber);
        return userExists;
    }

    Observable<Boolean> getUserInfo(String userName) {
        return repository.getUserInfo(userName)
                .map(userInfo -> userInfo != null && userInfo.getName() != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    void clear() {
        disposable.clear();
    }

    class UserExistsObservable extends DisposableObserver<Boolean> {

        @Override
        public void onNext(@NonNull Boolean aBoolean) {
            userExists.setValue(aBoolean);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            userExists.setValue(false);
        }

        @Override
        public void onComplete() {

        }
    }

}
