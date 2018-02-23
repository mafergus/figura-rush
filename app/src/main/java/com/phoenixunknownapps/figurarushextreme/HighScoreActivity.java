package com.phoenixunknownapps.figurarushextreme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewferguson on 10/21/15.
 */
public class HighScoreActivity extends Activity {

    private Button inviteButton;
    private int highScore = 0;

    private Scores topScores;
    private Scores friendScores;
    private MyScoreItem myScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_activity);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            highScore = b.getInt("highScore", 0);
            Log.v("MNF", "HighScoreActivity highScore: " + highScore);
        } else {
            Log.v("MNF", "high score bundle is null!");
        }

        setScoreTvs();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("HighScore");
        query.setLimit(100);
        query.orderByDescending("score");
        query.include("user");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.v("MNF", "got high scores count: " + objects.size());
                    setTopScores(objects);
                } else {
                    Log.e("MNF", "Couldn't get high scores " + e.getMessage());
                }
            }
        });

        fetchFriendTopScores();

        inviteButton = (Button) findViewById(R.id.inviteButton);
        inviteButton.setTypeface(((FiguraRushApplication) getApplicationContext()).getFontBold());
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.phoenixunknownapps.figurarushextreme";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Bring it! Think you can beat my high score of " + highScore + " in Figura Rush?? " + url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private void setTopFriendScores(final List<FriendsSingleton.FriendScorePair> fObjects) {
        List<FriendsSingleton.FriendScorePair> objects = removeDuplicates(fObjects);
        Log.v("MNF", "setTopFriendScores count: " + objects.size());
        if (objects.size() > 0) {
            Log.v("MNF", "setting first friend high score");
            ParseObject score = objects.get(0).friend;
            String contactName = objects.get(0).number;
            ParseObject player = (ParseObject) score.get("user");
            friendScores.getItem1().setLabel(contactName + "(" + player.getString("displayName") + ")");
            friendScores.getItem1().setScore(score.getInt("score"));
        }
        if (objects.size() > 1) {
            Log.v("MNF", "setting second friend high score");
            ParseObject score = objects.get(1).friend;
            String contactName = objects.get(1).number;
            ParseObject player = (ParseObject) score.get("user");
            friendScores.getItem2().setLabel(contactName + "(" + player.getString("displayName") + ")");
            friendScores.getItem2().setScore(score.getInt("score"));
        }
        if (objects.size() > 2) {
            Log.v("MNF", "setting third friend high score");
            ParseObject score = objects.get(2).friend;
            String contactName = objects.get(2).number;
            ParseObject player = (ParseObject) score.get("user");
            friendScores.getItem3().setLabel(contactName + "(" + player.getString("displayName") + ")");
            friendScores.getItem3().setScore(score.getInt("score"));
        }
        if (objects.size() > 3) {
            Log.v("MNF", "setting fourth friend high score");
            ParseObject score = objects.get(3).friend;
            String contactName = objects.get(3).number;
            ParseObject player = (ParseObject) score.get("user");
            friendScores.getItem4().setLabel(contactName + "(" + player.getString("displayName") + ")");
            friendScores.getItem4().setScore(score.getInt("score"));
        }
    }

    private List<FriendsSingleton.FriendScorePair> removeDuplicates(final List<FriendsSingleton.FriendScorePair> friendScores) {
        List<FriendsSingleton.FriendScorePair> scoresList = new ArrayList<FriendsSingleton.FriendScorePair>();
        for (FriendsSingleton.FriendScorePair p : friendScores) {
            String objectId2 = ((ParseObject) p.friend.get("user")).getObjectId();
//            String objectId2 = ((ParseObject) po.get("user")).getObjectId();
            boolean exists = false;
            for (FriendsSingleton.FriendScorePair score : scoresList) {
                String objectId1 = ((ParseObject) score.friend.get("user")).getObjectId();
                if (objectId1.equals(objectId2)) {
                    exists = true;
                }
            }
            if (!exists) {
                scoresList.add(p);
            }
        }
        return scoresList;
    }

    private void setTopScores(final List<ParseObject> objects) {

        List<ParseObject> scoresList = new ArrayList<ParseObject>();
        for (ParseObject po : objects) {
            String objectId2 = ((ParseObject) po.get("user")).getObjectId();
            boolean exists = false;
            for (ParseObject score : scoresList) {
                String objectId1 = ((ParseObject) score.get("user")).getObjectId();
                if (objectId1.equals(objectId2)) {
                    exists = true;
                }
            }
            if (!exists) {
                scoresList.add(po);
            }
        }

        if (scoresList.size() > 0) {
            ParseObject score = scoresList.get(0);
            ParseObject player = (ParseObject) score.get("user");
            topScores.getItem1().setLabel(player.getString("displayName"));
            topScores.getItem1().setScore(score.getInt("score"));
        }
        if (scoresList.size() > 1) {
            ParseObject score = scoresList.get(1);
            ParseObject player = (ParseObject) score.get("user");
            topScores.getItem2().setLabel(player.getString("displayName"));
            topScores.getItem2().setScore(score.getInt("score"));
        }
        if (scoresList.size() > 2) {
            ParseObject score = scoresList.get(2);
            ParseObject player = (ParseObject) score.get("user");
            topScores.getItem3().setLabel(player.getString("displayName"));
            topScores.getItem3().setScore(score.getInt("score"));
        }
        if (scoresList.size() > 3) {
            ParseObject score = scoresList.get(3);
            ParseObject player = (ParseObject) score.get("user");
            topScores.getItem4().setLabel(player.getString("displayName"));
            topScores.getItem4().setScore(score.getInt("score"));
        }
    }

    private void setScoreTvs() {

        Typeface fontRegular = ((FiguraRushApplication) getApplicationContext()).getFontRegular();
        Typeface fontBold = ((FiguraRushApplication) getApplicationContext()).getFontBold();

        topScores = (Scores) findViewById(R.id.topScores);
        topScores.setTitle(getResources().getString(R.string.global_label));
        friendScores = (Scores) findViewById(R.id.friendScores);
        friendScores.setTitle(getResources().getString(R.string.friends_label));
        myScore = (MyScoreItem) findViewById(R.id.myScore);
        myScore.setScore(highScore);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String displayName = (currentUser == null) ? "You" : currentUser.getString("displayName");
        myScore.setLabel(displayName);
        myScore.setOnTouchListener(mysiTouchListener);
        final TextView myLabel = (TextView) findViewById(R.id.youLabel);
        myLabel.setTypeface(fontBold);
        myLabel.setOnTouchListener(touchListener);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ((TextView) v).setTextColor(getResources().getColor(R.color.DarkGreen));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                ((TextView) v).setTextColor(getResources().getColor(R.color.Black));
                showEditDisplayNamePopup();
            }

            return true;
        }
    };

    private View.OnTouchListener mysiTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                myScore.getLabel().setTextColor(getResources().getColor(R.color.DarkGreen));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                myScore.getLabel().setTextColor(getResources().getColor(R.color.Black));
                showEditDisplayNamePopup();
            }

            return true;
        }
    };

    private void showEditDisplayNamePopup() {
        if (ParseUser.getCurrentUser() == null) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(HighScoreActivity.this);
        View promptView = layoutInflater.inflate(R.layout.edit_display_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
        input.setText(ParseUser.getCurrentUser().getString("displayName"));
        input.setSelectAllOnFocus(true);
        input.requestFocus();
        // setup a dialog window
        alertDialogBuilder
                .setTitle("Enter Display Name")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseUser.getCurrentUser().put("displayName", "" + input.getText());
                        ParseUser.getCurrentUser().saveInBackground();
                        myScore.setLabel("" + input.getText());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    void fetchFriendTopScores() {
        final List<FriendsSingleton.FriendScorePair> friendScoresList = FriendsSingleton.getInstance().getFriendHighScores();
        Log.v("MNF", "fetched " + friendScoresList.size() + " friend scores from singleton");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTopFriendScores(friendScoresList);
                friendScores.setVisibility(View.VISIBLE);
                ((ProgressBar) HighScoreActivity.this.findViewById(R.id.friendSpinner)).setVisibility(View.GONE);
            }
        });
    }

}
