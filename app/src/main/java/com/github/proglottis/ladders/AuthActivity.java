package com.github.proglottis.ladders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.proglottis.ladders.data.AuthToken;
import com.github.proglottis.ladders.requests.AuthTokenRequest;

public class AuthActivity extends AppCompatActivity implements Response.Listener<AuthToken>, OAuth2WebViewClient.Callback {
    private static final String TAG = AuthActivity.class.getSimpleName();
    public static final String AUTH_URL = "com.github.proglottis.ladders.auth_url";
    public static final String AUTH_STATE = "com.github.proglottis.ladders.auth_state";

    private String url;
    private String state;
    private View progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent intent = getIntent();
        url = intent.getStringExtra(AUTH_URL);
        state = intent.getStringExtra(AUTH_STATE);

        progressBar = findViewById(R.id.progress);
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new OAuth2WebViewClient(getString(R.string.google_redirect_uri), this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadUrl(url);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.stopLoading();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    public void onCallback(String callbackCode, String callbackState) {
        hideContent();
        if(!java.security.MessageDigest.isEqual(callbackState.getBytes(), state.getBytes())) {
            onAuthFailure();
            return;
        }
        AuthTokenRequest req = new AuthTokenRequest("google", callbackCode, getString(R.string.google_client_id), getString(R.string.google_redirect_uri), this, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d(TAG, "Error: " + e.getLocalizedMessage());
                onAuthFailure();
            }
        });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    private void onAuthFailure() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove(getString(R.string.api_token)).apply();
        Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResponse(AuthToken response) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(getString(R.string.api_token), response.getToken()).apply();
        Intent intent = new Intent(this, TournamentListActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideContent() {
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.INVISIBLE);
    }
}
