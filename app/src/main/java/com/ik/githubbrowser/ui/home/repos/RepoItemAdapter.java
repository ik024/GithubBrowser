package com.ik.githubbrowser.ui.home.repos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.db.entity.RepoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RepoItemAdapter extends RecyclerView.Adapter<RepoItemAdapter.MyViewHolder> {

    private List<RepoItem> mList;

    public RepoItemAdapter (List<RepoItem> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RepoItem item = mList.get(position);
        holder.tvRepoName.setText(item.getRepoName());
        holder.tvRepoDesc.setText(item.getRepoDesc());
        holder.tvCommitCount.setText(item.getCommitCount()+" Commits");
        holder.tvIssueCount.setText(item.getIssueCount()+" Issues");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_repo_name)
        TextView tvRepoName;

        @BindView(R.id.tv_repo_desc)
        TextView tvRepoDesc;

        @BindView(R.id.tv_commit_count)
        TextView tvCommitCount;

        @BindView(R.id.tv_issue_count)
        TextView tvIssueCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
