package com.github.proglottis.ladders.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.PushNotificationKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steventan on 17/09/15.
 */
public class PushNotificationKeyRequest extends Request<PushNotificationKey> {
    private static final String BASE_URL = "http://ladders.pw/api/v1/push_notification_keys";
    private static final String TAG = PushNotificationKeyRequest.class.getSimpleName();
    private final PushNotificationKey key;
    private final String token;
    private final Response.Listener<PushNotificationKey> listener;

    public PushNotificationKeyRequest(PushNotificationKey key, String token,
                                      Response.Listener<PushNotificationKey> listener,
                                      Response.ErrorListener errorListener) {
        super(Request.Method.POST, BASE_URL, errorListener);
        this.key = key;
        this.token = token;
        this.listener = listener;
    }
    @Override
    protected Response<PushNotificationKey> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            PushNotificationKey pushNotificationKey = PushNotificationKey.fromJSON(json);
            return Response.success(pushNotificationKey, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(PushNotificationKey response) {
        listener.onResponse(response);

    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return key.toJSON().toString().getBytes();
        } catch (JSONException e) {
            Log.i(TAG, "Failed to encode JSON: " + e.getMessage());
            throw new AuthFailureError();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + token);
        params.put("Content-Type", "application/json");
        return params;
    }

}
