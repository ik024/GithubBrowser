package com.ik.githubbrowser.ui.home.followings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.db.entity.followings.Following;
import com.ik.githubbrowser.repository.network.NetworkInstance;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FollowingItemAdapter extends RecyclerView.Adapter<FollowingItemAdapter.MyViewHolder> {

    private List<Following> mList;
    private RecyclerViewItemClickListener mItemClickListener;
    private Picasso mPicasso;

    public FollowingItemAdapter (List<Following> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);

        mPicasso = NetworkInstance.getInstance(parent.getContext()).getPicasso();

        return new MyViewHolder(view);
    }


    public void registerItemClickListener (RecyclerViewItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void unregisterItemClickListener () {
        mItemClickListener = null;
    }

    public Following getFollowingAtPosition (int position){
        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Following following = mList.get(position);
        holder.tvName.setText(following.getLogin());
        mPicasso.load(following.getAvatar_url()).into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<Following> followings) {
        mList = followings;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

         @BindView(R.id.iv_profile_pic)
         ImageView ivProfilePic;

         @BindView(R.id.tv_name)
         TextView tvName;

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
