package com.ik.githubbrowser.repository;


import com.ik.githubbrowser.repository.db.entity.UserInfo;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.db.entity.repos.Repo;
import com.ik.githubbrowser.repository.network.ApiService;

import java.util.List;

import io.reactivex.Observable;


public class RepositoryImpl implements Repository{

    private ApiService apiService;

    public RepositoryImpl(ApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public Observable<UserInfo> getUserInfo(String userName) {
        //TODO: check in local db
        return apiService.getUserInfo(userName);

    }

    @Override
    public Observable<List<Event>> getUserEvents(String userName) {
        //TODO: check in local db
        return apiService.getUserEvents(userName);
    }

    @Override
    public Observable<List<Repo>> getUserRepos(String userName) {
        //TODO: check in local db
        return apiService.getUserRepos(userName);
    }


}
