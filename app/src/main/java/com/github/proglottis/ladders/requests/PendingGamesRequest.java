package com.github.proglottis.ladders.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.Game;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steventan on 18/09/15.
 */
public class PendingGamesRequest extends Request<List<Game>> {

    private static final String BASE_URL = "http://ladders.pw/api/v1/games";
    private String token;
    private Listener listener;


    public PendingGamesRequest(String token, Listener listener) {
        super(Method.GET, BASE_URL, null);
        this.token = token;
        this.listener = listener;
    }

    @Override
    protected Response<List<Game>> parseNetworkResponse(NetworkResponse response) {
        try {
            List<Game> games = new ArrayList<>();
            JSONArray json = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            for(int i = 0; i < json.length(); i++) {
                games.add(Game.fromJSON(json.getJSONObject(i)));
            }
            return Response.success(games, HttpHeaderParser.parseCacheHeaders(response));
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
    protected void deliverResponse(List<Game> response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        listener.onPendingGamesError(error);
    }

    public interface Listener extends Response.ErrorListener{
        void onResponse(List<Game> response);

        void onPendingGamesError(VolleyError error);
    }
}
