package com.example.vidiic.proyecto_music.classes;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Vidiic on 26/05/2018.
 */

public class Artist {

    private String name;
    private String image;
    private String genre;
    private String age;
    private String description;

    public Artist(String name, String image, String genre, String description,String age) {
        this.name = name;
        this.image = image;
        this.genre = genre;
        this.description = description;
        this.age = age;
    }
    public Artist(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }


}
