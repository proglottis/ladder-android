package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.proglottis.ladders.data.Tournament;
import com.github.proglottis.ladders.requests.TournamentListRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TournamentListActivity extends AppCompatActivity implements Response.Listener<Tournament[]>, Response.ErrorListener,TournamentListAdapter.OnItemSelectedListener {
    private static final String TAG = TournamentListActivity.class.getSimpleName();
    private Tournament[] tournaments;

    @Bind(R.id.progress) View progressBar;
    @Bind(R.id.content) View content;
    @Bind(R.id.recycler) RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_list);
        ButterKnife.bind(this);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        makeRequest();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    private void makeRequest() {
        showProgressBar();
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.api_token), null);
        TournamentListRequest req = new TournamentListRequest(token, this, this);
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    @Override
    public void onResponse(Tournament[] tournaments) {
        hideProgressBar();
        this.tournaments = tournaments;
        TournamentListAdapter adapter = new TournamentListAdapter(TournamentListActivity.this, tournaments, this);
        recycler.setAdapter(adapter);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressBar();
        Snackbar.make(content, R.string.request_failed, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeRequest();
                    }
                })
                .show();
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

    @Override
    public void onItemSelected(int position) {
        if(tournaments == null) {
            return;
        }
        Intent intent = new Intent(this, TournamentActivity.class);
        intent.putExtra(TournamentActivity.TOURNAMENT_ID, tournaments[position].getId());
        intent.putExtra(TournamentActivity.TOURNAMENT_NAME, tournaments[position].getName());
        startActivity(intent);
    }
}
