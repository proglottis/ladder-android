package com.github.proglottis.ladders.requests;

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
 * Created by james on 17/09/15.
 */
public class GameRequest extends Request<Game> {
    private static final String TAG = GameRequest.class.getSimpleName();

    private static final String BASE_URL = "http://ladders.pw/api/v1/games/";
    private final Response.Listener<Game> listener;
    private final String token;

    public GameRequest(String gameId, String token, Response.Listener<Game> listener, Response.ErrorListener errorlistener) {
        super(Request.Method.GET, BASE_URL+gameId, errorlistener);
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
    protected void deliverResponse(Game response) {
        listener.onResponse(response);
    }
}
