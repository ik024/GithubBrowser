package com.ik.githubbrowser.ui.home.followers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerItemAdapter extends RecyclerView.Adapter<FollowerItemAdapter.MyViewHolder> {

    private List<Follower> mList;

    public FollowerItemAdapter(List<Follower> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Follower follower = mList.get(position);
        holder.tvUsername.setText(follower.getLogin());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile_pic)
        ImageView ivProfilePic;
        @BindView(R.id.tv_name)
        TextView tvUsername;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
