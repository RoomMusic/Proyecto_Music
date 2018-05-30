package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.adapters.AdapterArtist;
import com.example.vidiic.proyecto_music.adapters.AdapterSong;
import com.example.vidiic.proyecto_music.adapters.AdapterSongsArtist;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Artist_Activity extends AppCompatActivity {

    private TextView name,desc,cat;
    private ImageView img;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";

    List<Song> songList;
    List<Song> songListUser;
    RecyclerView recyclerViewSong;
    AdapterSongsArtist adapterSongsArtist;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_);

        name = findViewById(R.id.artist_name);
        desc = findViewById(R.id.artist_desc);
        cat = findViewById(R.id.artist_cat);
        img = findViewById(R.id.artist_image);

        Intent intent = getIntent();
        String nameArtist = intent.getExtras().getString("NameArtist");
        String descArtist = intent.getExtras().getString("DescriptionArtist");
        String catArtist = intent.getExtras().getString("GenereArtist");
        String age = intent.getExtras().getString("age");
        int imgArtist = intent.getExtras().getInt("ImageArtist");

        name.setText(nameArtist);
        desc.setText(descArtist);
        cat.setText(catArtist);
        img.setImageResource(imgArtist);

        songList = new ArrayList<>();
        songListUser = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        recyclerViewSong = findViewById(R.id.recyclerSongsArtists);
        recyclerViewSong.setHasFixedSize(true);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(Artist_Activity.this));

        loadDataFromFireBase(nameArtist);

    }
    private void loadDataFromFireBase(String nameArtist) {
        if (songList.size()>0){
            songList.clear();
        }
        database.collection("users").document(idUser).collection("songlist")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Song song = documentSnapshot.toObject(Song.class);
                        for (Artist artist :song.getArtistList()){
                            if (artist.getName().equals(nameArtist)){
                                songListUser.add(song);
                            }
                        }
                    }
                    adapterSongsArtist = new AdapterSongsArtist(Artist_Activity.this,songListUser);
                    recyclerViewSong.setAdapter(adapterSongsArtist);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Artist_Activity.this,"ERROR LOAD MUSIC",Toast.LENGTH_SHORT).show();
                    Log.v("ERROR LOAD MUSIC",e.getMessage());
                }
            });
    }

}
