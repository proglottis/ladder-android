package com.github.proglottis.ladders;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.github.proglottis.ladders.data.Tournament;

public class TournamentListActivity extends ListActivity implements Response.Listener<Tournament[]>{
    private static final String TAG = TournamentListActivity.class.getSimpleName();
    private Tournament[] tournaments;
    private View progressBar;
    private View content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_list);

        progressBar = findViewById(R.id.progress);
        content = findViewById(R.id.content);

        makeRequest();
    }

    private void showContent() {
        progressBar.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    private void makeRequest() {
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
        TournamentListRequest req = new TournamentListRequest(token, this,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    VolleyLog.d(TAG, "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    @Override
    public void onResponse(Tournament[] tournaments) {
        this.tournaments = tournaments;
        showContent();
        ListAdapter adapter = new TournamentListAdapter(TournamentListActivity.this, tournaments);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(tournaments == null) {
            return;
        }
        Intent intent = new Intent(this, TournamentActivity.class);
        intent.putExtra(TournamentActivity.TOURNAMENT_ID, tournaments[position].getId());
        intent.putExtra(TournamentActivity.TOURNAMENT_NAME, tournaments[position].getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
