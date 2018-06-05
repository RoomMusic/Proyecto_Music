package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.Artist_Activity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.player.Player_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by Vidiic on 26/05/2018.
 */

public class AdapterSongGenre extends RecyclerView.Adapter<AdapterSongGenre.MyViewHolder>  implements View.OnClickListener {

    private Context myContext;
    private List<Song> songList;
    View.OnClickListener listener;

    public AdapterSongGenre(Context myContext, List<Song> songList) {
        this.myContext = myContext;
        this.songList = songList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        view = layoutInflater.inflate(R.layout.viewholder_genresonglist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.imagen.setImageResource(R.drawable.ic_action_music);
        holder.nombreSong.setText(songList.get(position).getName());
        holder.artistaSong.setText(songList.get(position).nameOfArtists());

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagen;
        public TextView nombreSong;
        public TextView artistaSong;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.imageSong);
            nombreSong = itemView.findViewById(R.id.nameSong);
            artistaSong = itemView.findViewById(R.id.nameArtist);
        }
    }
}
