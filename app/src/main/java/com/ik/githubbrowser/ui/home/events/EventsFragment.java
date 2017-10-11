package com.ik.githubbrowser.ui.home.events;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.repository.network.ApiService;
import com.ik.githubbrowser.repository.network.NetworkInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ik.githubbrowser.AppConstants.KEY_USERNAME;


public class EventsFragment extends Fragment implements LifecycleOwner {

    private LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private String mUsername;
    private BaseActivity mActivity;
    private FragmentInteraction mListener;

    @BindView(R.id.rv_activity)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private EventItemAdapter mAdapter;
    private EventsFragmentViewModel mViewModel;

    public EventsFragment() {
        // Required empty public constructor
    }


    public static EventsFragment newInstance(String username) {
        EventsFragment fragment = new EventsFragment();
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
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this, view);

        ApiService apiService = NetworkInstance.getInstance(getContext()).getApiService();
        Repository repository = new RepositoryImpl(apiService);
        EventsFragmentViewModelFactory factory
                = new EventsFragmentViewModelFactory(repository, mUsername);

        mViewModel = ViewModelProviders.of(getActivity(), factory).get(EventsFragmentViewModel.class);

        mActivity = (BaseActivity) getActivity();

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewModel.observerMsgs().observe(this, msg -> mActivity.showMessage(msg));

        mViewModel.getEvents().observe(this, this::populateRecyclerView);

        return view;
    }

    private void populateRecyclerView(List<Event> list) {
        mProgressBar.setVisibility(View.GONE);
        if (list.size() > 0) {
            mAdapter = new EventItemAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mActivity.showMessage(getString(R.string.no_events));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            mListener = (FragmentInteraction) context;
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mViewModel.clear();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    public interface FragmentInteraction {
    }
}
