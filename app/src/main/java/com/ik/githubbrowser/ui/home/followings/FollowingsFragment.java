package com.ik.githubbrowser.ui.home.followings;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ik.githubbrowser.BaseActivity;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;

public class FollowingsFragment extends Fragment implements LifecycleOwner{

    @BindView(R.id.rv_followings)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private FragmentInteraction mListener;
    private BaseActivity mActivity;
    private FollowingsFragmentViewModel mViewModel;
    private FollowingItemAdapter mAdapter;
    private String mUsername;

    public FollowingsFragment() {
        // Required empty public constructor
    }

    public static FollowingsFragment newInstance(String username) {
        FollowingsFragment fragment = new FollowingsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(KEY_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followings, container, false);
        ButterKnife.bind(this, view);

        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mActivity  = (BaseActivity) getActivity();

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        FollowingFragmentViewModelFactory factory = new FollowingFragmentViewModelFactory(repository, mUsername);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(FollowingsFragmentViewModel.class);

        mViewModel.getFollowings().observe(this, followings -> {
            mProgressBar.setVisibility(View.GONE);
            if (followings.size() > 0) {
                mAdapter = new FollowingItemAdapter(followings);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mActivity.showMessage("Not Following anyone.");
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            mListener = (FragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        mViewModel.clear();
    }

    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    public interface FragmentInteraction {

    }
}
