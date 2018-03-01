package com.addressunknowngames.shapeninja.highscores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.addressunknowngames.shapeninja.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewferguson on 2/23/18.
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    final private Context context;
    final private List<ScoreEntry> scores = new ArrayList<>();

    public static class ScoreEntry implements Comparable<ScoreEntry> {
        final public String userId;
        final public String displayName;
        final public Long score;

        public ScoreEntry(String displayName, String userId, Long score) {
            this.displayName = displayName;
            this.userId = userId;
            this.score = score;
        }

        @Override
        public int compareTo(@NonNull ScoreEntry o) {
            if (this.score > o.score) {
                return -1;
            } else if (this.score < o.score) {
                return 1;
            } else {
                return 0;
            }
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
        final private View rootView;
        final private TextView name;
        final private TextView score;

        ViewHolder(View v) {
            super(v);
            this.rootView = v;
            this.name = (TextView)v.findViewById(R.id.label);
            this.score = (TextView)v.findViewById(R.id.score);
        }

        void populate(Context context,  ScoreEntry entry) {
            boolean isCurrentUser = entry.userId.equals(FirebaseAuth.getInstance().getUid());
            this.rootView.setBackgroundColor(isCurrentUser ?
                    context.getResources().getColor(R.color.Aquamarine) :
                    context.getResources().getColor(android.R.color.transparent));
            this.name.setText(isCurrentUser ? "You" : entry.displayName);
            this.score.setText("" + entry.score);
        }
    }
}
