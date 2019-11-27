package com.sparkstudios.ezeepg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.sparkstudios.ezeepg.home_activit_files.MainActivity;
import com.squareup.picasso.Picasso;

public class splash_screen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.app_splash);
        Picasso.get().load(R.drawable.logo_splash).into(imageView);
        SharedPreferences spreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        final int t =  spreferences.getInt("t",0);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(t==0){
                    Intent i = new Intent(getApplicationContext(), basicDetails.class);

                    startActivity(i);

                    finish();
                }
                else{
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }}
        }, SPLASH_TIME_OUT);
    }
    }

