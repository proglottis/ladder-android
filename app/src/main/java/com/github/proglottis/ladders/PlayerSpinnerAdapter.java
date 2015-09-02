package com.github.proglottis.ladders;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Player;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by james on 21/08/15.
 */
public class PlayerSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private final Player[] players;
    private final String currentUserId;

    public PlayerSpinnerAdapter(Context context, Player[] players, String currentUserId) {
        this.context = context;
        this.players = players;
        this.currentUserId = currentUserId;
    }

    static class ViewHolder {
        @Bind(R.id.image) ImageView image;
        @Bind(R.id.name) TextView name;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Player player = players[position];
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.player_spinner_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(player.getUser().getImageURL()).into(holder.image);

        if(player.getUser().getId().equals(currentUserId)) {
            holder.name.setTypeface(null, Typeface.BOLD);
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL);
        }
        holder.name.setText(player.getUser().getName());
        return convertView;
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
}
