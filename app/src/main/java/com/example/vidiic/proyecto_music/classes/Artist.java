package com.example.vidiic.proyecto_music.classes;

import android.graphics.Bitmap;

/**
 * Created by Vidiic on 26/05/2018.
 */

public class Artist {

    private String name;
    private String image;
    private String category;
    private String description;

    public Artist(String name, String image, String category, String description) {
        this.name = name;
        this.image = image;
        this.category = category;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
