package com.example.vidiic.proyecto_music.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.Artist_Activity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Artist;

import java.util.List;

/**
 * Created by Vidiic on 26/05/2018.
 */

public class AdapterArtistGenre extends RecyclerView.Adapter<AdapterArtistGenre.MyViewHolder> {

    private Context myContext;
    private List<Artist> artistsList;

    public AdapterArtistGenre(Context myContext, List<Artist> artistsList) {
        this.myContext = myContext;
        this.artistsList = artistsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        view = layoutInflater.inflate(R.layout.viewholder_artist_genre, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.image_artist.setImageResource(R.drawable.artistdefect);

        //click listener
        holder.image_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(myContext, Artist_Activity.class);

               intent.putExtra("NameArtist",artistsList.get(position).getName());
               intent.putExtra("DescriptionArtist",artistsList.get(position).getDescription());
               intent.putExtra("GenereArtist",artistsList.get(position).getGenre());
               intent.putExtra("age",artistsList.get(position).getAge());
               intent.putExtra("ImageArtist",R.drawable.ic_action_music);

               myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image_artist;

        public MyViewHolder(View itemView){
            super(itemView);
            image_artist = itemView.findViewById(R.id.image_artist_genre);
        }
    }
}
