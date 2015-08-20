package com.github.proglottis.ladders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.github.proglottis.ladders.data.AuthToken;

public class AuthActivity extends AppCompatActivity implements Response.Listener<AuthToken> {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_URL = "com.github.proglottis.ladders.extra_url";
    public static final String EXTRA_STATE = "com.github.proglottis.ladders.extra_state";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final String stateExtra = getIntent().getStringExtra(EXTRA_STATE);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, url);
                if (url.startsWith("http://ladders.pw/auth/google_oauth2/callback")) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    String state = uri.getQueryParameter("state");
                    Log.d(TAG, "StateExtra:" + stateExtra + " ==== State:" + state);
                    if(!java.security.MessageDigest.isEqual(state.getBytes(), stateExtra.getBytes())) {
                        Log.d(TAG, "Shits fucked up");
                        finish();
                        return;
                    }
                    Log.d(TAG, "GOT TO THE REQUEST THINGY!!!!!!");
                    AuthTokenRequest req = new AuthTokenRequest("google", code, getString(R.string.google_client_id), getString(R.string.google_redirect_uri), AuthActivity.this, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            VolleyLog.d(TAG, "Error: " + e.getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);
                    view.stopLoading();
                    return;
                }
                super.onPageStarted(view, url, favicon);
            }
        });
        webView.loadUrl(getIntent().getStringExtra(EXTRA_URL));
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

    @Override
    public void onResponse(AuthToken response) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString(getString(R.string.api_token), response.getToken()).apply();
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
