package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.adapters.AdapterArtistGenre;
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

public class Genre_Activity extends AppCompatActivity {

    private TextView name;
    private ImageView img;

    List<Artist> artistList;
    List<Song> songList;
    RecyclerView recyclerViewSong;
    RecyclerView recyclerViewArtist;
    AdapterArtistGenre adapterArtistGenre;
    FirebaseFirestore database;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_);

        name = findViewById(R.id.nameOneGenre);
        img = findViewById(R.id.imagenOneGenre);

        Intent intent = getIntent();
        String genreArtist = intent.getExtras().getString("NameGenre");
        int imgArtist = intent.getExtras().getInt("ImageGenre");

        name.setText(genreArtist);
        img.setImageResource(imgArtist);

        artistList = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        recyclerViewArtist = findViewById(R.id.recyclerartistsOneGenre);
        recyclerViewArtist.setHasFixedSize(true);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(Genre_Activity.this));

        Log.e("heyy","aaa");

//        recyclerViewSong = findViewById(R.id.recyclerSongsArtists);
//        recyclerViewSong.setHasFixedSize(true);
//        recyclerViewSong.setLayoutManager(new LinearLayoutManager(Genre_Activity.this));
        Log.d("heyy",genreArtist);
        loadArtistsFromFireBase(genreArtist);

    }
    private void loadArtistsFromFireBase(String nameArtist) {
        if (artistList.size()>0){
            artistList.clear();
        }
        Log.e("heyy","aaa");
        database.collection("users").document(idUser).collection("artistlist")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Artist artist = documentSnapshot.toObject(Artist.class);
                            if (artist.getGenre().toUpperCase().equals(nameArtist.toUpperCase())){
                                artistList.add(artist);
                                Log.e("heyy",artist.getGenre());
                            }
                    }
                    adapterArtistGenre = new AdapterArtistGenre(Genre_Activity.this,artistList);
                    recyclerViewArtist.setAdapter(adapterArtistGenre);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Genre_Activity.this,"ERROR LOAD MUSIC",Toast.LENGTH_SHORT).show();
                    Log.v("ERROR LOAD MUSIC",e.getMessage());
                }
            });
    }
}
