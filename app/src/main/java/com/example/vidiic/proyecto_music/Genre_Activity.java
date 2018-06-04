package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.adapters.AdapterArtistGenre;
import com.example.vidiic.proyecto_music.adapters.AdapterSong;
import com.example.vidiic.proyecto_music.adapters.AdapterSongGenre;
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

public class Genre_Activity extends AppCompatActivity {

    private TextView name;
    private ImageView img;

    List<Artist> artistList;
    List<Song> songList;
    RecyclerView recyclerViewSong;
    RecyclerView recyclerViewArtist;
    AdapterArtistGenre adapterArtistGenre;
    AdapterSongGenre adapterSongGenre;
    FirebaseFirestore database;
    Toolbar toolbar_genre;
    public String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_);

        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        name = findViewById(R.id.nameOneGenre);
        img = findViewById(R.id.imagenOneGenre);

        Intent intent = getIntent();
        String genreArtist = intent.getExtras().getString("NameGenre");
        int imgArtist = intent.getExtras().getInt("ImageGenre");

        name.setText(genreArtist);
        img.setImageResource(imgArtist);

        addToolbar(R.id.toolbar_genre, R.string.GenreTitle);

        artistList = new ArrayList<>();
        songList = new ArrayList<>();

        database = FirebaseFirestore.getInstance();

        recyclerViewArtist = findViewById(R.id.recyclerartistsOneGenre);
        recyclerViewArtist.setHasFixedSize(true);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(Genre_Activity.this));

        Log.e("heyy","aaa");

        recyclerViewSong = findViewById(R.id.recyclersongsOneGenre);
        recyclerViewSong.setHasFixedSize(true);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(Genre_Activity.this));

        Log.d("heyy",genreArtist);
        loadArtistsFromFireBase(genreArtist);
        loadMusicFromFireBase(genreArtist);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    private void addToolbar(int resource_id, int title) {

        Toolbar toolbar = findViewById(resource_id);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    private void loadMusicFromFireBase(String nameArtist) {
        if (songList.size()>0){
            songList.clear();
        }
        Log.e("heyy","aaa");
        database.collection("users").document(idUser).collection("songlist")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Song song = documentSnapshot.toObject(Song.class);
                        for (Artist artist :song.getArtistList()){
                            if (artist.getGenre().toUpperCase().equals(nameArtist.toUpperCase())){
                                songList.add(song);
                                Log.e("heyy",artist.getGenre());
                            }
                        }
                    }
                    adapterSongGenre = new AdapterSongGenre(Genre_Activity.this,songList);
                    recyclerViewSong.setAdapter(adapterSongGenre);
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
