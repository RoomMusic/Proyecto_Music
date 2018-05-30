package com.example.vidiic.proyecto_music.launcher;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.vidiic.proyecto_music.Home.HomeActivity;
import com.example.vidiic.proyecto_music.MainActivity;
import com.example.vidiic.proyecto_music.R;

import java.nio.BufferUnderflowException;

/**
 * Created by poldominguez on 28/5/18.
 */

public class Activity_Launcher extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        final Intent i = new Intent(this, HomeActivity.class);
        Thread timer = new Thread(){

            public void run() {

                try{

                    sleep(3000);

                }  catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {

                    startActivity(i);
                    finish();


                }

            }
        };
                timer.start();
    }
}
