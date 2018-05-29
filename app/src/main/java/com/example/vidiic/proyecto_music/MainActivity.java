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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.asynctasks.AsyncTaskSong;
import com.example.vidiic.proyecto_music.fragments.Fragment_Home;
import com.example.vidiic.proyecto_music.fragments.Fragment_ListSong;
import com.example.vidiic.proyecto_music.fragments.Fragment_Profile;
import com.example.vidiic.proyecto_music.fragments.Fragment_Share;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Fragment_ListSong.OnFragmentInteractionListener{

    private Fragment_ListSong fragment_listSong;
    private Fragment_Home fragment_home;
    private Fragment_Share fragment_share;
    private Fragment_Profile fragment_profile;


    private BottomNavigationView nMainNav;
    private FrameLayout frameLayoutMain;

    private static final int MY_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_listSong = new Fragment_ListSong();
        fragment_home = new Fragment_Home();
        fragment_share = new Fragment_Share();
        fragment_profile = new Fragment_Profile();

        nMainNav = (BottomNavigationView)findViewById(R.id.main_nav);
        frameLayoutMain = (FrameLayout) findViewById(R.id.contenedorFragment);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment_listSong).commit();
        }

        nMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home :
                        nMainNav.setItemBackgroundResource(R.color.rojologo);
                        setFragment(fragment_home);
                        return true;

                    case R.id.nav_music :
                        nMainNav.setItemBackgroundResource(R.color.rojologo);
                        setFragment(fragment_listSong);
                        return true;

                    case R.id.nav_profile :
                        nMainNav.setItemBackgroundResource(R.color.rojologo);
                        setFragment(fragment_profile);
                        return true;

                    case R.id.nav_share :
                        nMainNav.setItemBackgroundResource(R.color.rojologo);
                        setFragment(fragment_share);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"PermissionGranted",Toast.LENGTH_SHORT).show();
                        Log.i("Main","Entramos2");
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, fragment_listSong).commit();
                    }
                }else {
                    Toast.makeText(this,"PermissionDeneged",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
