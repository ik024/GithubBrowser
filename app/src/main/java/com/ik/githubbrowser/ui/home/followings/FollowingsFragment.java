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
import com.ik.githubbrowser.MyRecyclerView;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.Repository;
import com.ik.githubbrowser.repository.RepositoryImpl;
import com.ik.githubbrowser.repository.db.entity.followings.Following;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.EmptyLayout;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;

public class FollowingsFragment extends Fragment implements LifecycleOwner, RecyclerViewItemClickListener {

    @BindView(R.id.rv_followings)
    MyRecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.layout_empty_view)
    View mEmptyView;

    private EmptyLayout mIncludedEmptyLayout = new EmptyLayout();

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private FragmentInteraction mListener;
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

        ButterKnife.bind(mIncludedEmptyLayout, mEmptyView);
        mIncludedEmptyLayout.tvEmptyView.setText(R.string.empty_following_list);

        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        FollowingFragmentViewModelFactory factory = new FollowingFragmentViewModelFactory(repository, mUsername);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(FollowingsFragmentViewModel.class);

        mViewModel.getFollowings().observe(this, followings -> {
            mProgressBar.setVisibility(View.GONE);
            if (mAdapter == null) {
                mAdapter = new FollowingItemAdapter(followings);
                mRecyclerView.setEmptyView(mEmptyView);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.updateList(followings);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!= null)
            mAdapter.registerItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.unregisterItemClickListener();
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

    @Override
    public void onClick(Object object) {
        View view = (View) object;
        int position = mRecyclerView.getChildLayoutPosition(view);
        Following following = mAdapter.getFollowingAtPosition(position);
        mListener.onFollowingClicked(following.getLogin());
    }

    public interface FragmentInteraction {
        void onFollowingClicked(String followerUserName);
    }
}
