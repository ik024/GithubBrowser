package com.ik.githubbrowser.ui.search_user;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.ik.githubbrowser.repository.Repository;


public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private Repository repository;
    private static SearchUserViewModel viewModel;

    public SearchViewModelFactory(@NonNull Repository repository)
    {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass)
    {
        if (viewModel == null)
            viewModel = new SearchUserViewModel(repository);

        return (T) viewModel;
    }

    @VisibleForTesting
    public  void setViewModel(ViewModel model) {
        viewModel = (SearchUserViewModel) model;
    }


}
