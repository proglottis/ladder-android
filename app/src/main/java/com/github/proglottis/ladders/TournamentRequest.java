package com.github.proglottis.ladders;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.proglottis.ladders.data.Tournament;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 19/08/15.
 */
public class TournamentRequest extends Request<Tournament> {

    private static final String BASE_URL = "http://ladders.pw/api/v1/tournaments/";
    private final Response.Listener<Tournament> listener;
    private final String token;

    public TournamentRequest(String id, String token, Response.Listener<Tournament> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, BASE_URL + id, errorListener);
        this.listener = listener;
        this.token = token;
    }

    @Override
    protected Response<Tournament> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            Tournament tournament = Tournament.fromJSON(json);
            return Response.success(tournament, HttpHeaderParser.parseCacheHeaders(response));
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
    protected void deliverResponse(Tournament response) {
        listener.onResponse(response);
    }
}
