package com.github.proglottis.ladders.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.Game;
import com.github.proglottis.ladders.data.Tournament;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 21/08/15.
 */
public class CreateGameRequest extends Request<Game> {
    private static final String TAG = CreateGameRequest.class.getSimpleName();

    private static final String BASE_URL = "http://ladders.pw/api/v1/games";
    private final Response.Listener<Game> listener;
    private final String token;
    private final Game game;

    public CreateGameRequest(Game game, String token, Response.Listener<Game> listener, Response.ErrorListener errorlistener) {
        super(Request.Method.POST, BASE_URL, errorlistener);
        this.game = game;
        this.token = token;
        this.listener = listener;
    }

    @Override
    protected Response<Game> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            Game game = Game.fromJSON(json);
            return Response.success(game, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
            //return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + token);
        params.put("Content-Type", "application/json");
        return params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return game.toJSON().toString().getBytes();
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
