package com.example.vidiic.proyecto_music.Login;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.vidiic.proyecto_music.R;

/**
 * Created by poldominguez on 26/5/18.
 */

public class Activity_Reg extends AppCompatActivity {

    RelativeLayout rellay3;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay3.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);

        handler.postDelayed(runnable, 1000); //2000 is the timeout for the splash
    }
}