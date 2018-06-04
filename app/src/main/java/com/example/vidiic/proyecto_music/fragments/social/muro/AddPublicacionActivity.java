package com.example.vidiic.proyecto_music.fragments.social.muro;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.PublicacionSongAdapter;
import com.example.vidiic.proyecto_music.classes.Publicacion;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPublicacionActivity extends AppCompatActivity {

    private FloatingActionButton success_add_publication_btn, show_songs_btn, cancel_add_publicacion;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView rv_song_list;
    private PublicacionSongAdapter publicacionSongAdapter;
    private Publicacion publicacion;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Toolbar toolbar_add_publication;
    private TextView selected_song_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publicacion);


        success_add_publication_btn = findViewById(R.id.success_add_publication);

        show_songs_btn = findViewById(R.id.find_song_btn);
        rv_song_list = findViewById(R.id.rv_song_list);
        cancel_add_publicacion = findViewById(R.id.cancel_add_publication);

        selected_song_title = findViewById(R.id.songNameAddPublication);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String user_id = firebaseAuth.getCurrentUser().getUid();
        toolbar_add_publication = findViewById(R.id.toolbar_add_publicacion);

        //configuramos la app bar
        setSupportActionBar(toolbar_add_publication);

        getSupportActionBar().setTitle(R.string.AddPubliTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //añadimos la publicacion
        success_add_publication_btn.setOnClickListener(v -> {

            //obtenemos los datos de la cancion seleccionada
            if (publicacionSongAdapter.getSelectedSongList().size() > 1) {
                Log.d("songadapter", "solo pueds seleccionar una cancion: " + publicacionSongAdapter.getSelectedSongList().size());
                Toast.makeText(this, "Solo puedes seleccionar una cancion. Seleccionadas: " + publicacionSongAdapter.getSelectedSongList().size(), Toast.LENGTH_SHORT).show();
            } else {

                if (publicacionSongAdapter.getSelectedSongList().isEmpty()) {

//                    getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_publication, fragment_muro).commit();
//                    relative_publication_details.setVisibility(View.GONE);
//                    relative_song_list.setVisibility(View.GONE);

                    finish();

                } else {

                    publicacion = new Publicacion(new Date(), publicacionSongAdapter.getSelectedSongList().get(0));

                    //agregamos la publicacion a firebase
                    addPublicationToFirebase(user_id, firebaseFirestore, publicacion, v);

                    //getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_publication, fragment_muro).commit();
                    //relative_publication_details.setVisibility(View.GONE);
                    //relative_song_list.setVisibility(View.GONE);
                    finish();
                    Log.d("sergio", "path sancion: " + song.getImageSong());
                    Toast.makeText(this, "Cancion añadida ", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //mostramos las canciones del usuario
        show_songs_btn.setOnClickListener(v -> {

            publicacionSongAdapter = new PublicacionSongAdapter(getSongList(firebaseFirestore, user_id), selected_song_title);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            rv_song_list.setLayoutManager(layoutManager);
            rv_song_list.setItemAnimator(new DefaultItemAnimator());
            rv_song_list.setAdapter(publicacionSongAdapter);

            //publicacionSongAdapter.notifyDataSetChanged();
            Log.d("add_publicacion", "entro cabron");
            //Toast.makeText(view.getContext(), "Canciones mostradas", Toast.LENGTH_SHORT).show();
        });

        cancel_add_publicacion.setOnClickListener(v -> {
            //Toast.makeText(view.getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
//            getSupportFragmentManager().beginTransaction().replace(R.id.relative_add_publication, fragment_muro).commit();
//            relative_song_list.setVisibility(View.GONE);
//            relative_publication_details.setVisibility(View.GONE);
            finish();
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();


        return super.onOptionsItemSelected(item);
    }

    private void uploadSongFile(UserApp user, String path) {

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        //dividimos la ruta de la imagen por la /
        String[] fileName = path.split("/");

        //en la posicion 5 tenemos el string del titulo mas la extension .mp3
        Log.d("sergio", "DATA NAME " + fileName[5] + user.getEmail());

        //configuramos una ruta para guardar los archivos, esta ruta consistira en el email del usuario mas el nombre del archvio
        //de esta manera los archivos se guardan por usuario
        storageReference = firebaseStorage.getReference().child(user.getEmail() + "/music/" + fileName[5]);

        Uri songFile = Uri.fromFile(new File(path));

        //con uppload task subimos el archivo a firebase
        //asi se sube y ya esta
        storageReference.putFile(songFile);

        //si guardamos en una variable la tarea podremos configurar distintas acciones para las distintas fases de subida de archvios
        UploadTask uploadTask = storageReference.putFile(songFile);

    }

    private int new_id;
    private Publicacion publicacionAux;

    private UserApp app_user;

    private void addPublicationToFirebase(String user_id, FirebaseFirestore firebaseFirestore, Publicacion publicacion, View view) {


        //obtenemos el usuario y lo asociamos con la publicacion
        firebaseFirestore.collection("users").document(user_id).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) app_user = documentSnapshot.toObject(UserApp.class);
            Log.d("publicaciones", "username: " + app_user.getEmail());

            publicacion.setPublication_user(app_user);

            //obtenemos la publicacion con el id mas grande y le sumammos uno
            firebaseFirestore.collection("publicaciones").orderBy("publication_id", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();

                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();

                            if (!docList.isEmpty())
                                publicacionAux = docList.get(0).toObject(Publicacion.class);

                            if (publicacionAux != null)
                                new_id = publicacionAux.getPublication_id() + 1;
                            else new_id = 1;

                            Log.d("publicacion", "nuevo id: " + new_id);

                            //añadimos el nuevo id a la publicacion
                            publicacion.setPublication_id(new_id);

                            firebaseFirestore.collection("publicaciones").document(String.valueOf(publicacion.getPublication_id())).set(publicacion)
                                    .addOnCompleteListener(aVoid -> {

                                        //Toast.makeText(view.getContext(), "Cancion añadida a firebase.", Toast.LENGTH_SHORT).show();

                                        uploadSongFile(publicacion.getPublication_user(), publicacion.getPublication_song().getImageSong());



                                    });


                        }
                    });

        });

    }

    private Song song;
    private List<Song> song_list;

    public List<Song> getSongList(FirebaseFirestore firebaseFirestore, String user_id) {

        song_list = new ArrayList<>();

        firebaseFirestore.collection("users").document(user_id).collection("songlist").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot snapshot : task.getResult()) {
                    song = snapshot.toObject(Song.class);
                    Log.d("getsong", "song name: " + song.getIdsong() + " " + song.getName());
                    song_list.add(song);
                }

                publicacionSongAdapter.notifyDataSetChanged();
            }
        });

        return song_list;
    }
}
