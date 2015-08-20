package com.github.proglottis.ladders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Tournament;

public class NewGameActivity extends AppCompatActivity {
    private static final String TAG = NewGameActivity.class.getSimpleName();

    public static final String TOURNAMENT_EXTRA = "com.github.proglottis.ladders.tournament_extra";
    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        tournament = getIntent().getParcelableExtra(TOURNAMENT_EXTRA);

        TextView text = (TextView) findViewById(R.id.text);
        Log.d(TAG, "Tournament Name: " + tournament.getName());
        Log.d(TAG, "Tournament Player: " + tournament.getPlayers()[0].getId());
        text.setText(tournament.getPlayers()[0].getUser().getName());
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
}
