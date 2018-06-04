package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.classes.Artist;
import com.example.vidiic.proyecto_music.classes.Genre;
import com.example.vidiic.proyecto_music.classes.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Form_Artist extends AppCompatActivity {

    EditText editname,editdesc,editage,editgen;
    Button button;
    FirebaseFirestore db;
    public static final String idUser = "pRwOSof611Uw8Xluuy1ntvptYC73";
    Boolean existgenre;
    Artist artistnew;
    Toolbar toolbar_form_artist;

    private void addToolbar(Toolbar toolbar, int resource_id, int title) {



        toolbar = findViewById(resource_id);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form__artist);

        editage = findViewById(R.id.formedad);
        editdesc =findViewById(R.id.formdes);
        editname = findViewById(R.id.formnom);
        editgen = findViewById(R.id.formgen);
        button = findViewById(R.id.btnsenartist);

        addToolbar(toolbar_form_artist, R.id.toolbar_form_artist, R.string.FormArtistAddArtist);

        existgenre = false;

        db = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editage.getText().toString() != "" && editname.getText().toString() != "" && editdesc.getText().toString() != ""){
                    artistnew = new Artist(editname.getText().toString(),
                            editname.getText().toString(),
                            editgen.getText().toString(),
                            editage.getText().toString(),
                            editdesc.getText().toString()
                    );
                    Toast.makeText(view.getContext(), "Se esta guardando el artista "+artistnew.getName(), Toast.LENGTH_LONG).show();
                    db.collection("artist")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        Artist artist = documentSnapshot.toObject(Artist.class);
                                        if (artist.getName().toUpperCase().equals(editname.getText().toString().toUpperCase())){
                                            db.collection("users").document(idUser).collection("artistlist").document(artistnew.getName()).set(artistnew);
                                            db.collection("artist").document(artistnew.getName().toUpperCase()).set(artistnew);
                                            Toast.makeText(view.getContext(),"Artista modificado",Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                    db.collection("artist").document(artistnew.getName().toUpperCase()).set(artistnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            db.collection("genre")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                                Genre genre = documentSnapshot.toObject(Genre.class);
                                                                if (genre.getName().toUpperCase().equals(artistnew.getGenre().toUpperCase())){
                                                                    existgenre = true;
                                                                }
                                                            }
                                                            db.collection("users").document(idUser).collection("artistlist").document(artistnew.getName()).set(artistnew);
                                                            Toast.makeText(view.getContext(),"Artista nuevo creado", Toast.LENGTH_SHORT).show();
                                                            if (!existgenre){
                                                                Toast.makeText(view.getContext(),"Genero nuevo creado", Toast.LENGTH_SHORT).show();
                                                                db.collection("genre").document().set(new Genre(artistnew.getGenre(),"s"));
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.v("ERROR LOAD ARTIST", e.getMessage());
                                                        }
                                                    });
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.v("ERROR LOAD ARTIST", e.getMessage());
                                }
                            });
                }else{
                    Toast.makeText(view.getContext(),"Faltan Campos",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
