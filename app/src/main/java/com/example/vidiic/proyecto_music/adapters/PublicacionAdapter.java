package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.example.vidiic.proyecto_music.fragments.social.chat.ChatActivity;
import com.example.vidiic.proyecto_music.classes.Publicacion;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder> {

    private List<Publicacion> publiaciones_list;
    private Context context;
    private String current_user_id;
    private String url_song;
    private FirebaseStorage song_firebase_storage;
    private StorageReference song_storage_reference;
    private String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public PublicacionAdapter(String current_user_id, Context context, List<Publicacion> publicaciones_list) {
        super();
        this.current_user_id = current_user_id;
        this.context = context;
        this.publiaciones_list = publicaciones_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View publication_item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_user_muro, parent, false);
        //inicializamos la instancia firebase storage para poder hacer referncia al usuario al cual pertenece la cancion
        song_firebase_storage = FirebaseStorage.getInstance();


        return new PublicationViewHolder(publication_item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Publicacion publicacion = publiaciones_list.get(position);

        PublicationViewHolder vh = (PublicationViewHolder) holder;

        if (publicacion.getPublication_image() == null) {
            vh.publication_image.setImageResource(R.drawable.ic_action_music);
        }

        vh.userName.setText(publicacion.getPublication_user().getUserName());
        vh.songName.setText(publicacion.getPublication_song().getName());


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

                if (!current_user_id.equals(user_publitacion.getUserid())) {


                    //descargar cancion
                    vh.progressBar.setVisibility(View.VISIBLE);
                    vh.progressBar.setIndeterminate(true);


                    downloadSong(publicacion, vh.progressBar);


//                    chat_intent.putExtra("userids", userIds);
//                    chat_intent.putExtra("url_song", url_song);
//
//                    Log.d("sergio", "id1 " + userIds[0] + " id2 " + userIds[1]);
//
//                    context.startActivity(chat_intent);
                } else {
                    Toast.makeText(context.getApplicationContext(), "Ya tienes esta cancion.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private File song_file = null, song_file_aux = null;

    private void downloadSong(Publicacion publicacion, ProgressBar progressBar){

        String song_user_email = publicacion.getPublication_user().getEmail();
        String song_name = publicacion.getPublication_song().getImageSong();
        StorageReference songReference;


        //dividimos la ruta de la imagen por la /
        String[] fileName = song_name.split("/");

        //en el 5 lugar tenemos el nombre con el que se ha guardado en la base de datos de almacenamiento
        song_name = fileName[5];

        //creamos una referencia al usuario del cual queremos obtenes el email
        song_storage_reference = song_firebase_storage.getReference();


        songReference = song_storage_reference.child(song_user_email + "/" + song_name);


        Log.d("descarga", "useremail: " + song_user_email + " songname: " + song_name);

        //archivo de la cancion
        try {
            song_file = File.createTempFile("audio", "mpeg");
        } catch (IOException e) {
            e.printStackTrace();

        }

//        song_storage_reference.child(song_user_email + "/" + song_name).getDownloadUrl().addOnSuccessListener(uri -> {
//            song_file = new File(uri.toString());
//
//
//            //actualizamos la lista del usuario
//
//
//            Log.d("descarga", "URI: " + uri.toString());
//            progressBar.setIndeterminate(false);
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(context, "Cancion descargada", Toast.LENGTH_SHORT).show();
//        }).addOnFailureListener(e -> {
//            progressBar.setIndeterminate(false);
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(context, "Error al descargar cancion", Toast.LENGTH_SHORT).show();
//        });

        songReference.getFile(song_file).addOnSuccessListener(taskSnapshot -> {
            Log.d("descarga", "song data: " + song_file.getPath());
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Cancion descargada", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Error al descargar cancion", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return publiaciones_list.size();
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
