package com.phoenixunknownapps.figurarushextreme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class FiguraRushActivity extends Activity {

    private int highScore = 0;

//	private InterstitialAd interstitialAd;

    //	private ViewGroup rootView;
    private Button startButton;
    private Button startButton2;
    private Button highScoresButton;
    private Button highScoresButton2;
    private Button practiceButton;
    private Button practiceButton2;
    private ViewGroup overlay;

    private ViewGroup highScoreOverlay;
    private TextView gameEndedText;
    private TextView highScoreTextLabel;
    private TextView highScoreText;
    private TextView gameTimeTextLabel;
    private TextView gameTimeText;

    private GameBase currentGame;
    private GameBase game;
    private GameBase practiceGame;

    private Typeface FONT_REGULAR;
    private Typeface FONT_BOLD;

    private IGame.GameCallback gameCallback = new IGame.GameCallback() {

        @Override
        public void onGameEnd(final long timeMs, final int score) {
            Log.v("MNF", "onGameEnd score: " + score + " high score: " + highScore);
            if (score > highScore) {
                highScore = score;
                saveHighScore();
            }
            gameEndedText.setText(FiguraRushActivity.this.getString(R.string.game_over));

            Animation.AnimationListener listener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.v("MNF", "onAnimationEnd");
                    String text = timeToString(timeMs);
                    highScoreText.setText("" + score);
                    gameTimeText.setText(text);
                    highScoreOverlay.setVisibility(View.VISIBLE);
                    SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
//                    startButton2.setText(FiguraRushActivity.this.getString(R.string.level, prefs.getInt("level", 1)));
                    loadAd();

                    game.reset();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };

            currentGame.playExitAnim(listener);
        }

        @Override
        public void onGameWon(long timeMs, int score) {
            Log.v("MNF", "onGameWon score: " + score);
            SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
            int level = prefs.getInt("level", 1);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("level", level + 1);
            editor.commit();

            onGameEnd(timeMs, score);
            gameEndedText.setText(FiguraRushActivity.this.getString(R.string.level_passed, level));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.figura_rush_main);

        FONT_REGULAR = ((FiguraRushApplication) getApplicationContext()).getFontRegular();
        FONT_BOLD = ((FiguraRushApplication) getApplicationContext()).getFontBold();

        doParseLogin();

        loadHighScore();

        gameEndedText = (TextView) findViewById(R.id.gameEndedTitle);
        gameEndedText.setTypeface(FONT_REGULAR);
        highScoreOverlay = (ViewGroup) findViewById(R.id.highScoreOverlay);
        highScoreTextLabel = (TextView) findViewById(R.id.highScoreTextLabel);
        highScoreTextLabel.setTypeface(FONT_REGULAR);
        highScoreText = (TextView) findViewById(R.id.highScoreText);
        highScoreText.setTypeface(FONT_REGULAR);
        gameTimeTextLabel = (TextView) findViewById(R.id.gameTimeTextLabel);
        gameTimeTextLabel.setTypeface(FONT_REGULAR);
        gameTimeText = (TextView) findViewById(R.id.gameTimeText);
        gameTimeText.setTypeface(FONT_REGULAR);

        game = (GameBase) findViewById(R.id.game);
        game.setGameCallback(gameCallback);
        practiceGame = (GameBase) findViewById(R.id.practiceGame);
        overlay = (ViewGroup) findViewById(R.id.startOverlay);
        startButton = (Button) findViewById(R.id.trainMagicStartButton);
        startButton.setTypeface(FONT_BOLD);
        //		TextAppearanceSpan span = new TextAppearanceSpan(context, appearance)
        startButton.setOnClickListener(new StartButtonClickListener(overlay));
        startButton2 = (Button) findViewById(R.id.startButton2);
        startButton2.setTypeface(FONT_BOLD);
        startButton2.setOnClickListener(new StartButtonClickListener(highScoreOverlay));
        highScoresButton = (Button) findViewById(R.id.highScores);
        highScoresButton.setTypeface(FONT_BOLD);
        highScoresButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showHighScores();
            }
        });
        highScoresButton2 = (Button) findViewById(R.id.highScores2);
        highScoresButton2.setTypeface(FONT_BOLD);
        highScoresButton2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showHighScores();
            }
        });
        practiceButton = (Button) findViewById(R.id.practice);
        practiceButton.setTypeface(FONT_BOLD);
        practiceButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
