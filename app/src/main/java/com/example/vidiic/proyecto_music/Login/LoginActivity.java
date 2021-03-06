package com.example.vidiic.proyecto_music.Login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.vidiic.proyecto_music.MainActivity;
import com.example.vidiic.proyecto_music.R;

import com.example.vidiic.proyecto_music.fragments.Fragment_Home;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

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

    private Button loginBtn, btnSignUp;
    private EditText usernameET, passwordET;
    private FirebaseAuth firebaseAuth;
    private Fragment_Home fragment_home;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;


    @Override
    protected void onStart() {
        loginBtn.setEnabled(true);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        loginBtn = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.signUpBtn);
        usernameET = findViewById(R.id.userEmail);
        fragment_home = new Fragment_Home();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        passwordET = findViewById(R.id.passwordText);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash


//        usernameET.setText("stucomtest@gmail.com");
//        passwordET.setText("ssoo++");

        loginBtn.setEnabled(true);

        loginBtn.setOnClickListener(v -> {
            progressDialog.setTitle(R.string.LoginLoading);
            progressDialog.show();
            //deshabilitmaos el boton para que no puedan hacer doble click
            loginBtn.setEnabled(false);
            //comprobamos que el usuario esta en la base de datos
            registerUser();
        });


        //delcarem el valor del final
        btnSignUp = (Button) findViewById(R.id.signUpBtn);

        btnSignUp.setOnClickListener(view -> {
            //amb aixo mirme si el boto funciona, en el android monitor sortira si lo donem al boto o no "filtrem per flx"
            Log.d("loginactivity", "onClick()");
            //option + enter i importa
            Intent intent = new Intent(LoginActivity.this, Activity_Reg.class);
            startActivity(intent);
        });

    }

    private void registerUser() {

        final String email = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            loginBtn.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            loginBtn.setEnabled(true);
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //el usuario existe en la bbbd, correcto
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.LoginSuccess), Toast.LENGTH_SHORT).show();

                //setFragment(fragment_home);
                progressDialog.dismiss();

                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                mainIntent.putExtra("useremail", email);

                startActivity(mainIntent);
            } else {

                try {
                    throw task.getException();

                } catch (FirebaseAuthInvalidUserException invalidemail) {
                    //usuario no registrado, redirigir al signup
                    Intent intent = new Intent(LoginActivity.this, Activity_Reg.class);

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.LoginFail), Toast.LENGTH_SHORT).show();

                    startActivity(intent);

                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.LoginPassFail), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
                finally {
                    loginBtn.setEnabled(true);
                }
            }
        });
    }


}
