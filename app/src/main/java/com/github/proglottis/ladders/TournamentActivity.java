package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.github.proglottis.ladders.data.Player;
import com.github.proglottis.ladders.data.Token;
import com.github.proglottis.ladders.data.Tournament;
import com.github.proglottis.ladders.requests.TournamentRequest;

import org.json.JSONException;

import java.util.Arrays;

public class TournamentActivity extends AppCompatActivity implements Response.Listener<Tournament>, View.OnClickListener {
    private static final String TAG = TournamentActivity.class.getSimpleName();
    public static final String TOURNAMENT_ID = "com.github.proglottis.ladders.tournament_id";
    public static final String TOURNAMENT_NAME = "com.github.proglottis.ladders.tournament_name";
    public static final int NEW_GAME_REQUEST = 1000;
    private Tournament tournament;
    private View progressBar;
    private View content;
    private RecyclerView playerList;
    private FloatingActionButton newGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        progressBar = findViewById(R.id.progress);
        content = findViewById(R.id.content);
        playerList = (RecyclerView) findViewById(R.id.player_list);
        playerList.setLayoutManager(new LinearLayoutManager(this));
        newGameBtn = (FloatingActionButton) findViewById(R.id.new_game_btn);
        newGameBtn.setOnClickListener(this);

        tournament = new Tournament();

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            tournament.setId(intent.getStringExtra(TOURNAMENT_ID));
            tournament.setName(intent.getStringExtra(TOURNAMENT_NAME));
            Log.d(TAG, "INTENT TOURNAMENT ID: " + tournament.getId());
        } else {
            tournament.setId(savedInstanceState.getString(TOURNAMENT_ID));
            tournament.setName(savedInstanceState.getString(TOURNAMENT_NAME));
            Log.d(TAG, "BUNDLE TOURNAMENT ID: " + tournament.getId());
        }
        updateView();
        makeRequest(tournament.getId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "SAVING TOURNAMENT ID: " + tournament.getId());
        outState.putString(TOURNAMENT_ID, tournament.getId());
        outState.putString(TOURNAMENT_NAME, tournament.getName());
        super.onSaveInstanceState(outState);
    }

    private void makeRequest(String tournamentId) {
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
        TournamentRequest req = new TournamentRequest(tournamentId, token, this,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        showContent();
                        VolleyLog.d(TAG, "Error: " + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    public void onResponse(Tournament tournament) {
        this.tournament = tournament;
        showContent();
        updateView();
    }

    public void updateView() {
        setTitle(tournament.getName());
        if (tournament.getPlayers() == null) {
            return;
        }
        Player[] players = tournament.getPlayers();
        Arrays.sort(players, Player.BY_POSITION);
        String currentUserId;
        try {
            String rawToken = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
            currentUserId = Token.decode(rawToken).getUserId();
        } catch (JSONException e) {
            currentUserId = null;
        }
        playerList.setAdapter(new PlayerListAdapter(this, players, currentUserId));
    }

    public void showContent() {
        progressBar.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournament, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
        case R.id.new_game_btn:
            Intent intent = new Intent(this, NewGameActivity.class);
            intent.putExtra(NewGameActivity.TOURNAMENT_EXTRA, tournament);
            startActivityForResult(intent, NEW_GAME_REQUEST);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_GAME_REQUEST && resultCode == RESULT_OK) {
            Snackbar.make(content, R.string.game_created, Snackbar.LENGTH_LONG).show();
        }
    }
}
