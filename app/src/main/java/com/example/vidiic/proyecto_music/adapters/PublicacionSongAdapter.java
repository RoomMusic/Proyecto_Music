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

import java.util.List;

public class PublicacionSongAdapter  extends RecyclerView.Adapter<PublicacionSongAdapter.ViewHolder>{

    private List<Song> songList;

    public PublicacionSongAdapter(List<Song> songList) {
        super();
        this.songList = songList;
        Song s = new Song("te bote", "bad bunny", "");
        songList.add(s);
    }

    public List<Song> getSongList() {
        return songList;
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

        if (song.getImageSong().equals("")) vh.songImage.setImageResource(R.drawable.ic_action_music);

        if (!song.getName().equals("")) vh.song_name.setText(song.getName());
        else vh.song_name.setText("song name");

        vh.add_song_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!check_box){
                    //añadimos cancion al array
                    songList.add(song);
                    check_box = true;
                    Log.d("songadapter", "Cancion añadida " + song.getName() + songList.size());
                }else{
                    //eliminamos la cancion
                    songList.remove(song);
                    check_box = false;
                    Log.d("songadapter", "Cancion eliminada " + song.getName() + songList.size());

                }
                Log.d("songadapter", "Cancion seleccionada " + song.getName());
            }
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
