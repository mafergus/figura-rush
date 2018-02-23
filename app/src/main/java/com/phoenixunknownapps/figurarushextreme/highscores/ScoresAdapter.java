package com.phoenixunknownapps.figurarushextreme.highscores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenixunknownapps.figurarushextreme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewferguson on 2/23/18.
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    final private Context context;
    final private List<ScoreEntry> scores = new ArrayList<>();

    public static class ScoreEntry {
        final public String user;
        final public Long score;

        public ScoreEntry(String user, Long score) {
            this.user = user;
            this.score = score;
        }
    }

    public ScoresAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.high_score_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScoreEntry entry = scores.get(position);
        holder.populate(context, entry);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public void setScores(List<ScoreEntry> scores) {
        this.scores.clear();
        this.scores.addAll(scores);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView name;
        final private TextView score;

        ViewHolder(View v) {
            super(v);
            this.name = (TextView)v.findViewById(R.id.label);
            this.score = (TextView)v.findViewById(R.id.score);
        }

        void populate(Context context,  ScoreEntry entry) {
            this.name.setText(entry.user);
            this.score.setText("" + entry.score);
        }
    }
}
