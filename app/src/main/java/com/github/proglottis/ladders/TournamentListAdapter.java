package com.github.proglottis.ladders;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Tournament;

/**
 * Created by james on 19/08/15.
 */
public class TournamentListAdapter extends ArrayAdapter<Tournament> {
    private final Activity context;
    private final Tournament[] values;

    public TournamentListAdapter(Activity context, Tournament[] values) {
        super(context, R.layout.tournament_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.tournament_list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(values[position].getName());

        return convertView;
    }
}
