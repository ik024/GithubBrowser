package com.ik.githubbrowser.ui.home.events;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.db.entity.events.Event;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EventsFragmentViewModel extends ViewModel{

    private MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private MutableLiveData<String> messages = new MutableLiveData<>();

    private Repository repository;
    private String username;
    private CompositeDisposable disposable = new CompositeDisposable();

    public EventsFragmentViewModel(Repository repository, String username) {

        this.repository = repository;
        this.username = username;
    }

    public LiveData<String> observerMsgs(){
        return messages;
    }

    public LiveData<List<Event>> getEvents() {
        fetchEvents();
        return events;
    }

    private void fetchEvents() {
        EventsObserver observer = new EventsObserver();
        repository.getUserEvents(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        disposable.add(observer);

    }

    public void clear() {
        disposable.clear();
    }

    private class EventsObserver extends DisposableObserver<List<Event>> {
        EventsObserver(){
        }
        @Override
        public void onNext(List<Event> fetchedEvents) {
            events.setValue(fetchedEvents);
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
