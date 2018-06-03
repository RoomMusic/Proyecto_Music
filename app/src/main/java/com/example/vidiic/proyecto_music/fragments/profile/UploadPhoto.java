package com.example.vidiic.proyecto_music.fragments.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadPhoto extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_upload_photo, btn_select_photo;
    ImageView uploaded_image;
    FirebaseStorage user_image_storage;
    StorageReference image_reference;
    FirebaseFirestore firebaseFirestore;
    UserApp userApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        toolbar = findViewById(R.id.toolbar);
        btn_upload_photo = findViewById(R.id.btn_upload_photo);
        btn_select_photo = findViewById(R.id.btn_select_photo);
        uploaded_image = findViewById(R.id.photo_uploaded);
        user_image_storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("RooMusic");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_select_photo.setOnClickListener(v -> {
            //pedimos al usuario una foto de la galera
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        });

        btn_upload_photo.setOnClickListener(v -> uploadPhoto());

        firebaseFirestore.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    userApp = documentSnapshot.toObject(UserApp.class);
                }
            }
        });
    }

    //subimos las fotos a firestorage para tenerlas guardadas en la base ded atos y no en los dispositivos
    private void uploadPhoto() {

        String image_name = selectedImage.getPath().split("/")[4];

        //ruta para guardar la image
        image_reference = user_image_storage.getReference().child(userApp.getEmail() + "/pictures/" + image_name + ".jpg");

        userApp.setUserImage(image_name);

        //actualizar usuario en la base de datos ya que hemos actualizado el nombre de la imagen
        updateUser(userApp);

        image_reference.putFile(selectedImage);

        Toast.makeText(this, getResources().getString(R.string.UploadPhotoSucces), Toast.LENGTH_SHORT).show();

        finish();
    }

    private Uri selectedImage;

    private void updateUser(UserApp userApp){
        firebaseFirestore.collection("users").document(userApp.getUserid()).set(userApp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            selectedImage = data.getData();
            Log.d("upload", "path foto: " + selectedImage.getPath().split("/")[4]);
            uploaded_image.setImageURI(selectedImage);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            //cerramos upload photo
            finish();

        return super.onOptionsItemSelected(item);
    }
}
