package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button googleBtn = (Button) findViewById(R.id.google_btn);
        googleBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        switch(view.getId()){
        case R.id.google_btn:
            startGoogleAuth();
            break;
        }
    }

    private void startGoogleAuth() {
        String state = UUID.randomUUID().toString();
        String url = getString(R.string.google_base_uri)
                + "?access_type=offline"
                + "&client_id="
                + getString(R.string.google_client_id)
                + "&redirect_uri="
                + getString(R.string.google_redirect_uri)
                + "&response_type=code"
                + "&scope="
                + getString(R.string.google_scope)
                + "&state="
                + state;

        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(AuthActivity.EXTRA_URL, url);
        intent.putExtra(AuthActivity.EXTRA_STATE, state);
        startActivity(intent);
    }
}
