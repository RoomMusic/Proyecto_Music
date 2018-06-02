package com.example.vidiic.proyecto_music.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.vidiic.proyecto_music.Home.HomeActivity;
import com.example.vidiic.proyecto_music.Login.LoginActivity;
import com.example.vidiic.proyecto_music.R;

/**
 * Created by poldominguez on 28/5/18.
 */

public class Activity_Launcher extends AppCompatActivity {

    //variable para saber si el usuario ya ha entrado a la app
    Intent nextIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        nextIntent = new Intent(this, HomeActivity.class);
        Thread timer = new Thread() {

            public void run() {

                try {

                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {

                    if (isFirstRun) {
                        startActivity(nextIntent);
                        finish();
                    }else{
                        nextIntent = new Intent(Activity_Launcher.this, LoginActivity.class);
                        startActivity(nextIntent);
                        finish();
                    }

                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();


                }

            }
        };
        timer.start();
    }
}
