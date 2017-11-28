package com.ik.githubbrowser.ui.home.followers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.db.entity.followers.Follower;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerItemAdapter extends RecyclerView.Adapter<FollowerItemAdapter.MyViewHolder> {

    private List<Follower> mList;
    private RecyclerViewItemClickListener mItemClickListener;
    private Picasso mPicasso;

    public FollowerItemAdapter(List<Follower> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);

        mPicasso = NetworkInstance.getInstance(parent.getContext()).getPicasso();

        return new MyViewHolder(view);
    }

    public void registerItemClickListener (RecyclerViewItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void unregisterClickListener () {
        mItemClickListener = null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Follower follower = mList.get(position);
        holder.tvUsername.setText(follower.getLogin());
        mPicasso.load(follower.getAvatar_url()).into(holder.ivProfilePic);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<Follower> followers) {
        mList = followers;
        notifyDataSetChanged();
    }

    public Follower  getFollowerAtPosition (int position) {
        return mList.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile_pic)
        ImageView ivProfilePic;
        @BindView(R.id.tv_name)
        TextView tvUsername;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                if (mItemClickListener != null)
                    mItemClickListener.onClick(itemView);
            });
        }
    }
}
