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

public class AdapterArtist extends RecyclerView.Adapter<AdapterArtist.MyViewHolder> {

    private Context myContext;
    private List<Artist> artistsList;

    public AdapterArtist(Context myContext, List<Artist> artistsList) {
        this.myContext = myContext;
        this.artistsList = artistsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        view = layoutInflater.inflate(R.layout.cardview_artist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.text_name_artist.setText(artistsList.get(position).getName());
        holder.image_artist.setImageResource(R.drawable.ic_action_music);

        //click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
        TextView text_name_artist;
        ImageView image_artist;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);

            text_name_artist = itemView.findViewById(R.id.name_artist);
            image_artist = itemView.findViewById(R.id.image_artist);
            cardView = itemView.findViewById(R.id.cardview_artist);

        }
    }
}
