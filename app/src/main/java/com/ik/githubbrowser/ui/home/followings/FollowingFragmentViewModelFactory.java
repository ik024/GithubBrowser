package com.ik.githubbrowser.ui.home.followings;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ik.githubbrowser.repository.Repository;


public class FollowingFragmentViewModelFactory implements ViewModelProvider.Factory {

    private Repository mRepository;
    private String mUsername;
    private static FollowingsFragmentViewModel mViewModel;

    public FollowingFragmentViewModelFactory(Repository repository, String username) {
        mRepository = repository;
        mUsername = username;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (mViewModel == null) {
            mViewModel = new FollowingsFragmentViewModel(mRepository, mUsername);
        }
        return (T) mViewModel;
    }
}
