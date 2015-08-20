package com.github.proglottis.ladders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.proglottis.ladders.data.AuthToken;

public class AuthActivity extends AppCompatActivity implements Response.Listener<AuthToken>, OAuth2WebViewClient.Callback {
    private static final String TAG = AuthActivity.class.getSimpleName();
    public static final String EXTRA_URL = "com.github.proglottis.ladders.extra_url";
    public static final String EXTRA_STATE = "com.github.proglottis.ladders.extra_state";

    private String url;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent intent = getIntent();
        url = intent.getStringExtra(EXTRA_URL);
        state = intent.getStringExtra(EXTRA_STATE);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new OAuth2WebViewClient(getString(R.string.google_redirect_uri), this));
        webView.loadUrl(url);
    }

    public void onCallback(String callbackCode, String callbackState) {
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
        AppController.getInstance().addToRequestQueue(req);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
