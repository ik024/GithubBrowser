package com.ik.githubbrowser;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


public class MyRecyclerView extends RecyclerView {

    private View mEmptyView;

    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
    
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean emptyViewVisible = getAdapter().getItemCount() ==  0;
            mEmptyView.setVisibility(emptyViewVisible ? VISIBLE :   GONE);
            setVisibility(emptyViewVisible ? GONE :  VISIBLE);
        }
    }

    public void setEmptyView (View view) {
        mEmptyView = view;
        checkIfEmpty();
    }

}
