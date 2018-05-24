package com.example.vidiic.proyecto_music.asynctasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;


import com.example.vidiic.proyecto_music.classes.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidiic on 17/03/2018.
 */

public class AsyncTaskSong extends AsyncTask<String,Integer,List<Song>> {

    private static String data="";
    public List<Song> songs;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private byte[] art;
    private Bitmap songimage;
    private static final int MY_PERMISSION_REQUEST = 1;

    public interface WeakReference{
        Context getContext();
        void finished(List<Song> list);
    }
    private WeakReference ref;

    public AsyncTaskSong(WeakReference ref){
        super();
        this.ref=ref;
    }
    @Override
    protected List<Song> doInBackground(String... strings) {
        songs = new ArrayList<>();
        Log.i("Main","Entramos Async");
        ContentResolver contentResolver =ref.getContext().getContentResolver();
        Uri songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor =contentResolver.query(songuri,null,null,null,null);

        if (songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int imagen = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentImae = songCursor.getString(imagen);

                /*try{
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(currentImae);
                    art = mediaMetadataRetriever.getEmbeddedPicture();
                    songimage = BitmapFactory.decodeByteArray(art,0,art.length);

                }catch (Exception e) {
                   songimage = null ;
                }*/
                Song song = new Song(currentTitle,currentArtist,currentImae);
                Log.d("Main", "doInBackground: "+song.toString());
                songs.add(song);

            }while (songCursor.moveToNext());
        }

        return songs;
    }
    @Override
    public void onPostExecute(List<Song> result) {
        ref.finished(result);
    }
}
