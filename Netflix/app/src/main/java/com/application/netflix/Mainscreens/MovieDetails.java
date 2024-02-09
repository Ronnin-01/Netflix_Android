package com.application.netflix.Mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.netflix.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MovieDetails extends AppCompatActivity {
    ImageView movieImage;
    TextView movieName;
    Button Play;
    String name,image,fileurl,movieid;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieImage=findViewById(R.id.imagedeatils);
        movieName=findViewById(R.id.moviename);
        Play=findViewById(R.id.playbutton);
        movieid=getIntent().getStringExtra("movieId");
        fileurl=getIntent().getStringExtra("movieFile");
        image=getIntent().getStringExtra("movieImageUrl");
        name=getIntent().getStringExtra("movieName");
        Glide.with(this).load(image).into(movieImage);
        movieName.setText(name);
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieDetails.this,VideoPlayer.class);
                i.putExtra("url",fileurl);
                startActivity(i);
            }
        });


    }
}