package com.example.vidiic.proyecto_music.Login;

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

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.AppUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

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

    private EditText userNameEditText, userEmailEditText, passwordET1, passwordET2;
    private Button signUpBtn;
    private String key;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        userNameEditText = findViewById(R.id.userNameSign);
        userEmailEditText = findViewById(R.id.userEmailSign);
        passwordET1 = findViewById(R.id.passwordSign1);
        passwordET2 = findViewById(R.id.passwordSign2);
        signUpBtn = findViewById(R.id.btnSignUp);

        userNameEditText.setText("stucom");
        userEmailEditText.setText("stucomtest@gmail.com");
        passwordET1.setText("ssoo++");
        passwordET2.setText("ssoo++");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(v ->
                registerUser()
        );

    }

    private void registerUser() {

        final String userName = userNameEditText.getText().toString().trim();
        final String userMail = userEmailEditText.getText().toString().trim();
        final String userPass = passwordET1.getText().toString().trim();
        String userPassRep = passwordET2.getText().toString().trim();

        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(userMail) && TextUtils.isEmpty(userPass) && TextUtils.isEmpty(userPassRep)) {
            Toast.makeText(Activity_Reg.this, "There are empty fields", Toast.LENGTH_SHORT).show();
        } else if (!userPass.equals(userPassRep)) {
            Toast.makeText(Activity_Reg.this, "Password fields does not match.", Toast.LENGTH_SHORT).show();
        } else {

            firebaseAuth.createUserWithEmailAndPassword(userMail, userPass).addOnCompleteListener(task -> {

                //los usuarios pueden registrarse con una contraseña
                if (task.isSuccessful()) {
                    //usuario registrado completamente
                    Intent intent = new Intent(Activity_Reg.this, LoginActivity.class);
                    startActivity(intent);

                    saveUser(userMail, userPass);

                    Toast.makeText(Activity_Reg.this, "User Registered Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("signup", "error: " + task.getException());
                    //usuario ya registrado
                    Toast.makeText(Activity_Reg.this, "User is already registered.", Toast.LENGTH_SHORT).show();
                }

            });
        }

    }

    private void saveUser(String email, String pass) {

        key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AppUser user = new AppUser(key, email, pass, new Date(), false);

        firebaseFirestore.collection("users").document(key).set(user).addOnSuccessListener(aVoid ->
                Toast.makeText(Activity_Reg.this, "Usuario registrado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                Toast.makeText(Activity_Reg.this, "Fallo", Toast.LENGTH_SHORT).show());

    }
}