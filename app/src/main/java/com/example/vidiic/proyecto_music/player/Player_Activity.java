package com.example.vidiic.proyecto_music.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.R;

import java.io.File;

public class Player_Activity extends AppCompatActivity {

    Button btnplay;
    SeekBar volumenBar;
    SeekBar positionBar;
    MediaPlayer mediaPlayer;
    TextView finshtime,currentime;
    TextView elapsedTimeLabel;
    TextView emainingTimeLabel;
    int totalTime;
    Toolbar toolbar_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_);

        Intent intent = getIntent();
        String nameSong = intent.getExtras().getString("NameSong");
        String path = intent.getExtras().getString("Path");

        btnplay = findViewById(R.id.playSong);
        positionBar = findViewById(R.id.songtime);
        volumenBar = findViewById(R.id.songvolum);
       // finshtime = findViewById(R.id.finishtime);
        currentime = findViewById(R.id.currenttime);

        elapsedTimeLabel = findViewById(R.id.elapsedTimelabel);
        emainingTimeLabel = findViewById(R.id.remainingTimelabel);

        addToolbar(toolbar_player, R.id.toolbar_player, R.string.PlayerTitle);

        Uri uri = Uri.parse(path);

        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        totalTime = mediaPlayer.getDuration();
        //finshtime.setText(totalTime);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    btnplay.setBackgroundResource(R.drawable.ic_pause);

                }else {
                    mediaPlayer.pause();
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                }
            }
        });

        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum = progress / 100f;
                mediaPlayer.setVolume(volumeNum, volumeNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int currentPosition = msg.what;
                //update position
                positionBar.setProgress(currentPosition);
                //updatelabel
                String elapsedTime = createTimeLabel(currentPosition);
                elapsedTimeLabel.setText(elapsedTime);

                String remainingtime = createTimeLabel(totalTime-currentPosition);
                emainingTimeLabel.setText("- "+ remainingtime);



            }
        };

        public String createTimeLabel(int time){
            String timelabel = "";
            int min = time / 1000 / 60;
            int seg = time /1000 % 60 ;

            timelabel = min + ":";
            if (seg<10) timelabel += "0";
            timelabel += seg;

            return timelabel;

        }


    private void addToolbar(Toolbar toolbar, int resource_id, int title) {



        toolbar = findViewById(resource_id);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }
}
