package com.ik.githubbrowser.ui.home.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ik.githubbrowser.AppUtils;
import com.ik.githubbrowser.R;
import com.ik.githubbrowser.repository.db.entity.events.Event;
import com.ik.githubbrowser.ui.home.RecyclerViewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.MyViewHolder> {

    private List<Event> mList;
    private RecyclerViewItemClickListener mItemClickListener;

    EventItemAdapter(List<Event> list) {
        mList = list;
    }

    public void updateList(List<Event> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void registerItemClickListener(RecyclerViewItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    public void unregisterItemClickListener () {
        mItemClickListener = null;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event item = mList.get(position);
        holder.tvEventName.setText(item.getType());
        holder.tvRepositoryName.setText(item.getRepo().getName());
        holder.tvCreatedAt.setText(AppUtils.dateFormat(item.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Event getEventAtPosition (int position) {
        return mList.get(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_event_name)
        TextView tvEventName;
        @BindView(R.id.tv_repository_name)
        TextView tvRepositoryName;
        @BindView(R.id.tv_created_at)
        TextView tvCreatedAt;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if (mItemClickListener != null)
                    mItemClickListener.onClick(view);
            });
        }
    }
}
