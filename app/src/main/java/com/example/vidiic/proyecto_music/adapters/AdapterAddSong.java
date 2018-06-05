package com.example.vidiic.proyecto_music.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class AdapterAddSong extends RecyclerView.Adapter<AdapterAddSong.SongViewHolder> {

    List<Song> songsList;
    String nameArtist;

    public AdapterAddSong(List<Song> songsList,String nameArtist) {
        this.songsList = songsList;
        this.nameArtist = nameArtist;

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
        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Artist artistfinal = new Artist();
                FirebaseFirestore database  = FirebaseFirestore.getInstance();
                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database.collection("artist")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot documentSnapshot: task.getResult()){
                                    Artist artist = documentSnapshot.toObject(Artist.class);
                                    if (artist.getName().equals(nameArtist)){
                                        database.collection("users").document(idUser).collection("songlist")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for(DocumentSnapshot documentSnapshot: task.getResult()){
                                                            Song song = documentSnapshot.toObject(Song.class);
                                                            if (song.getName().equals(songsList.get(position).getName())){
                                                                List<Artist> artistList = song.getArtistList();
                                                                artistList.add(artist);
                                                                Log.e("addartistas",artist.getName());
                                                                song.setArtistList(artistList);
                                                                database.collection("users").document(idUser).collection("songlist").document("Song-"+song.getIdsong()).set(song);
                                                                holder.btnadd.setText("X");
                                                                holder.btnadd.setEnabled(false);
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"ERROR LOAD MUSIC",Toast.LENGTH_SHORT).show();
                                Log.v("ERROR LOAD MUSIC",e.getMessage());
                            }
                        });
                database.collection("users").document(idUser).collection("songlist")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot documentSnapshot: task.getResult()){
                                    Song song = documentSnapshot.toObject(Song.class);
                                    if (song.getName().equals(songsList.get(position).getName())){
                                        List<Artist> artistList = song.getArtistList();

                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(),"ERROR LOAD MUSIC",Toast.LENGTH_SHORT).show();
                                Log.v("ERROR LOAD MUSIC",e.getMessage());
                            }
                        });
            }
        });

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
            nombreSong = itemView.findViewById(R.id.addnamesong);
            btnadd = itemView.findViewById(R.id.chkaddsong);
        }
    }
}
