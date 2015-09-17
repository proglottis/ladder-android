package com.github.proglottis.ladders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by james on 17/09/15.
 */
public class GameActivity extends AppCompatActivity {
    private static final String TAG = GameActivity.class.getSimpleName();
    public static final String GAME_ID = "GAME_ID";

    @Bind(R.id.test)
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        Intent data = getIntent();

        test.setText(data.getStringExtra(GAME_ID));
    }
}