//                currentGame = practiceGame;
//                currentGame.setVisibility(View.VISIBLE);
//                currentGame.startGame(prefs.getInt("level", 1));
//                overlay.setVisibility(View.GONE);

                String url = "https://play.google.com/store/apps/details?id=com.phoenixunknownapps.figurarushextreme";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Bring it! Think you can beat my high score of " + highScore + " in Figura Rush?? " + url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        practiceButton2 = (Button) findViewById(R.id.practice2);
        practiceButton2.setTypeface(FONT_BOLD);
        practiceButton2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.phoenixunknownapps.figurarushextreme";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Bring it! Think you can beat my high score of " + highScore + " in Figura Rush?? " + url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

//                SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
//                currentGame = practiceGame;
//                currentGame.setVisibility(View.VISIBLE);
//                currentGame.startGame(prefs.getInt("level", 1));
//                highScoreOverlay.setVisibility(View.GONE);
            }
        });

//		interstitialAd = new InterstitialAd(this);
//		interstitialAd.setAdUnitId("ca-app-pub-8435025261167085/7660447653");
//		interstitialAd.loadAd(new AdRequest.Builder().build());
//
//		AppRater.app_launched(this);

        SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
        boolean isFte = prefs.getBoolean(getPackageName() + "prefs" + "isFte", true);
        if (isFte) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(getPackageName() + "prefs" + "isFte", false);
            editor.commit();
        }

        int level = prefs.getInt("level", 1);
