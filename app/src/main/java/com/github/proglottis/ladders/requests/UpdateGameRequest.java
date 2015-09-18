package com.github.proglottis.ladders.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.Game;
import com.github.proglottis.ladders.data.GameUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 18/09/15.
 */
public class UpdateGameRequest extends Request<Game> {
    private static final String TAG = UpdateGameRequest.class.getSimpleName();

    private static final String BASE_URL = "http://ladders.pw/api/v1/games/";
    private final Response.Listener<Game> listener;
    private final String token;
    private final GameUpdate update;


    public UpdateGameRequest(GameUpdate update, String token, Response.Listener<Game> listener, Response.ErrorListener errorlistener) {
        super(Method.PATCH, BASE_URL+update.getGameId(), errorlistener);
        this.token = token;
        this.listener = listener;
        this.update = update;
    }

    @Override
    protected Response<Game> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            Game game = Game.fromJSON(json);
            return Response.success(game, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<>();
        params.put("Authorization", "Bearer " + token);
        params.put("Content-Type", "application/json");
        return params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return update.toJSON().toString().getBytes();
        } catch (JSONException e) {
            Log.i(TAG, "Failed to encode JSON: " + e.getMessage());
            throw new AuthFailureError();
        }
    }

    @Override
    protected void deliverResponse(Game response) {
        listener.onResponse(response);
    }
}
