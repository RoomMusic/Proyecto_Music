package com.example.vidiic.proyecto_music.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.Song;

import java.util.ArrayList;
import java.util.List;

public class PublicacionSongAdapter  extends RecyclerView.Adapter<PublicacionSongAdapter.ViewHolder>{

    private List<Song> songList;
    private List<Song> selected_song_list;

    public PublicacionSongAdapter(List<Song> songList) {
        super();
        this.songList = songList;
        selected_song_list = new ArrayList<>();
    }

    public List<Song> getSelectedSongList() {
        return selected_song_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View song_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.publicacion_song_list_item, parent, false);


        return new SongPostViewHolder(song_item);
    }

    private boolean check_box;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Song song = songList.get(position);

        SongPostViewHolder vh = (SongPostViewHolder) holder;

        if (!song.getImageSong().equals("")) vh.songImage.setImageResource(R.drawable.ic_action_music);

        if (!song.getName().equals("")) vh.song_name.setText(song.getName());

        else vh.song_name.setText("song name");

        vh.add_song_check_box.setOnClickListener(v -> {

            if (selected_song_list.contains(song)){
                selected_song_list.remove(song);
                Log.d("songadapter", "Cancion eliminada " + song.getId() + selected_song_list.size());
            }else{
                selected_song_list.add(song);
                Log.d("songadapter", "Cancion añadida " + song.getId() + selected_song_list.size());
            }

            Log.d("songadapter", "Cancion seleccionada " + song.getName());
            Log.d("songadapter", "Tamaño array canciones: " + selected_song_list.size());
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class SongPostViewHolder extends ViewHolder{

        ImageView songImage;
        TextView song_name;
        CheckBox add_song_check_box;

        public SongPostViewHolder(View itemView) {
            super(itemView);

            songImage = itemView.findViewById(R.id.publicacion_song_image_view);
            song_name = itemView.findViewById(R.id.publicacion_song_name);
            add_song_check_box = itemView.findViewById(R.id.checkbox_add_song);
        }



    }

}
