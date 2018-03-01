package com.addressunknowngames.shapeninja.lists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.model.Quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewferguson on 2/28/18.
 */

public class QuestsAdapter extends RecyclerView.Adapter<QuestsAdapter.ViewHolder> {

    private List<Quest> quests = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.populate(quest);
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public void setQuests(List<Quest> quests) {
        this.quests.clear();
        this.quests.addAll(quests);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.title);
            this.description = itemView.findViewById(R.id.description);
        }

        void populate(Quest quest) {
            title.setText(quest.title);
            description.setText(quest.description);
        }
    }
}
