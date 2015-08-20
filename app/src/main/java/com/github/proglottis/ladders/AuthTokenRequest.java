package com.github.proglottis.ladders;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by james on 20/08/15.
 */
public class AuthTokenRequest extends Request<AuthToken> {
    private static final String TAG = AuthTokenRequest.class.getSimpleName();


    private static final String BASE_URL = "http://ladders.pw/api/v1/auth/";
    private final Response.Listener<AuthToken> listener;

    public AuthTokenRequest(String provider, String code, String clientId, String redirectUri, Response.Listener<AuthToken> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, BASE_URL + provider + "?code="+code+"&clientId="+clientId+"&redirectUri="+redirectUri, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<AuthToken> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            AuthToken token = AuthToken.fromJSON(json);
            return Response.success(token, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(AuthToken response) {
        listener.onResponse(response);
    }
}
