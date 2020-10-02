package com.example.flash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        ImageButton imgbtn=(ImageButton)findViewById(R.id.imageButton5);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadnewActivity= new Intent(Home.this,NewActivity.class);
                EditText Het=(EditText)findViewById(R.id.url4);
                String url=Het.getText().toString().trim();
                loadnewActivity.putExtra("url",url);
                startActivity(loadnewActivity);

            }
        });
    }

}