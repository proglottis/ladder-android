package com.github.proglottis.ladders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.proglottis.ladders.data.Tournament;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by james on 19/08/15.
 */
public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListAdapter.ViewHolder> {
    private final Context context;
    private final Tournament[] values;
    private OnItemSelectedListener listener;

    public TournamentListAdapter(Context context, Tournament[] values, OnItemSelectedListener listener) {
        this.context = context;
        this.values = values;
        this.listener = listener;
    }


    @Override
    public TournamentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.tournament_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TournamentListAdapter.ViewHolder holder, final int position) {
        holder.name.setText(values[position].getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View root;
        @Bind(R.id.name) TextView name;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            root = v;
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
