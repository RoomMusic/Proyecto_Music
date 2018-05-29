package com.example.vidiic.proyecto_music.classes;

import android.media.Image;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Publicacion {


    private static boolean publication_is_added;
    private static List<Publicacion> publicaciones_list = new ArrayList<>();
    private static Publicacion publicacion = new Publicacion();

    private String publication_id;
    private Date pulication_date;
    private UserApp publication_user;
    private Song publication_song;
    private Image publication_image;


    public Publicacion(){}

    public Publicacion(String publication_id, Date pulication_date, UserApp publication_user, Song publication_song) {
        this.publication_id = publication_id;
        this.pulication_date = pulication_date;
        this.publication_user = publication_user;
        this.publication_song = publication_song;
    }

    public static boolean addPublicationToFirebase(FirebaseFirestore firebaseFirestore, Publicacion publicacion){

        firebaseFirestore.collection("publicaciones").document(publicacion.getPublication_id()).set(publicacion)
                .addOnSuccessListener(aVoid -> publication_is_added = true)
                .addOnFailureListener(e ->publication_is_added = false);

        return publication_is_added;
    }



    public static List<Publicacion> getAllPublicaciones(FirebaseFirestore firebaseFirestore){


        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("publicaciones").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (DocumentSnapshot snapshot : task.getResult()){
                    publicacion = snapshot.toObject(Publicacion.class);
                    publicaciones_list.add(publicacion);
                    Log.d("publicacion", "publication user and song " + publicacion.getPublication_user().getUserName() + " and " + publicacion.getPublication_song().getName());
                }
            }
        });



        return publicaciones_list;
    }


    public String getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(String publication_id) {
        this.publication_id = publication_id;
    }

    public Date getPulication_date() {
        return pulication_date;
    }

    public void setPulication_date(Date pulication_date) {
        this.pulication_date = pulication_date;
    }

    public UserApp getPublication_user() {
        return publication_user;
    }

    public void setPublication_user(UserApp publication_user) {
        this.publication_user = publication_user;
    }

    public Song getPublication_song() {
        return publication_song;
    }

    public void setPublication_song(Song publication_song) {
        this.publication_song = publication_song;
    }

    public Image getPublication_image() {
        return publication_image;
    }

    public void setPublication_image(Image publication_image) {
        this.publication_image = publication_image;
    }
}
