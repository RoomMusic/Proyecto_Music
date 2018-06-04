package com.example.vidiic.proyecto_music.classes;

import android.util.Log;

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

    public Song() {
    }

    public Song(String name, String imageSong, List<Artist> artistList) {
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

    public static String splitImageSong(String song_path){

        String image_name_with_extension;

        String name_array[] = song_path.split("/");

        int last_item = name_array.length - 1;

        image_name_with_extension = name_array[last_item];

        return image_name_with_extension;
    }

    public String nameOfArtists() {
        StringBuilder sb = new StringBuilder();

        for (Artist artist : this.artistList) {
            Log.d("Yandel", artist.getName());
            sb.append(artist.getName() + "-");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
