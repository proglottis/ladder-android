package com.github.proglottis.ladders;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Game;
import com.github.proglottis.ladders.data.Rank;
import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by james on 18/09/15.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private static final int FOOTER_VIEW = 1;
    private static final int RANK_VIEW = 2;
    private final Game game;
    private final GameInteractionListener listener;
    private final String currentUserId;

    public GameAdapter(Game game, String currentUserId, GameInteractionListener listener) {
        this.game = game;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View itemView;
        switch (viewType) {
            case RANK_VIEW:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_list_item, parent, false);
                holder = new RankViewHolder(itemView);
                break;
            case FOOTER_VIEW:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_footer, parent, false);
                holder = new FooterViewHolder(itemView, currentUserId, listener);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(game, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= game.getRanks().length) {
            return FOOTER_VIEW;
        } else {
            return RANK_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return game.getRanks().length + 1;
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindView(final Game game, int position);
    }

    public static class RankViewHolder extends GameAdapter.ViewHolder {
        @Bind(R.id.image) ImageView image;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.place) TextView place;
        @Bind(R.id.confirmed) TextView confirmed;

        public RankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Game game, int position) {
            Rank rank = game.getRanks()[position];
            Picasso.with(itemView.getContext()).load(rank.getPlayer().getUser().getImageURL()).into(image);
            name.setText(rank.getPlayer().getUser().getName());
            place.setText(String.valueOf(rank.getPosition()));
            if (rank.getConfirmedAt() != null) {
                String confirmedAgo = "Confirmed ";
                confirmedAgo += DateUtils.getRelativeTimeSpanString(rank.getConfirmedAt().getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
                confirmed.setText(confirmedAgo);
            } else {
                confirmed.setText(null);
            }
        }
    }

    public static class FooterViewHolder extends GameAdapter.ViewHolder {
        @Bind(R.id.confirm_btn) Button confirmBtn;

        private String currentUserId;
        private final GameInteractionListener listener;

        public FooterViewHolder(View itemView, String currentUserId, GameInteractionListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.currentUserId = currentUserId;
            this.listener = listener;
        }

        public void bindView(final Game game, int position) {
            if (game.isConfirmed(currentUserId)) {
                confirmBtn.setVisibility(View.GONE);
                confirmBtn.setOnClickListener(null);
            } else {
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onConfirmGameClick(game);
                    }
                });
            }
        }
    }

    public interface GameInteractionListener {
        void onConfirmGameClick(final Game game);
    }
}
