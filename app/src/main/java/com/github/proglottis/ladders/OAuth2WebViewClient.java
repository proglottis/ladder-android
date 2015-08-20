package com.github.proglottis.ladders;

import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by james on 20/08/15.
 */
public class OAuth2WebViewClient extends WebViewClient {
    private static final String TAG = OAuth2WebViewClient.class.getSimpleName();

    public interface Callback {
        void onCallback(String callbackCode, String callbackState);
    }

    private final String redirectUri;
    private final Callback callback;

    public OAuth2WebViewClient(String redirectUri, Callback callback) {
        this.redirectUri = redirectUri;
        this.callback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (url.startsWith(redirectUri)) {
            Uri uri = Uri.parse(url);
            String callbackCode = uri.getQueryParameter("code");
            String callbackState = uri.getQueryParameter("state");
            callback.onCallback(callbackCode, callbackState);
            view.stopLoading();
            return;
        }
        super.onPageStarted(view, url, favicon);
    }
}
