package com.example.vidiic.proyecto_music.fragments.profile;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    EditText edit_password1, edit_password2;
    Button btn_change_pass;
    String pass1, pass2;
    FirebaseUser firebaseUser;
    Fragment_Profile fragment_profile;
    Toolbar toolbar_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edit_password1 = findViewById(R.id.password1);
        edit_password2 = findViewById(R.id.password2);
        btn_change_pass = findViewById(R.id.confirm_change_password);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fragment_profile = new Fragment_Profile();
        toolbar_pass = findViewById(R.id.toolbar_password);

        setSupportActionBar(toolbar_pass);

        getSupportActionBar().setTitle(R.string.ChangePassTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_change_pass.setOnClickListener(v -> {

            pass1 = edit_password1.getText().toString().trim();
            pass2 = edit_password2.getText().toString().trim();

            if (!pass1.isEmpty() && !pass2.isEmpty()) {
                //si no estan vacios los campos, comrpobamos que sean iguales
                if (pass1.equals(pass2)) {
                    //si las contraseñas coinciden las cambiamos
                    firebaseUser.updatePassword(pass1).addOnCompleteListener(task -> {
                        //Toast.makeText(ChangePassword.this, "Exito", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            finish();
                            getSupportFragmentManager().beginTransaction().replace(R.id.relative_change_password, fragment_profile).commit();
                        }
                    }).addOnFailureListener(e ->
                            Toast.makeText(ChangePassword.this, getResources().getString(R.string.pass_size), Toast.LENGTH_SHORT).show());

                } else {
                    Toast.makeText(ChangePassword.this, getResources().getString(R.string.pass_no_match), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePassword.this, getResources().getString(R.string.pass_empty), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            //cerramos upload photo
            finish();

        return super.onOptionsItemSelected(item);
    }
}
