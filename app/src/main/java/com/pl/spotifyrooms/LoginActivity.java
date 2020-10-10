package com.pl.spotifyrooms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.pl.spotifyrooms.util.Delayed;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import static com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN;
import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private static final String CLIENT_ID = "8090edf43a7c41afb6d3e5d7686e32a9";
    private static final String REDIRECT_URI = "com.pl.spotifyrooms://callback";

    public static SpotifyAppRemote spotifyController;
    public static SpotifyService spotifyService;

    private CardView logInButton;
    private TextView logInText;
    private ProgressBar loadingCircle;

    private boolean isLoading;

    private static final boolean isDebug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);
        logInButton = findViewById(R.id.log_in);
        logInButton.setOnClickListener(logIn);
        logInText = findViewById(R.id.simple_text_label);
        loadingCircle = findViewById(R.id.load_circle);
        TextView terms = findViewById(R.id.terms_of_usage);
        terms.setOnClickListener(termsClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLoading = false;
        if (hasRecognizer()) {
            showLoading(true);
            spotifyLogin();
        }
    }

    private void spotifyLogin() {
        if (isDebug){
            loadMain();
            return;
        }

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from spotify
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    String token = response.getAccessToken();
                    Log.i("Spotify", "Logged in!");
                    Log.i("Spotify", "token=" + token.substring(0, 10) + "...");

                    loggedIn(token);
                    break;

                // Auth flow returned an error
                case ERROR:
                    showLoading(false);
                    Log.e("Spotify", "Login error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
            }
        }
    }

    private void loggedIn(String token) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        spotifyService = api.getService();
        activateRemote();
    }

    private void activateRemote() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        spotifyController = spotifyAppRemote;
                        Log.i("Spotify", "Connected!");

                        loadMain();
                    }

                    public void onFailure(Throwable throwable) {
                        showLoading(false);
                        Log.e("Spotify", throwable.getMessage(), throwable);
                    }
                });
    }

    private final View.OnClickListener termsClick = v -> {
        Toast.makeText(this, "tba", Toast.LENGTH_SHORT).show();
    };

    private final View.OnClickListener logIn = v -> {
        if (!isLoading) {
            showLoading(true);
            new Delayed(this::spotifyLogin, 1000);
        }
    };

    private void showLoading(boolean condition) {
        View v = logInButton;
        if (condition) {
            logInText.setVisibility(View.INVISIBLE);
            loadingCircle.setVisibility(View.VISIBLE);
            isLoading = true;
        } else {
            logInText.setVisibility(View.VISIBLE);
            loadingCircle.setVisibility(View.INVISIBLE);
            isLoading = false;
        }
    }

    private void loadMain() {
        new Delayed(() -> {
            saveRecognizer();
            Log.i("Login", "Login successful");
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
        }, 500);
    }

    private boolean hasRecognizer() {
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.contains("RECOGNIZER");
    }

    private void saveRecognizer() {
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean("RECOGNIZER", true).apply();
    }

}