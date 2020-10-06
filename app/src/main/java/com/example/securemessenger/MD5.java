package com.example.securemessenger;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 extends AppCompatActivity {
    Button enc, share, reset;
    EditText inputView;
    TextView outputView;

    String inputText;
    String outputText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        /**INPUT- OUTPUT FIELD**/
        inputView = findViewById(R.id.inputView);
        outputView = findViewById(R.id.outputView);
        /**BUTTONS**/
        enc = findViewById(R.id.encryptBtn);
        share = findViewById(R.id.shareBtn);
        reset = findViewById(R.id.resetBtn);

        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = inputView.getText().toString();
                try {
                    if (!TextUtils.isEmpty(inputText)) {
                        inputText = inputView.getText().toString();
                        outputText = md5_encrypt(inputText);
                        outputView.setText(outputText);
                        //Toast.makeText(MD5.this, "Encrypt Button Working", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MD5.this, "Input field empty!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputView.setText(null);
                    outputText = null;
                    outputView.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(outputText)) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, outputText);
                        shareIntent.setType("text/plain");
                        startActivity(shareIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MD5.this, "Output Message Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String md5_encrypt(String inputText) throws NoSuchAlgorithmException {
        //Create MD5 Hash
        try {
            //Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            byte[] byteText = inputText.getBytes();
            digest.update(byteText);
            byte[] messageDigest = digest.digest();
            //Create a Hexadecimal String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}