package com.example.vidiic.proyecto_music.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.vidiic.proyecto_music.Home.HomeActivity;
import com.example.vidiic.proyecto_music.Login.LoginActivity;
import com.example.vidiic.proyecto_music.MainActivity;
import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by poldominguez on 28/5/18.
 */

public class Activity_Launcher extends AppCompatActivity {

    //variable para saber si el usuario ya ha entrado a la app
    Intent nextIntent;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        nextIntent = new Intent(this, HomeActivity.class);
        Thread timer = new Thread() {

            public void run() {

                try {

                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {

                    //if (isFirstRun) {
                        startActivity(nextIntent);

                        finish();
                    //} else {

                        //si es diferente de null quiere decir que sigue loggeado
//                        if (firebaseUser != null) {
//
//                            //obtener el correo del usuario por id
//                            firebaseFirestore.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        UserApp u = documentSnapshot.toObject(UserApp.class);
//
//                                        Log.d("sergio", "email: " + u.getEmail());
//
//                                        Intent intent = new Intent(Activity_Launcher.this, MainActivity.class);
//
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                                        intent.putExtra("email", u.getEmail());
//
//                                        startActivity(intent);
//
//                                    }
//                                }
//                            });
//                        } else {
//                            nextIntent = new Intent(Activity_Launcher.this, LoginActivity.class);
//                            startActivity(nextIntent);
//                        }
                    //}

                    //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();


                }

            }
        };
        timer.start();
    }
}
