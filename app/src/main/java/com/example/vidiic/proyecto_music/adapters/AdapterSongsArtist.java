package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.Artist_Activity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;

import java.util.List;

public class AdapterSongsArtist extends RecyclerView.Adapter<AdapterSongsArtist.MyViewHolder>{
    private Context myContext;
    private List<Song> songsList;

    public AdapterSongsArtist(Context myContext, List<Song> songsList) {
        this.myContext = myContext;
        this.songsList = songsList;
    }

    @Override
    public AdapterSongsArtist.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_songlist,parent,false);

        return new AdapterSongsArtist.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(AdapterSongsArtist.MyViewHolder holder, int position) {

        holder.imagen.setImageResource(R.drawable.ic_action_music);
        holder.nombreSong.setText(songsList.get(position).getName());

    }
    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imagen;
        public TextView nombreSong;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageSong);
            nombreSong = itemView.findViewById(R.id.nameSong);

        }
    }
}
