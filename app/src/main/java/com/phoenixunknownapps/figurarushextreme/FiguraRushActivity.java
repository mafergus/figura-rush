package com.phoenixunknownapps.figurarushextreme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phoenixunknownapps.figurarushextreme.utils.TimestampUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FiguraRushActivity extends Activity {
    private static final String TAG = FiguraRushActivity.class.getCanonicalName();

//	private InterstitialAd interstitialAd;

    private Long highScore = new Long(0);

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
            Log.v("MNF", "onGameEnd score: " + score);
            saveScore(new Long(score));
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

        initFb();
    }

    CallbackManager callbackManager;
    private LoginButton loginButton;

    private void initFb() {
        final String EMAIL = "email";

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setOnClickListener(click -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        if (loggedIn) {
//            Intent mainActivity = new Intent(this, MainActivity.class);
//            startActivity(mainActivity);
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "linkWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
//                            updateUI(user);
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.getException());
                        Toast.makeText(FiguraRushActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        fbCreate(credential);
                    }
                });
    }

    private void fbCreate(AuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FiguraRushActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
    protected void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "signInAnonymously:success");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userLoginEvent");
                Map<String, Object> updates = new HashMap<>();
                updates.put(user.getUid(), TimestampUtils.getISO8601StringForCurrentDate());
                ref.updateChildren(updates);
            } else {
                Log.w(TAG, "signInAnonymously:failure", task.getException());
                Toast.makeText(FiguraRushActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
    }

    private void loginDone() {
//        FriendsSingleton.getInstance().loadFriendScores(this);
    }

    private String loadUserId() {
        return null;
    }

//    public void loadHighScore() {
//        String scoreString = readScoreFromFile("highScore.txt");
//        highScore = Integer.valueOf(scoreString.length() == 0 ? "0" : scoreString);
//    }

    public void saveScore(Long score) {
//        writeScoreToFile("highScore.txt", "" + highScore);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users/" + user.getUid() + "/highScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long previousHighScore = (Long)dataSnapshot.getValue();
                    if (score > previousHighScore) {
                        highScore = score;
                        database.child("high-scores").child(user.getUid()).setValue(score);
                        database.child("users").child(user.getUid()).child("highScore").setValue(score);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
