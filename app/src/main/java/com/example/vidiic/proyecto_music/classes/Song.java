package com.example.vidiic.proyecto_music.classes;

import android.graphics.Bitmap;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class Song {
    private static int id = 1;
    private int idsong;
    private String name;
    private String artist;
    private String imageSong;

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

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", imageSong=" + imageSong +
                '}';
    }
}
