package com.example.vidiic.proyecto_music.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.vidiic.proyecto_music.R;

/**
 * Created by poldominguez on 25/5/18.
 */

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        //delcarem el valor del final
        Button btnquanta = (Button) findViewById(R.id.btnreg);
        btnquanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //amb aixo mirme si el boto funciona, en el android monitor sortira si lo donem al boto o no "filtrem per flx"
                Log.d("flx", "onClick()");
                //option + enter i importa
                Intent intent = new Intent(LoginActivity.this,Activity_Reg.class);
                startActivity(intent);
            }
        });
    }
    }

