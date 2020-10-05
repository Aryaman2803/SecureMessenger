package com.example.securemessenger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA extends AppCompatActivity {
    Button enc, dec, share, reset;
    EditText inputView;
    TextView outputView;

    String inputText;
    String outputText = "";

    public static KeyPair getKeyPair() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kp;
    }

    public static String encryptRSAToString(String clearText, String publicKey) {
        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");//new line or carriage return replace kar and send kr
    }

    public static String decryptRSAToString(String encryptedBase64, String privateKey) {

        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //BUTTONS
        enc = findViewById(R.id.encrpytBtn);
        dec = findViewById(R.id.decryptBtn);
        reset = findViewById(R.id.resetBtn);
        share = findViewById(R.id.shareBtn);

        //INPUT OUTPUT FIELDS
        inputView = findViewById(R.id.inputView);
        outputView = findViewById(R.id.outputView);
        // generate a new public/private key pair to test with (note. you should only do this once and keep them!)
        KeyPair kp = getKeyPair();

        PublicKey publicKey = kp.getPublic();
        final byte[] publicKeyBytes = publicKey.getEncoded();
        final String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

        PrivateKey privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        final String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));


        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = inputView.getText().toString();
                if (!TextUtils.isEmpty(inputText)) {
                    outputText = encryptRSAToString(inputText, publicKeyBytesBase64);
                    outputView.setText(outputText);
                    Toast.makeText(RSA.this, "Encrypt Button Workin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RSA.this, "Input Field Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = inputView.getText().toString();
                if (!TextUtils.isEmpty(inputText)) {
                    outputText = decryptRSAToString(inputText, privateKeyBytesBase64);
                    outputView.setText(outputText);
                } else {
                    Toast.makeText(RSA.this, "Input Field Empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputView.setText(null);
                    outputView.setText(null);
                    outputText = null;
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
                    Toast.makeText(RSA.this, "Output Message Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}