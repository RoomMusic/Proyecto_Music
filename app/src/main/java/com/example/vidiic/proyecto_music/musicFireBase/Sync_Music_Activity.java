package com.example.vidiic.proyecto_music.musicFireBase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.asynctasks.AsyncTaskSong;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sync_Music_Activity extends AppCompatActivity implements AsyncTaskSong.WeakReference {

    FirebaseFirestore db;
    Map<String,Song> Songs;
    public static final String idUser ="gp2V0KkaEwOQ2ukVGUkaGv7ywFr1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync__music_);

        db = FirebaseFirestore.getInstance();
        new AsyncTaskSong(this).execute();
        Songs = new HashMap<>();

    }

    @Override
    public Context getContext() {
        return Sync_Music_Activity.this;
    }

    @Override
    public void finished(List<Song> list) {
        for(Song song: list){
            Songs.put(song.getName(),song);
        }
        db.collection("users").document(idUser).collection("music").document("songlist")
                .set(Songs)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Sync_Music_Activity.this,"Added Songs",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sync_Music_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
