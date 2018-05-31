package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder> {

    private List<Publicacion> publiaciones_list;
    private Context context;
    private String current_user_id;
    private String url_song;

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


                vh.progressBar.setVisibility(View.VISIBLE);
                vh.progressBar.setIndeterminate(true);

                //abrimos un canal con el usuario para mandarle el mensaje
                Intent chat_intent = new Intent(context.getApplicationContext(), ChatActivity.class);

                String[] userIds = {current_user_id, user_publitacion.getUserid()};

                Log.d("sergio", "id1 " + userIds[0] + " id2 " + userIds[1]);

                Log.d("sergio", "nombre cancion: " + publicacion.getPublication_song().getName());

                if (!current_user_id.equals(user_publitacion.getUserid())) {
                    chat_intent.putExtra("userids", userIds);
                    chat_intent.putExtra("url_song", url_song);

                    Log.d("sergio", "id1 " + userIds[0] + " id2 " + userIds[1]);

                    context.startActivity(chat_intent);
                }else{
                    Toast.makeText(context.getApplicationContext(), "Ya tienes esta cancion.", Toast.LENGTH_SHORT).show();
                }


            }
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
