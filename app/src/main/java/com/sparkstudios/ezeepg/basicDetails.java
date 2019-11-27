package com.sparkstudios.ezeepg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.sparkstudios.ezeepg.home_activit_files.MainActivity;

public class basicDetails extends AppCompatActivity {
private String gender;
    private int t;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = preferences.edit();
    Intent i = getIntent();
   t =  i.getIntExtra("t",0);
     t++;
    }




    public void rfemale(View view) {
        gender = "Girls";
        Intent i = new Intent(this, MainActivity.class);
        editor.putInt("t",t);
        editor.putString("gender",gender);
        editor.commit();
        startActivity(i);


    }

    public void rmale(View view) {
        gender = "Boys";
        Intent i = new Intent(this, MainActivity.class);
        editor.putInt("t",t);
        editor.putString("gender",gender);
        editor.commit();
        startActivity(i);
    }
}