//        startButton.setText(getString(R.string.level, level));


    }

    private class StartButtonClickListener implements OnClickListener {
        final private View view;

        public StartButtonClickListener(View v) {
            view = v;
        }

        @Override
        public void onClick(View v) {

            final SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
            boolean isFte = prefs.getBoolean("isFte", true);
            if (isFte) {
                SharedPreferences.Editor e = prefs.edit();
                e.putBoolean("isFte", false);
                e.apply();
                AlertDialog dialog = new AlertDialog.Builder(FiguraRushActivity.this)
                        .setTitle("Welcome")
                        .setMessage("Draw the shape on the screen with your finger!")
                        .setCancelable(false)
                        .setPositiveButton("Let's Go!", null)
                        .create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setGame();
                    }
                });
                dialog.show();
            } else {
                setGame();
            }
        }

        private void setGame() {
            final SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
            currentGame = game;
            currentGame.setVisibility(View.VISIBLE);
            currentGame.startGame(prefs.getInt("level", 1));
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(getPackageName() + "prefs", Context.MODE_PRIVATE);
        int level = prefs.getInt("level", 1);
//        startButton.setText(getString(R.string.level, level));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        boolean isPractice = (practiceGame.getVisibility() == View.VISIBLE);
        if (isPractice) {
            practiceGame.setVisibility(View.INVISIBLE);
            overlay.setVisibility(View.VISIBLE);
            return;
        } else {
            finish();
        }
    }

    private void showHighScores() {
        Intent highScoreIntent = new Intent();
        highScoreIntent.putExtra("highScore", highScore);
        Log.v("MNF", "FiguraRushActivity adding highScore: " + highScore);
        highScoreIntent.setClass(FiguraRushActivity.this, HighScoreActivity.class);
        startActivity(highScoreIntent);
    }

    private void doParseLogin() {

        final String username = loadUserId();

//        ParseUser.logInInBackground(username, "my pass", new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (user != null) {
//                    loginDone();
//                } else {
//                    final ParseUser user1 = new ParseUser();
//                    user1.setUsername(username);
//                    user1.setPassword("my pass");
//                    user1.put("displayName", "Anonymous-" + Math.abs(new Random().nextInt()));
//                    user1.signUpInBackground(new SignUpCallback() {
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                loginDone();
//                            } else {
//                                Toast.makeText(FiguraRushActivity.this, "Login Failed! " + e.getCode(), Toast.LENGTH_LONG).show();
//                                Crashlytics.getInstance().core.setString("username", loadUserId());
//                                Crashlytics.getInstance().core.setInt("loginErrorCode", e.getCode());
//                                Crashlytics.getInstance().core.setString("loginErrorMessage", e.getMessage());
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    private void loginDone() {
//        FriendsSingleton.getInstance().loadFriendScores(this);
    }

    private String loadUserId() {
        SharedPreferences prefs = getSharedPreferences("FiguraRushPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            userId = tMgr.getLine1Number();
            if (userId == null || userId.equals("")) {
                promptPhoneNumber();
            } else {
                userId = cleanPhoneNumber(userId);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userId", userId);
                editor.commit();
            }
        }
        return userId;
    }

    private String cleanPhoneNumber(String number) {
        return number.replace("+", "").replace("(", "").replace(")", "").replace(" ", "").replace("-", "");
    }

    private void promptPhoneNumber() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.edit_display_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
        input.setSelectAllOnFocus(true);
        input.requestFocus();
//         setup a dialog window
//        alertDialogBuilder
//                .setTitle("Enter Your Phone Number, with Country Code")
//                .setMessage("We're going to find your friends!")
//                .setCancelable(true)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String number = cleanPhoneNumber("" + input.getText());
//                        ParseUser user = ParseUser.getCurrentUser();
//                        if (user != null) {
//                            ParseUser.getCurrentUser().put("username", number);
//                            ParseUser.getCurrentUser().saveInBackground();
//                        }
//                    }
//                })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                SharedPreferences prefs = getSharedPreferences("FiguraRushPrefs", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                String userId = "" + Math.abs(new Random().nextInt());
//                                editor.putString("userId", userId);
//                                editor.commit();
//                            }
//                        });
//        // create an alert dialog
//        AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();
    }

    public void loadHighScore() {
        String scoreString = readScoreFromFile("highScore.txt");
        highScore = Integer.valueOf(scoreString.length() == 0 ? "0" : scoreString);
    }

    public void saveHighScore() {
        writeScoreToFile("highScore.txt", "" + highScore);

//        ParseUser user = ParseUser.getCurrentUser();
//        if (user != null) {
//            ParseQuery<ParseObject> highScoreQuery = ParseQuery.getQuery("HighScore");
//            highScoreQuery.whereEqualTo("user", user);
//            highScoreQuery.addDescendingOrder("score");
//            highScoreQuery.getFirstInBackground(new GetCallback<ParseObject>() {
//                @Override
//                public void done(ParseObject object, ParseException e) {
//                    if (e == null) {
//                        if (object != null) {
//                            Log.v("MNF", "Got high score object from server, putting new high score " + highScore);
//                            object.put("score", highScore);
//                            object.saveInBackground();
//                        } else {
//                            Log.v("MNF", "No high score object, adding a new one for user " + ParseUser.getCurrentUser() + " score " + highScore);
//                            ParseObject highScoreObject = new ParseObject("HighScore");
//                            highScoreObject.put("score", highScore);
//                            highScoreObject.put("user", ParseUser.getCurrentUser());
//                            highScoreObject.saveInBackground();
//                        }
//                    }
//                }
//            });
//        }
    }

    private void writeScoreToFile(String filename, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readScoreFromFile(String filename) {
        String ret = "";
        try {
            InputStream inputStream = openFileInput(filename);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            writeScoreToFile(filename, "0");
            return "0";
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            writeScoreToFile(filename, "0");
            return "0";
        }

        return ret;
    }

    private String timeToString(long totalRunTimeMs) {
        int hours = (int) (totalRunTimeMs / (1000 * 60 * 60));
        totalRunTimeMs -= (hours * 1000 * 60 * 60);
        int minutes = (int) (totalRunTimeMs / (1000 * 60));
        totalRunTimeMs -= (minutes * 1000 * 60);
        int seconds = (int) (totalRunTimeMs / 1000);
        totalRunTimeMs -= (seconds * 1000);
        int miliseconds = (int) (totalRunTimeMs);

        String hoursStr = "" + (hours == 0 ? "" : hours);
        String minutesStr = "" + (minutes == 0 ? "" : String.format("%02d", minutes));
        String secondsStr = String.format("%02d", seconds);
        String milisecondsStr = String.format("%2d", miliseconds / 10);

        String text = hoursStr + (hours != 0 ? ":" : "") + minutesStr + (minutes != 0 ? ":" : "") + secondsStr + "." + milisecondsStr;

        return text;
    }

    private void loadAd() {
        double randNum = Math.random();
//		if (randNum <= 0.15) {
//			interstitialAd.show();
//			interstitialAd.loadAd(new AdRequest.Builder().build());
//		}
    }

}
