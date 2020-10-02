package com.example.flash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class Downloads extends AppCompatActivity {
    public static ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);

    }
}