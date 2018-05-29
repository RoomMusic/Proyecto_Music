package com.example.vidiic.proyecto_music.classes;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class Song {
    private static int id = 1;
    private static List<Song> song_list = new ArrayList<>();
    private static Song song;

    private int idsong;
    private String name;
    private String artist;
    private String imageSong;

    public Song(){}

    public Song(String name, String artist, String imageSong) {
        this.idsong  = ++id;
        this.name = name;
        this.artist = artist;
        this.imageSong = imageSong;
    }
    public int getId() {
        return idsong;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageSong() {
        return imageSong;

    }

    public static List<Song> getSongList(FirebaseFirestore firebaseFirestore,  String user_id){

        firebaseFirestore.collection("users").document(user_id).collection("songlist").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (DocumentSnapshot snapshot : task.getResult()){
                    song = snapshot.toObject(Song.class);
                    Log.d("getsong", "song name: " + song.getName());
                    song_list.add(song);
                }
            }
        });

        return song_list;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", imageSong=" + imageSong +
                '}';
    }
}
