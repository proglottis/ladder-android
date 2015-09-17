package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.proglottis.ladders.data.Game;
import com.github.proglottis.ladders.requests.GameRequest;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by james on 17/09/15.
 */
public class GameActivity extends AppCompatActivity implements Response.Listener<Game>, Response.ErrorListener {
    private static final String TAG = GameActivity.class.getSimpleName();
    public static final String GAME_ID = "GAME_ID";

    @Bind(R.id.layout) ViewGroup layout;
    @Bind(R.id.progress) View progressBar;
    @Bind(R.id.ranks) RecyclerView rankList;

    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        rankList.setLayoutManager(new LinearLayoutManager(this));

        Intent data = getIntent();
        gameId = data.getStringExtra(GAME_ID);

        requestGame();
    }

    private void requestGame() {
        showProgressBar();
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
        GameRequest req = new GameRequest(gameId, token, this, this);
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    @Override
    public void onResponse(Game game) {
        hideProgressBar();
        setTitle(game.title());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressBar();
        Snackbar.make(layout, R.string.request_failed, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestGame();
                    }
                })
                .show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
