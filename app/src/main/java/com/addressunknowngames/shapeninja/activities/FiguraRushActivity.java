package com.addressunknowngames.shapeninja.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.addressunknowngames.shapeninja.FiguraRushApplication;
import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.utils.Constants;
import com.addressunknowngames.shapeninja.utils.TimestampUtils;
import com.addressunknowngames.shapeninja.utils.Utils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FiguraRushActivity extends Activity {
    private static final String TAG = FiguraRushActivity.class.getCanonicalName();

//	private InterstitialAd interstitialAd;

    public static Long highScore = new Long(0);

    private Button questButton;
    private Button startButton;
    private Button highScoresButton;
    private Button shareButton;

    private Typeface FONT_REGULAR;
    private Typeface FONT_BOLD;

    CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.figura_rush_main);

        FONT_REGULAR = ((FiguraRushApplication) getApplicationContext()).getFontRegular();
        FONT_BOLD = ((FiguraRushApplication) getApplicationContext()).getFontBold();

        questButton = findViewById(R.id.questButton);
        questButton.setTypeface(FONT_BOLD);
        questButton.setOnClickListener(view -> {
            Intent questIntent = new Intent(FiguraRushActivity.this, QuestsActivity.class);
            startActivity(questIntent);
        });
        startButton = findViewById(R.id.trainMagicStartButton);
        startButton.setTypeface(FONT_BOLD);
        startButton.setOnClickListener(view -> onStartGame());
        highScoresButton = findViewById(R.id.highScores);
        highScoresButton.setTypeface(FONT_BOLD);
        highScoresButton.setOnClickListener(v -> showHighScores());
        shareButton = findViewById(R.id.share);
        shareButton.setTypeface(FONT_BOLD);
        shareButton.setOnClickListener(v -> {
            String url = Constants.PLAY_STORE_URL;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Bring it! Think you can beat my high score of " + highScore + " in Figura Rush?? " + url);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
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

        initFb();

        String highScore = Utils.readScoreFromFile(FiguraRushActivity.this, "highScore.txt");
        this.highScore = Long.decode(highScore);
    }

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
            }

            @Override
            public void onError(FacebookException exception) {
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
//                        fbCreate(credential);
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
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onStartGame() {
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
            dialog.setOnDismissListener(d -> {
                launchGameIntent();
            });
            dialog.show();
        } else {
            launchGameIntent();
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

    private void launchGameIntent() {
        Intent gameIntent = new Intent(FiguraRushActivity.this, GameActivity.class);
        startActivity(gameIntent);
        finish();
    }

    private void showHighScores() {
        Intent highScoreIntent = new Intent();
        highScoreIntent.putExtra("highScore", highScore);
        Log.v("MNF", "FiguraRushActivity adding highScore: " + highScore);
        highScoreIntent.setClass(FiguraRushActivity.this, HighScoreActivity.class);
        startActivity(highScoreIntent);
    }

    private void loadAd() {
        double randNum = Math.random();
//		if (randNum <= 0.15) {
//			interstitialAd.show();
//			interstitialAd.loadAd(new AdRequest.Builder().build());
//		}
    }

}
