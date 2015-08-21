package com.github.proglottis.ladders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Player;
import com.squareup.picasso.Picasso;

/**
 * Created by james on 20/08/15.
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
    private static final String TAG = PlayerListAdapter.class.getSimpleName();
    private final Context context;
    private final String currentUserId;
    private Player[] players;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView position;
        public TextView name;
        public ImageView image;
        public TextView streak;
        public ViewHolder(View v) {
            super(v);
            position = (TextView) v.findViewById(R.id.position);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
            streak = (TextView) v.findViewById(R.id.streak);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlayerListAdapter(Context context, String currentUserId, Player[] players) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.players = players;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(players[position].getPosition() != null) {
            holder.position.setText(String.valueOf(players[position].getPosition()));
        } else {
            holder.position.setText("");
        }
        Picasso.with(context).load(players[position].getUser().getImageURL()).into(holder.image);

        if(players[position].getUser().getId().equals(currentUserId)) {
            holder.name.setText(players[position].getUser().getName() + " *");
        } else {
            holder.name.setText(players[position].getUser().getName());
        }

        if(players[position].getWinningStreakCount() >= 3) {
            holder.streak.setVisibility(View.VISIBLE);
            holder.streak.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lozenge_green));
            holder.streak.setText(String.valueOf(players[position].getWinningStreakCount()));
        } else if(players[position].getLosingStreakCount() >= 3) {
            holder.streak.setVisibility(View.VISIBLE);
            holder.streak.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lozenge_red));
            holder.streak.setText(String.valueOf(players[position].getLosingStreakCount()));
        } else {
            holder.streak.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.length;
    }
}