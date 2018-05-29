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

    private int idsong;
    private String name;
    private String imageSong;
    private List<Artist> artistList;


    public Song(){}

    public Song( String name, String imageSong, List<Artist> artistList) {
        this.idsong = ++id;
        this.name = name;
        this.imageSong = imageSong;
        this.artistList = artistList;
    }

    public int getIdsong() {
        return idsong;
    }

    public void setIdsong(int idsong) {
        this.idsong = idsong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageSong() {
        return imageSong;

    }

    public void setImageSong(String imageSong) {
        this.imageSong = imageSong;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public String nameOfArtists(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Artist artist: this.artistList){
            stringBuilder.append(artist.getName()+" ");
        }
        return stringBuilder.toString();
    }
}
