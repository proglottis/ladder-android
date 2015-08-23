package com.github.proglottis.ladders.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.Tournament;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 19/08/15.
 */
public class TournamentListRequest extends Request<Tournament[]> {
    private static final String TOURNAMENTS_URL = "http://ladders.pw/api/v1/tournaments";
    private final Response.Listener<Tournament[]> listener;
    private final String token;

    public TournamentListRequest(String token, Response.Listener<Tournament[]> listener, Response.ErrorListener errorListener) {
        super(Method.GET, TOURNAMENTS_URL, errorListener);
        this.token = token;
        this.listener = listener;
    }

    @Override
    protected Response<Tournament[]> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONArray json = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            Tournament[] tournaments = new Tournament[json.length()];
            for(int i = 0; i < json.length(); i++) {
                tournaments[i] = Tournament.fromJSON(json.getJSONObject(i));
            }
            Arrays.sort(tournaments, Tournament.BY_NAME);
            return Response.success(tournaments, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + token);
        return params;
    }

    @Override
    protected void deliverResponse(Tournament[] response) {
        listener.onResponse(response);
    }
}
