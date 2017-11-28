package com.ik.githubbrowser.ui.home.followers;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ik.githubbrowser.repository.Repository;


public class FollowersFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;
    private final String username;
    private static FollowersFragmentViewModel viewModel;

    public FollowersFragmentViewModelFactory (Repository repository, String username) {

        this.repository = repository;
        this.username = username;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null) {
            viewModel  = new FollowersFragmentViewModel(repository, username);
        }
        return (T) viewModel;
    }

    public static void clear(){
        viewModel = null;
    }
}
