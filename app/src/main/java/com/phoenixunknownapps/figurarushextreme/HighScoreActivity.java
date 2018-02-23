package com.phoenixunknownapps.figurarushextreme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
//        if (ParseUser.getCurrentUser() == null) {
//            return;
//        }
        LayoutInflater layoutInflater = LayoutInflater.from(HighScoreActivity.this);
        View promptView = layoutInflater.inflate(R.layout.edit_display_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
//        input.setText(ParseUser.getCurrentUser().getString("displayName"));
        input.setSelectAllOnFocus(true);
        input.requestFocus();
        // setup a dialog window
        alertDialogBuilder
                .setTitle("Enter Display Name")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        ParseUser.getCurrentUser().put("displayName", "" + input.getText());
//                        ParseUser.getCurrentUser().saveInBackground();
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
//        final List<FriendsSingleton.FriendScorePair> friendScoresList = FriendsSingleton.getInstance().getFriendHighScores();
//        Log.v("MNF", "fetched " + friendScoresList.size() + " friend scores from singleton");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                setTopFriendScores(friendScoresList);
//                friendScores.setVisibility(View.VISIBLE);
//                ((ProgressBar) HighScoreActivity.this.findViewById(R.id.friendSpinner)).setVisibility(View.GONE);
//            }
//        });
    }

}
