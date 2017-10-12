package com.ik.githubbrowser.ui.home.events;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.ui.search_user.SearchUserViewModel;

public class EventsFragmentViewModelFactory implements ViewModelProvider.Factory {
    private Repository repository;
    private String username;
    private static EventsFragmentViewModel viewModel;

    public EventsFragmentViewModelFactory(@NonNull Repository repository, @NonNull String username) {
        this.repository = repository;
        this.username = username;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null)
            viewModel = new EventsFragmentViewModel(repository, username);

        return (T) viewModel;
    }

    @VisibleForTesting
    public void setViewModel(ViewModel model) {
        viewModel = (EventsFragmentViewModel) model;
    }


}
