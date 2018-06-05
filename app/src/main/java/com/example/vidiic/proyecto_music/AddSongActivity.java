package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.adapters.AdapterAddSong;
import com.example.vidiic.proyecto_music.adapters.AdapterSongsArtist;
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

public class AddSongActivity extends AppCompatActivity {

    List<Song> songList;
    RecyclerView recyclerViewSong;
    AdapterAddSong adapterAddSong;
    FirebaseFirestore database;
    public String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        recyclerViewSong = findViewById(R.id.ryclersongadd);
        songList = new ArrayList<>();

        Intent intent = getIntent();
        String name = intent.getExtras().getString("nameArtist");

        database = FirebaseFirestore.getInstance();

        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerViewSong = findViewById(R.id.ryclersongadd);
        recyclerViewSong.setHasFixedSize(true);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(AddSongActivity.this));

        loadDataFromFireBase(name);

    }
    private void loadDataFromFireBase(String nameArtist) {
        if (songList.size() > 0) {
            songList.clear();
        }
        database.collection("users").document(idUser).collection("songlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Song song = documentSnapshot.toObject(Song.class);
                            for (Artist artist : song.getArtistList()) {
                                if (!(artist.getName().equals(nameArtist))) {
                                    songList.add(song);
                                    break;
                                }
                            }
                        }
                        adapterAddSong = new AdapterAddSong( songList,nameArtist);
                        recyclerViewSong.setAdapter(adapterAddSong);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSongActivity.this, "ERROR LOAD MUSIC", Toast.LENGTH_SHORT).show();
                        Log.v("ERROR LOAD MUSIC", e.getMessage());
                    }
                });
    }
}
