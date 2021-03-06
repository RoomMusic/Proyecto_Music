package com.example.vidiic.proyecto_music.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Publicacion;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder> implements MediaPlayer.OnPreparedListener {

    private List<Publicacion> publiaciones_list;
    private Context context;
    private String current_user_id;
    private String url_song;
    private FirebaseStorage song_firebase_storage;
    private StorageReference song_storage_reference;
    private String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public PublicacionAdapter(String current_user_id, Context context, List<Publicacion> publicaciones_list) {
        super();
        this.current_user_id = current_user_id;
        this.context = context;
        this.publiaciones_list = publicaciones_list;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private List<Song> song_list;
    private List<Song> song_list_aux;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View publication_item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_user_muro, parent, false);
        //inicializamos la instancia firebase storage para poder hacer referncia al usuario al cual pertenece la cancion
        song_firebase_storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        song_list = new ArrayList<>();
        song_list_aux = new ArrayList<>();

        mediaPlayer = new MediaPlayer();


        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("songlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Song song = snapshot.toObject(Song.class);
                        Log.d("sergio", "cancion descargada: " + song.getName());
                        song_list_aux.add(song);
                    }
                }
            }
        });

        return new PublicationViewHolder(publication_item);
    }

    private MediaPlayer mediaPlayer;
    ;
    private String FIRE_STORAGE_URL;

    private void getAudioFromFirebase(Publicacion publi, ProgressBar pb, ImageButton playBtn) {

        //guardamos el usuario y la cancion en variables
        UserApp user = publi.getPublication_user();
        Song song = publi.getPublication_song();

        //guardamos la url a la cual tenemos que acceder a los archivos streaming
        FIRE_STORAGE_URL = context.getResources().getString(R.string.FireStorageURL);

        //recoggemos el nombre de la cacion para buscarla en firestorage
        String song_file_name = Song.splitImageSong(song.getImageSong());

        Log.d("sergio", "song file name: " + song_file_name);

        //obtenemos la cancion del usuario y la descargamos
        song_firebase_storage.getReferenceFromUrl(FIRE_STORAGE_URL + "/" + user.getEmail() + "/music/" + song_file_name)
                .getDownloadUrl().addOnSuccessListener(uri -> {
            try {

                mediaPlayer = new MediaPlayer();

                //servicio streaming
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                String url = uri.toString();

                Log.d("sergio", url);

                mediaPlayer.setDataSource(url);

                mediaPlayer.prepareAsync();

                //empieza a reproducir
                mediaPlayer.setOnPreparedListener(mp -> {

                    pb.setIndeterminate(false);
                    pb.setVisibility(View.GONE);
                    playBtn.setEnabled(true);

                    mediaPlayer.start();


                });


            } catch (IOException e) {
                Log.d("sergio", e.getMessage());
            }
        }).addOnFailureListener(e -> Log.d("sergio", e.getMessage()));

    }


    //id de la publicacion que esta reproduciendo
    private int id_publi_playing;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Publicacion publicacion = publiaciones_list.get(position);
        UserApp u = publicacion.getPublication_user();

        PublicationViewHolder vh = (PublicationViewHolder) holder;

        if (publicacion.getPublication_image() == null) {
            vh.publication_image.setImageResource(R.drawable.ic_action_music);
        } else {
            song_firebase_storage.getReference().child(publicacion.getPublication_user().getEmail() + "/pictures/" + publicacion.getPublication_user().getUserImage() + ".jpg").getDownloadUrl().
                    addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(vh.publication_image);
                            vh.publication_image.invalidate();
                        }
                    });
        }

        vh.userName.setText(publicacion.getPublication_user().getUserName());
        vh.songName.setText(publicacion.getPublication_song().getName());

        //function click play in song
        vh.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //guardamos el id de la publicacion que ha clicado el usuario
                int id_publi_selected = publicacion.getPublication_id();

                if (!mediaPlayer.isPlaying()) {

                    vh.progressBar.setIndeterminate(true);
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.playBtn.setEnabled(false);
                    Toast.makeText(context, context.getResources().getString(R.string.AdapaterPubliGetSong), Toast.LENGTH_SHORT).show();

                    //obtenemos y reproducimos la cancion seleccionada
                    getAudioFromFirebase(publicacion, vh.progressBar, vh.playBtn);

//                    Log.d("sergio", "play");
                    vh.playBtn.setBackgroundResource(R.drawable.ic_pause);
                    id_publi_playing = id_publi_selected;
                } else {
                    if (id_publi_selected == id_publi_playing) {
                        if (mediaPlayer != null) {
                            //hacemos reset ya que no queremos dejar de utilizar el media player
                            mediaPlayer.reset();
                        }
//                        Log.d("sergio", "pause");
                        vh.playBtn.setBackgroundResource(R.drawable.ic_play);

                    }
                }
            }
        });


        //funcion para pedir la cancion al usuario
        vh.askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardamos el usuario de la publicacion
                UserApp user_publitacion = publicacion.getPublication_user();

                Song song_publicacion = publicacion.getPublication_song();


                //abrimos un canal con el usuario para mandarle el mensaje
                //Intent chat_intent = new Intent(context.getApplicationContext(), ChatActivity.class);

                String[] userIds = {current_user_id, user_publitacion.getUserid()};

                //Log.d("sergio", "id1 " + userIds[0] + " id2 " + userIds[1]);

                //Log.d("sergio", "nombre cancion: " + publicacion.getPublication_song().getName());
                //Log.d("sergio", "nombre cancion: " + publicacion.getPublication_song().getImageSong());

                boolean check_if_song_exists = false;

                for (Song s : song_list_aux) {
                    if (s.getName().equalsIgnoreCase(song_publicacion.getName())) {
                        check_if_song_exists = true;
                    }
                }


                //comprobamos que la cancion seleccionada por elusuario no la tiene en su lista
                if (!check_if_song_exists) {


                    //descargar cancion
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.progressBar.setIndeterminate(true);


                    downloadSong(publicacion, vh.progressBar, song_publicacion);

                } else {
                    Toast.makeText(context.getApplicationContext(), "Ya tienes esta cancion.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private DownloadManager downloadManager;
    private String song_name;


    private void downloadSong(Publicacion publicacion, ProgressBar progressBar, Song song) {

        String song_user_email = publicacion.getPublication_user().getEmail();
        song_name = publicacion.getPublication_song().getImageSong();


        //dividimos la ruta de la imagen por la /
        String[] fileName = song_name.split("/");

        //en el 5 lugar tenemos el nombre con el que se ha guardado en la base de datos de almacenamiento
        song_name = fileName[5];

        //creamos una referencia al usuario del cual queremos obtenes el email
        song_storage_reference = song_firebase_storage.getReference();


        Log.d("descarga", "useremail: " + song_user_email + " songname: " + song_name);

        //archivo de la cancion
        try {
            File.createTempFile("audio", "mpeg");
        } catch (IOException e) {
            e.printStackTrace();

        }

        song_storage_reference.child(song_user_email + "/music/" + song_name).getDownloadUrl().addOnSuccessListener(uri -> {

            String url = uri.toString();

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            request.setTitle(song_name);

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, song_name);

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            manager.enqueue(request);

            //actualizamos la lista del usuario
            Log.d("descarga", "URI: " + uri.toString());

            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);

            //guardamos la cancion en firebase
            Log.d("descarga", "song details: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + song_name);

            //al descargar la cancion en el dispositivo le seteamos la ruta de la carpeta de descargas con el nombre de la cancion
            song.setImageSong(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + song_name);


            saveSongInFireBase(song, current_user_id);

        }).addOnFailureListener(e -> {

            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
            Log.d("descarga", "error: " + e.getMessage());
            Toast.makeText(context, "Error al descargar cancion", Toast.LENGTH_SHORT).show();
        });

    }

    private void saveSongInFireBase(Song song, String user_id) {

        //lista para guardar los ids de las canciones obtenidas
        List<Integer> song_id_list = new ArrayList<>();
        //lista para guardar todas las canciones descargadas
        List<Song> song_list = new ArrayList<>();


        firebaseFirestore.collection("users").document(user_id).collection("songlist").orderBy("idsong", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {

            //si no esta vacia quiere decir que tiene canciones por lo tanto comprobamos si existe
            if (!queryDocumentSnapshots.isEmpty()) {

                for (DocumentSnapshot snap : queryDocumentSnapshots) {
                    //Log.d("descarga", "cancion id: " + snap.toObject(Song.class).getIdsong() + "song id: " + song.getIdsong());
                    Song s = snap.toObject(Song.class);

                    //Log.d("descarga", "cancion aux: " + s.getName() + " cancion descargada: " + song.getName());

                    song_list.add(s);
                    song_id_list.add(s.getIdsong());

                }

                //si el id de la cancion descargada esta en la lista de ids descargados, comprobamos que no sea la misma cancion
                if (song_id_list.contains(song.getIdsong())) {

                    //recogemos la posicion del indice que coincide para obtener la cancion por id y comprobar que no son la misma comparando los nombres de la descargada y de la que tiene el mismo id
                    int position = song_id_list.indexOf(song.getIdsong());

                    Song songaux = song_list.get(position);

                    //si el nombre es igual la cancion no se descargara ya que ya esta presente en la base de datos
                    if (songaux.getName().equals(song.getName())) {
                        Toast.makeText(context, "La cancion ya existe en la bbdd", Toast.LENGTH_SHORT).show();
                    } else {
                        //si el nombre no es el mismo quiere decir que no existe en la bbdd por lo tanto le asignamos un nuevo id y la añadimos
                        int new_song_id = Collections.max(song_id_list) + 1;

                        song.setIdsong(new_song_id);

                        firebaseFirestore.collection("users").document(current_user_id).collection("songlist")
                                .document("Song-" + String.valueOf(song.getIdsong())).set(song).addOnSuccessListener(aVoid -> Toast.makeText(context, "Cancion guardada en firebase 1", Toast.LENGTH_SHORT).show());

                    }
                } else {

                    int new_song_id = Collections.max(song_id_list) + 1;

                    boolean check = false;

                    for (Song saux : song_list) {
                        if (saux.getName().equals(song.getName())) {
                            Log.d("descarga", "cancion aux: " + saux.getName() + " cancion descargada: " + song.getName());
                            check = true;
                        }
                    }

                    //si es falso quiere decir que no coincide el nombre por lo tanto la ingresamos en la bbdd
                    if (!check) {

                        song.setIdsong(new_song_id);

                        firebaseFirestore.collection("users").document(current_user_id).collection("songlist")
                                .document("Song-" + String.valueOf(song.getIdsong())).set(song).addOnSuccessListener(aVoid -> Toast.makeText(context, "Cancion guardada en firebase 2", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(context, "La cancion ya existe en la bbdd 2", Toast.LENGTH_SHORT).show();

                    }


                }

                //si esta vacia añadimos la cancion
            } else {
                Toast.makeText(context, "No hay canciones en tu lista", Toast.LENGTH_SHORT).show();

                //seteamos el id de la cancion a 1
                song.setIdsong(1);

                firebaseFirestore.collection("users").document(current_user_id).collection("songlist")
                        .document("Song-" + String.valueOf(song.getIdsong()))
                        .set(song)
                        .addOnSuccessListener(aVoid -> {

                            Toast.makeText(context, "Cancion guardada en firebase 3", Toast.LENGTH_SHORT).show();
                        });

            }
        });


    }


    @Override
    public int getItemCount() {
        return publiaciones_list.size();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        mediaPlayer.start();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PublicationViewHolder extends ViewHolder {

        ImageView publication_image;
        TextView userName, songName;
        ImageButton askBtn, playBtn;
        ProgressBar progressBar;

        public PublicationViewHolder(View itemView) {
            super(itemView);

            publication_image = itemView.findViewById(R.id.songImageMuro);
            userName = itemView.findViewById(R.id.userNameMuro);
            songName = itemView.findViewById(R.id.songNameMuro);
            askBtn = itemView.findViewById(R.id.btnAsk);
            playBtn = itemView.findViewById(R.id.btnPlaySong);
            progressBar = itemView.findViewById(R.id.progress_bar_download);
            progressBar.setVisibility(View.GONE);

        }


    }


}
