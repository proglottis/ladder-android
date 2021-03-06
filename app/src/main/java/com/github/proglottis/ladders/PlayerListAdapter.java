package com.github.proglottis.ladders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Player;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        @Bind(R.id.position) TextView position;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.image) ImageView image;
        @Bind(R.id.streak) TextView streak;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlayerListAdapter(Context context, Player[] players, String currentUserId) {
        this.context = context;
        this.players = players;
        this.currentUserId = currentUserId;
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
        Player player = players[position];
        if(player.getPosition() != null) {
            holder.position.setText(String.valueOf(player.getPosition()));
        } else {
            holder.position.setText("");
        }
        Picasso.with(context).load(player.getUser().getImageURL()).into(holder.image);

        if(player.getUser().getId().equals(currentUserId)) {
            holder.name.setTypeface(null, Typeface.BOLD);
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL);
        }
        holder.name.setText(player.getUser().getName());

        if(player.hasStreak()) {
            holder.streak.setVisibility(View.VISIBLE);
            holder.streak.setText(String.valueOf(player.getStreakCount()));
            if(player.hasWinningStreak()) {
                holder.streak.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lozenge_green));
            } else {
                holder.streak.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.lozenge_red));
            }
        } else {
            holder.streak.setVisibility(View.INVISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.length;
    }
}