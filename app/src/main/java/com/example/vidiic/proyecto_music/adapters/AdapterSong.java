package com.example.vidiic.proyecto_music.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Song;
import com.example.vidiic.proyecto_music.player.Player_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by Vidiic on 24/05/2018.
 */

public class AdapterSong extends RecyclerView.Adapter<AdapterSong.SongViewHolder> implements View.OnClickListener {

    List<Song> songsList;

    View.OnClickListener listener;

    Dialog dialog;

    public AdapterSong(List<Song> songsList) {
        this.songsList = songsList;

    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_songlist, parent, false);

        itemView.setOnClickListener(this);
        dialog = new Dialog(parent.getContext());


        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        holder.imagen.setImageResource(R.drawable.ic_action_music);
        holder.nombreSong.setText(songsList.get(position).getName());
        holder.artistaSong.setText(songsList.get(position).nameOfArtists());

        dialog.setContentView(R.layout.dialog_music);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        holder.item_music.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"Test Click", Toast.LENGTH_LONG).show();
                TextView dialog_name = dialog.findViewById(R.id.dialog_name);
                TextView dialog_artist = dialog.findViewById(R.id.dialog_artist);
                ImageView dialog_Image = dialog.findViewById(R.id.dialog_image);
                Button btnplay = dialog.findViewById(R.id.dialog_btnplay);
                Button btndelete = dialog.findViewById(R.id.dialog_btndelete);

                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = database.collection("users").document(idUser)
                            .collection("songlist").document("Song-"+songsList.get(position).getIdsong());
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(view.getContext(),"Eliminado",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                btnplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), Player_Activity.class);

                        intent.putExtra("NameSong", songsList.get(position).getName());
                        intent.putExtra("Path", songsList.get(position).getImageSong());

                        Log.d("player", "name song: " + songsList.get(position).getImageSong());

                        view.getContext().startActivity(intent);
                    }
                });

                dialog_name.setText(songsList.get(position).getName());
                dialog_artist.setText(songsList.get(position).nameOfArtists());
                //dialog_Image.setImageBitmap();
                dialog_Image.setImageResource(R.drawable.ic_action_music);


                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout item_music;
        public ImageView imagen;
        public TextView nombreSong;
        public TextView artistaSong;

        public SongViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageSong);
            nombreSong = itemView.findViewById(R.id.nameSong);
            artistaSong = itemView.findViewById(R.id.nameArtist);
            item_music = itemView.findViewById(R.id.music_item);
        }
    }
}
