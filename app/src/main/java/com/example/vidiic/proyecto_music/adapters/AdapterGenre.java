package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.Genre_Activity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Genre;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vidiic on 26/05/2018.
 */

public class AdapterGenre extends RecyclerView.Adapter<AdapterGenre.MyViewHolder> {

    private Context myContext;
    private List<Genre> genreList;

    public AdapterGenre(Context myContext, List<Genre> genreList) {
        this.myContext = myContext;
        this.genreList = genreList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        view = layoutInflater.inflate(R.layout.cardview_genre, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.text_name_genre.setText(genreList.get(position).getName());
        Log.e("GENRES",genreList.get(position).getName());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


        firebaseStorage.getReference().child("generopics/"+ genreList.get(position).getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(holder.image_genre);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Picasso.get().load(R.drawable.user_empty_image).into(holder.image_genre);
            }
        });

        //click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, Genre_Activity.class);

                intent.putExtra("NameGenre",genreList.get(position).getName());
                intent.putExtra("ImageGenre",R.drawable.ic_action_music);

                myContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_name_genre;
        ImageView image_genre;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            text_name_genre = itemView.findViewById(R.id.viewnamegenre);
            image_genre = itemView.findViewById(R.id.viewimagengenre);
            cardView = itemView.findViewById(R.id.cardview_genre);

        }
    }
}
