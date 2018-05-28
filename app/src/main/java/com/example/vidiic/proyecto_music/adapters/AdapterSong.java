package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Song;

import java.util.List;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class AdapterSong extends RecyclerView.Adapter<AdapterSong.SongViewHolder> implements View.OnClickListener {

    List<Song> songsList;

    View.OnClickListener listener;

    public AdapterSong(List<Song> songsList) {
        this.songsList = songsList;

    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_songlist,parent,false);

        itemView.setOnClickListener(this);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        holder.imagen.setImageResource(R.drawable.ic_action_music);
        holder.nombreSong.setText(songsList.get(position).getName());
        holder.artistaSong.setText(songsList.get(position).nameOfArtists());

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class SongViewHolder extends RecyclerView.ViewHolder  {

        public ImageView imagen;
        public TextView nombreSong;
        public TextView artistaSong;

        public SongViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageSong);
            nombreSong = itemView.findViewById(R.id.nameSong);
            artistaSong = itemView.findViewById(R.id.nameArtist);
        }
    }
}
