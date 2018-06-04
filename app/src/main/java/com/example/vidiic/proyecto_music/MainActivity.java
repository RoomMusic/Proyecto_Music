package com.example.vidiic.proyecto_music;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.classes.UserApp;
import com.example.vidiic.proyecto_music.fragments.Fragment_Home;
import com.example.vidiic.proyecto_music.fragments.profile.Fragment_Profile;
import com.example.vidiic.proyecto_music.fragments.Fragment_Settings;
import com.example.vidiic.proyecto_music.fragments.music.Fragment_ListSong;
import com.example.vidiic.proyecto_music.fragments.music.Fragment_Music;
import com.example.vidiic.proyecto_music.fragments.social.muro.Fragment_Muro;
import com.example.vidiic.proyecto_music.fragments.social.Fragment_Social;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sendbird.android.SendBird;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements Fragment_Music.OnFragmentInteractionListener, Fragment_Social.OnFragmentInteractionListener, Fragment_Muro.OnFragmentInteractionListener {

    private Fragment_ListSong fragment_listSong;
    private Fragment_Home fragment_home;
    private Fragment_Social fragment_social;
    private Fragment_Music fragment_music;
    private Fragment_Profile fragment_profile;
    private Fragment_Settings fragment_settings;

    private BottomNavigationView nMainNav;
    private FrameLayout frameLayoutMain;

    private static final int MY_PERMISSION_REQUEST = 1;
    private static final String APP_ID = "60DA930F-248F-479A-B406-028DEF5060D7";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private String userEmail, userKey;
    private UserApp userAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_listSong = new Fragment_ListSong();
        fragment_home = new Fragment_Home();
        fragment_social = new Fragment_Social();
        fragment_music = new Fragment_Music();
        fragment_profile = new Fragment_Profile();
        fragment_settings = new Fragment_Settings();


        //inicializamos el servicio de mensajeria una vez el usuario entra en la app
        SendBird.init(APP_ID, this.getApplicationContext());

        //inicializamos los objetos necesrios para la conexion con Firebase
        firebaseFirestore = FirebaseFirestore.getInstance(); //base de datos cloudfirestore
        firebaseAuth = FirebaseAuth.getInstance(); //para poder obtener las credenciales del usuario actual
        firebaseStorage = FirebaseStorage.getInstance(); //obtenemos conexion al sistema de almacenamiento de FireBase


        //recogemos el email pasado desde el login
        userEmail = getIntent().getExtras().getString("useremail");

        //obtenemos la clave del usuario
        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //buscamos el usuario a traves de la clave que podemos recoger utilizando FirebaseAuth
        firebaseFirestore.collection("users").document(userKey).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userAux = task.getResult().toObject(UserApp.class);
                Log.d("sergio", "NICKNAME: " + userAux.getUserName());
            } else {
                Log.d("sergio", "no existe");
            }
        });

        checkUser(userKey);


        nMainNav = findViewById(R.id.main_nav);
        BottomNavigationViewHelper.disableShiftMode(nMainNav);
        nMainNav.setSelectedItemId(R.id.nav_home);

        frameLayoutMain = findViewById(R.id.contenedorFragment);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment_home).commit();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment_home).commit();
        }

        nMainNav.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    setFragment(fragment_home);
                    return true;

                case R.id.nav_music:
                    setFragment(fragment_music);
                    return true;

                case R.id.nav_share:
                    //Log.d("test", "MainACtivityButton");
                    setFragment(fragment_social);
                    return true;
                case R.id.nav_perfil:
                    //Log.d("test", "MainACtivityButton");
                    setFragment(fragment_profile);
                    return true;

                default:
                    return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, getResources().getString(R.string.MainPermisoSi), Toast.LENGTH_SHORT).show();
                        Log.i("Main", "Entramos2");
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment_listSong).commit();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.MainPermisoNo), Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //sobreescribimos la funcion de on back pressed y hacemos return para cuando el usuario pulse atras no pueda volver al login si no es pulsando log out
//    @Override
//    public void onBackPressed() {
//
//        return;
//    }

    private void checkUser(String userid) {
        //con esta sentencia obtenemos de la coleccion de usuario el documento con el email del usuario el cual contiene los datos de este
        firebaseFirestore.collection("users").document(userid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                //obtenemos los datos del usurio en un map
                Map<String, Object> map = task.getResult().getData();

                //Log.d("sergio", "" + task.getResult().getData().get("firstIn"));


                //comprobamos si el usuario ya ha entrado antes o no
                boolean check = (boolean) map.get("firstIn");

                //Log.d("sergio", map.get("email").toString());

                //comprobar si el usuario ya habia entrado
                //actualizamos el usuario
                if (!check) {
                    //si no ha entrado obtenemos las canciones de su movil y las guardamos en la bbdd
                    Log.d("sergio", "no ha entrado");

                    //actualizamos el campo firstIn a TRUE
                    firebaseFirestore.collection("users").document(map.get("userid").toString()).
                            update("firstIn", true).
                            addOnSuccessListener(aVoid -> Log.d("sergio", "Campo actualizado")).
                            addOnFailureListener(e -> Log.d("sergio", "Error al actualizar"));

                } else {
                    //si ya ha entrado, obtenemos las canciones de la base de datos
                    Log.d("sergio", "si ha entrado");


                }
            }
        });
    }
}
