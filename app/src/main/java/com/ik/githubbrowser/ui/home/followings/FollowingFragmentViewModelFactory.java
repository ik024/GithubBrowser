package com.ik.githubbrowser.ui.home.followings;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ik.githubbrowser.repository.Repository;


public class FollowingFragmentViewModelFactory implements ViewModelProvider.Factory {

    private Repository mRepository;
    private String mUsername;
    private static FollowingsFragmentViewModel viewModel;

    public FollowingFragmentViewModelFactory(Repository repository, String username) {
        mRepository = repository;
        mUsername = username;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (viewModel == null) {
            viewModel = new FollowingsFragmentViewModel(mRepository, mUsername);
        }
        return (T) viewModel;
    }

    public static void clear(){
        viewModel = null;
    }
}
