package com.example.securemessenger;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton aesBtn, desBtn, md5Btn, rsaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#003459"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        aesBtn = findViewById(R.id.aesButton);
        desBtn = findViewById(R.id.desButton);
        md5Btn = findViewById(R.id.md5Button);
        rsaBtn = findViewById(R.id.rsaButton);

        aesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AES.class);
                startActivity(intent);
            }
        });
        desBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DES.class);
                startActivity(intent);
            }
        });
        md5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MD5.class);
                startActivity(intent);
            }
        });
        rsaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RSA.class);
                startActivity(intent);
            }
        });
    }
}