package com.example.vidiic.proyecto_music;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.classes.Genre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class From_Genre extends AppCompatActivity {

    EditText editname;
    Button btn;
    FirebaseFirestore db;
    Toolbar toolbar_form_genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from__genre);

        editname = findViewById(R.id.formnomgenre);
        btn = findViewById(R.id.btngenreform);

        db = FirebaseFirestore.getInstance();

        addToolbar(R.id.toolbar_form_genre, R.string.FormGenreTitle);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db.collection("genre")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Genre genre = documentSnapshot.toObject(Genre.class);
                                    if (genre.getName().toUpperCase().equals(editname.getText().toString().toUpperCase())){
                                        Toast.makeText(view.getContext(),"Este Genero ya existe",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                db.collection("genre").document().set(new Genre(editname.getText().toString(),"s"));
                                Toast.makeText(view.getContext(),"Genero creado",Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    private void addToolbar(int resource_id, int title) {

        Toolbar toolbar = findViewById(resource_id);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
