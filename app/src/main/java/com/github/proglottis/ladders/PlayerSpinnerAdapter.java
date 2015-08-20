package com.github.proglottis.ladders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Player;

/**
 * Created by james on 21/08/15.
 */
public class PlayerSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private final Player[] players;

    public PlayerSpinnerAdapter(Context context, Player[] players) {
        this.context = context;
        this.players = players;
    }

    @Override
    public int getCount() {
        return players.length;
    }

    @Override
    public Object getItem(int position) {
        return players[position];
    }

    @Override
    public long getItemId(int position) {
        return players[position].getId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) View.inflate(context, android.R.layout.simple_list_item_1, null);
        view.setText(players[position].getUser().getName());
        return view;
    }
}
