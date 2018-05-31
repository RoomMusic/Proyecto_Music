package com.example.vidiic.proyecto_music.classes;

public class Genre {

    private String name;
    private String image;

    public Genre(String name, String image) {
        this.name = name;
        this.image = image;
    }
    public Genre(){

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
}
