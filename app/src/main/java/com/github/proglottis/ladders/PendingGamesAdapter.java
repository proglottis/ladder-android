package com.github.proglottis.ladders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Game;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by steventan on 18/09/15.
 */
public class PendingGamesAdapter extends RecyclerView.Adapter<PendingGamesAdapter.ViewHolder> {

    private Context context;
    private List<Game> pendingGames;
    private OnPendingGameSelectedListener listener;

    public PendingGamesAdapter(Context context, List<Game> pendingGames,
                               OnPendingGameSelectedListener listener) {
        this.context = context;
        this.pendingGames = pendingGames;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_game_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Game game = pendingGames.get(position);
        holder.title.setText(game.getTournament().getName());
        holder.gameName.setText(game.title());

        holder.pendingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPendingGameSelected(game.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingGames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.pending_game) View pendingGame;
        @Bind(R.id.tournament_title) TextView title;
        @Bind(R.id.game_name) TextView gameName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnPendingGameSelectedListener {
        void onPendingGameSelected(String gameId);
    }
}
