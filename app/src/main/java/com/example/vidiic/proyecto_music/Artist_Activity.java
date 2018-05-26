package com.example.vidiic.proyecto_music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Artist_Activity extends AppCompatActivity {

    private TextView name,desc,cat;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_);

        name = findViewById(R.id.artist_name);
        desc = findViewById(R.id.artist_desc);
        cat = findViewById(R.id.artist_cat);
        img = findViewById(R.id.artist_image);

        Intent intent = getIntent();
        String nameArtist = intent.getExtras().getString("NameArtist");
        String descArtist = intent.getExtras().getString("DescriptionArtist");
        String catArtist = intent.getExtras().getString("CategoryArtist");
        int imgArtist = intent.getExtras().getInt("ImageArtist");

        name.setText(nameArtist);
        desc.setText(descArtist);
        cat.setText(catArtist);
        img.setImageResource(imgArtist);

    }
}
