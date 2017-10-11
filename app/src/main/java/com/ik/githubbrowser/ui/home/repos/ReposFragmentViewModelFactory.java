package com.ik.githubbrowser.ui.home.repos;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.ik.githubbrowser.repository.Repository;

public class ReposFragmentViewModelFactory  implements ViewModelProvider.Factory {
    private Repository repository;
    private String username;
    private static ReposFragmentViewModel viewModel;

    public ReposFragmentViewModelFactory(@NonNull Repository repository, @NonNull String username) {
        this.repository = repository;
        this.username = username;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null)
            viewModel = new ReposFragmentViewModel(repository, username);

        return (T) viewModel;
    }

    @VisibleForTesting
    public void setViewModel(ViewModel model) {
        viewModel = (ReposFragmentViewModel) model;
    }


}
