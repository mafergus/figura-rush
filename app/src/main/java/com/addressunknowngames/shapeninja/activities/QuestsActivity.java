package com.addressunknowngames.shapeninja.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.lists.QuestsAdapter;
import com.addressunknowngames.shapeninja.model.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matthewferguson on 2/28/18.
 */

public class QuestsActivity extends Activity {

    private TextView closeButton;

    private QuestsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> {
            finish();
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("quests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Quest> questList = new ArrayList<>();
                    HashMap<String, Object> quests = (HashMap)dataSnapshot.getValue();
                    for (String key : quests.keySet()) {
                        HashMap<String, Object> questMap = (HashMap<String, Object>) quests.get(key);
                        Quest quest = Quest.fromMap(key, questMap);
                        questList.add(quest);
                    }
                    adapter.setQuests(questList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        list = findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(QuestsActivity.this);
        adapter = new QuestsAdapter();
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }
}
