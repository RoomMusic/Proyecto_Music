package com.example.vidiic.proyecto_music.fragments.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RequestCredentials extends AppCompatActivity {

    EditText edit_user_email, edit_password;
    Button btn_continuar;
    String email, password;
    FirebaseUser firebaseUser;
    Toolbar toolbar_credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_credentials);

        edit_user_email = findViewById(R.id.user_correo);
        edit_password = findViewById(R.id.user_old_password);
        btn_continuar = findViewById(R.id.btn_check_credentials);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar_credentials = findViewById(R.id.toolbar_credentials);


        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setSupportActionBar(toolbar_credentials);

        getSupportActionBar().setTitle(R.string.ChangePassTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = firebaseUser.getEmail();

        edit_user_email.setText(email);
        edit_user_email.setEnabled(false);

        btn_continuar.setOnClickListener(v -> {

            password = edit_password.getText().toString().trim();

            if (!password.isEmpty()) checkCredentials(password);
            else Toast.makeText(RequestCredentials.this, getResources().getString(R.string.pass_empty), Toast.LENGTH_SHORT).show();
        });

    }


    private void checkCredentials(String pass){

        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);

        firebaseUser.reauthenticate(credential).addOnSuccessListener(aVoid -> {
            finish();
            startActivity(new Intent(RequestCredentials.this, ChangePassword.class));
            //Toast.makeText(RequestCredentials.this, "Exito", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Toast.makeText(RequestCredentials.this, getResources().getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            //cerramos upload photo
            finish();

        return super.onOptionsItemSelected(item);
    }
}
