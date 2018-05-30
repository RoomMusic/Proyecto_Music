package com.example.vidiic.proyecto_music.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Publicacion;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder>{

    private List<Publicacion> publiaciones_list;

    public PublicacionAdapter(List<Publicacion> publicaciones_list) {
        super();
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

        if (publicacion.getPublication_image() == null){
            vh.publication_image.setImageResource(R.drawable.ic_action_music);
        }

        vh.userName.setText(publicacion.getPublication_user().getUserName());
        vh.songName.setText(publicacion.getPublication_song().getName());
    }

    @Override
    public int getItemCount() {
        return publiaciones_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PublicationViewHolder extends ViewHolder {

        ImageView publication_image;
        TextView userName, songName;
        ImageButton askBtn, playBtn;

        public PublicationViewHolder(View itemView) {
            super(itemView);

            publication_image = itemView.findViewById(R.id.songImageMuro);
            userName = itemView.findViewById(R.id.userNameMuro);
            songName = itemView.findViewById(R.id.songNameMuro);
            askBtn = itemView.findViewById(R.id.btnAsk);
            playBtn = itemView.findViewById(R.id.btnPlaySong);



        }


    }



}
