package com.ik.githubbrowser;

import android.app.Application;

import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class MyApplication extends Application {

    private NetworkInstance mNetworkInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        mNetworkInstance = NetworkInstance.getInstance(this);
    }

    public ApiService getApiService() {
        return mNetworkInstance.getApiService();
    }

    public Picasso getPicasso() {
        return mNetworkInstance.getPicasso();
    }
}
