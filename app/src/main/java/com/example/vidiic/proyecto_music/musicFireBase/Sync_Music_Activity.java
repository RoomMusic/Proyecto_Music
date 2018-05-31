package com.example.vidiic.proyecto_music.musicFireBase;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.MainActivity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.asynctasks.AsyncTaskSong;
import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sync_Music_Activity extends AppCompatActivity implements AsyncTaskSong.WeakReference {

    FirebaseFirestore db;
    List<Song> music;
    List<Artist> listaArtistas;
    List<Artist> artistasUsuario;
    List<Song> cancionesArtistas;

    public static final String idUser ="pRwOSof611Uw8Xluuy1ntvptYC73";
    private static final int MY_PERMISSION_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync__music_);


        if(ContextCompat.checkSelfPermission(Sync_Music_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Sync_Music_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(Sync_Music_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(Sync_Music_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }else {
            music = new ArrayList<>();
            listaArtistas = new ArrayList<>();
            artistasUsuario = new ArrayList<>();
            cancionesArtistas = new ArrayList<>();
            db = FirebaseFirestore.getInstance();
            new AsyncTaskSong(this).execute();

        }

    }

    @Override
    public Context getContext() {
        return Sync_Music_Activity.this;
    }

    @Override
    public void finished(List<Song> list) {
        music = list;
        loadArtistFromFireBase();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(Sync_Music_Activity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"PermissionGranted",Toast.LENGTH_SHORT).show();

                        db = FirebaseFirestore.getInstance();
                        new AsyncTaskSong(this).execute();

                    }
                }else {
                    Toast.makeText(this,"PermissionDeneged",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    public List<Artist> checkList(String nameSong, List<Artist> artistasExistentes){
        Log.d("Artistas", artistasExistentes.size()+"");
        if (artistasExistentes.size() >= 0){
            for (Artist artist : artistasExistentes){
                artistasUsuario.add(artist);
            }
        }
        for (Artist artist: listaArtistas){
            if (nameSong.contains(artist.getName().toUpperCase())){
                artistasUsuario.add(artist);
            }
        }
        return artistasUsuario;
    }
    public List<Song> checkArtist(String nameArtist, List<Song> music){
        Log.d("Song", music.size()+"");
            for (Song song: music){
                if (song.getName().toUpperCase().contains(nameArtist.toUpperCase())){
                    cancionesArtistas.add(song);
                }
            }
        return cancionesArtistas;
    }
    public void loadArtistFromFireBase(){
        if (listaArtistas.size()>0){
            listaArtistas.clear();
        }
        if (artistasUsuario.size()>0){
            artistasUsuario.clear();
        }
        setUpFireBase();
        db.collection("artist")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot: task.getResult()){
                        Artist artist = new Artist(documentSnapshot.getString("name"),
                            "hey",
                            documentSnapshot.getString("genre"),
                            documentSnapshot.getString("description"),documentSnapshot.getString("age"));
                        Log.d("Yandel",artist.getName());
                        listaArtistas.add(artist);
                    }
                    int fin = 1;
                    for (Song song: music) {
                        song.setArtistList(checkList(song.getName().toUpperCase(),song.getArtistList()));
                        db.collection("users").document(idUser).collection("songlist").document("Song-" + song.getIdsong()).set(song);
                        for (Artist artist: artistasUsuario){
                            db.collection("users").document(idUser).collection("artistlist").document(artist.getName()).set(artist);
                            cancionesArtistas.clear();
                        }
                        artistasUsuario.clear();
                        fin++;
                        if (fin == 10) {
                            break;
                        }
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v("ERROR LOAD ARTIST",e.getMessage());
                }
            });
    }
    private void setUpFireBase() {
        db = FirebaseFirestore.getInstance();
    }
}
