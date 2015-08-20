package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.github.proglottis.ladders.data.Player;
import com.github.proglottis.ladders.data.Tournament;

import java.util.Arrays;
import java.util.Comparator;

public class TournamentActivity extends AppCompatActivity implements Response.Listener<Tournament>, View.OnClickListener {
    private static final String TAG = TournamentActivity.class.getSimpleName();
    public static final String TOURNAMENT_ID = "com.github.proglottis.ladders.tournament_id";
    public static final String TOURNAMENT_NAME = "com.github.proglottis.ladders.tournament_name";
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

        Intent intent = getIntent();
        tournament = new Tournament();
        tournament.setId(intent.getStringExtra(TOURNAMENT_ID));
        tournament.setName(intent.getStringExtra(TOURNAMENT_NAME));
        updateView();

        makeRequest(tournament.getId());
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
        if(tournament.getPlayers() == null) {
            return;
        }
        Player[] players = tournament.getPlayers();
        Arrays.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getPosition() == null) {
                    return 1;
                } else if (p2.getPosition() == null) {
                    return -1;
                } else {
                    return p1.getPosition().compareTo(p2.getPosition());
                }
            }
        });
        playerList.setAdapter(new PlayerListAdapter(this, players));
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
            startActivity(intent);
            break;
        }
    }
}
