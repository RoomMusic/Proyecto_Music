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
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.example.vidiic.proyecto_music.musicFireBase.Sync_Music_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private UserApp userAux = null;
    private List<UserApp> listUser = new ArrayList<>();

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


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        signUpBtn.setOnClickListener(v ->
                registerUser()
        );

        //obtenemos todos los usuarios disponibles
        firebaseFirestore.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    userAux = snapshot.toObject(UserApp.class);
                    listUser.add(userAux);
                    Log.d("sergio", "usernameee: " + userAux.getEmail());

                }
            }

        });

    }

    private void registerUser() {

        final String userName = userNameEditText.getText().toString().trim();
        final String userMail = userEmailEditText.getText().toString().trim();
        final String userPass = passwordET1.getText().toString().trim();
        String userPassRep = passwordET2.getText().toString().trim();

        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(userMail) && TextUtils.isEmpty(userPass) && TextUtils.isEmpty(userPassRep)) {
            Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignEmptyField), Toast.LENGTH_SHORT).show();
        } else if (!userPass.equals(userPassRep)) {
            Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignPasswordNoMatch), Toast.LENGTH_SHORT).show();
        } else {

            //si es true quiere decir que el nombre de usuario no existe
            if (saveUser(userName, userMail, userPass)) {

                firebaseAuth.createUserWithEmailAndPassword(userMail, userPass).addOnCompleteListener(task -> {

                    //los usuarios pueden registrarse con una contraseña
                    if (task.isSuccessful()) {

                        //usuario registrado completamente
                        Intent intent = new Intent(Activity_Reg.this, Sync_Music_Activity.class);

                        intent.putExtra("useremail", userMail);
                        intent.putExtra("username", userName);


                        key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        UserApp user = new UserApp(key, userName, userMail, userPass, new Date(), false);


                        firebaseFirestore.collection("users").document(key).set(user).addOnSuccessListener(aVoid ->
                                Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignUserRegistered), Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignFail), Toast.LENGTH_SHORT).show());

                        startActivity(intent);


                        //Toast.makeText(Activity_Reg.this, "User Registered Succesfully", Toast.LENGTH_SHORT).show();

                    } else {
                        //Log.d("signup", "error: " + task.getException());

                        try{
                            throw task.getException();
                        }
                        catch(FirebaseAuthWeakPasswordException weakException){
                            Toast.makeText(Activity_Reg.this, getResources().getString(R.string.WeakPass), Toast.LENGTH_SHORT).show();
                        }
                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                        {
                            Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignMalformedEmail), Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthUserCollisionException existEmail)
                        {
                            //usuario ya registrado
                            Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignUserAlreadyRegistered), Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception e)
                        {

                        }

                    }

                });
            } else {
                //el nombre de usuario ya existe, avisamos al usuario
                Toast.makeText(Activity_Reg.this, getResources().getString(R.string.SignUsernameExists), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private boolean checkUserName(String username) {

        int i = 0;

        for (UserApp u : listUser) {
            if (u.getUserName().equals(username)) {
                i = 1;
            }
        }


        //si obtenemos diferente de null quiere decir que ese nombre de usuario ya existe
        Log.d("sergio", "llegooooooo");
        if (i == 1) return true;

        else return false;

        //si no devolvemos false, es decir que el nombre de usuario no existe en la base de datos


    }


    private boolean saveUser(String userName, String email, String pass) {

        if (checkUserName(userName)) {
            //si es true quiere decir que el nombre de usuario existe, devolvemos false para avisar al usuario
            Log.d("sergio", "nombre de usuario existe");
            return false;
        } else {
            //si es false quiere decir que esta libre y que el usuario se ha registrado correctamente en la base datos, y dirigimos el usuario al login
            Log.d("sergio", "nombre de usuario no existe");

            return true;
        }

    }
}