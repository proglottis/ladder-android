package com.github.proglottis.ladders;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.github.proglottis.ladders.data.Game;
import com.github.proglottis.ladders.data.Player;
import com.github.proglottis.ladders.data.Rank;
import com.github.proglottis.ladders.data.Token;
import com.github.proglottis.ladders.data.Tournament;
import com.github.proglottis.ladders.requests.CreateGameRequest;

import org.json.JSONException;

import java.util.Arrays;

public class NewGameActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<Game> {
    private static final String TAG = NewGameActivity.class.getSimpleName();

    public static final String TOURNAMENT_EXTRA = "com.github.proglottis.ladders.tournament_extra";
    private Tournament tournament;
    private Spinner player1Spin;
    private Spinner player2Spin;
    private Button createBtn;
    private Player[] players;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        tournament = getIntent().getParcelableExtra(TOURNAMENT_EXTRA);

        player1Spin = (Spinner) findViewById(R.id.player1_spin);
        player2Spin = (Spinner) findViewById(R.id.player2_spin);
        createBtn = (Button) findViewById(R.id.create_game);
        createBtn.setOnClickListener(this);


        players = tournament.getPlayers();
        Arrays.sort(players, Player.BY_NAME);
        token = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
        String currentUserId;
        try {
            currentUserId = Token.decode(token).getUserId();
        } catch (JSONException e) {
            currentUserId = null;
        }
        int playerPosition = 0;
        for(int i = 0; i < players.length; i++) {
            if(players[i].getUser().getId().equals(currentUserId)) {
                playerPosition = i;
                break;
            }
        }
        player1Spin.setAdapter(new PlayerSpinnerAdapter(this, players, currentUserId));
        player2Spin.setAdapter(new PlayerSpinnerAdapter(this, players, currentUserId));
        player1Spin.setSelection(playerPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_game:
                createGame();
                break;
        }
    }

    private void createGame() {
        createBtn.setEnabled(false);

        Rank[] ranks = new Rank[]{
                new Rank(players[player1Spin.getSelectedItemPosition()].getId(), 1),
                new Rank(players[player2Spin.getSelectedItemPosition()].getId(), 2),
        };
        Game g = new Game(tournament.getId(), ranks);

        CreateGameRequest req = new CreateGameRequest(g, token, this, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error);
                Toast.makeText(NewGameActivity.this, getString(R.string.request_failed), Toast.LENGTH_SHORT).show();
                createBtn.setEnabled(true);
            }
        });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    @Override
    public void onResponse(Game response) {
        setResult(RESULT_OK);
        finish();
    }
}
