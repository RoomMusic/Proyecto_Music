package com.example.vidiic.proyecto_music.adapters;


import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Song;


import java.util.List;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class AdapterAddSong extends RecyclerView.Adapter<AdapterAddSong.SongViewHolder> {

    List<Song> songsList;

    public AdapterAddSong(List<Song> songsList) {
        this.songsList = songsList;

    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.checksongs, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        holder.nombreSong.setText(songsList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {


        public TextView nombreSong;
        public Button btnadd;

        public SongViewHolder(View itemView) {
            super(itemView);

            nombreSong = itemView.findViewById(R.id.nameSong);
            btnadd = itemView.findViewById(R.id.chkaddsong);
        }
    }
}
