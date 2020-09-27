package com.example.securemessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES extends AppCompatActivity {

    Button enc, dec, share, reset;
    EditText inputView;
    TextView outputView;

    String inputText;
    String outputText = "";
    public static String pass = "PASSWORD_FOR_KEY";
    String AES = "AES";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //BUTTONS
        enc = (Button) findViewById(R.id.encrpytBtn);
        dec = (Button) findViewById(R.id.decryptBtn);
        reset = (Button) findViewById(R.id.resetBtn);
        share = (Button) findViewById(R.id.shareBtn);

        //INPUT OUTPUT FIELDS
        inputView = (EditText) findViewById(R.id.inputView);
        outputView = (TextView) findViewById(R.id.outputView);

        // Start Encrytping
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputText = inputView.getText().toString();
                    if (!TextUtils.isEmpty(inputText)) {
                        outputText = encrypt(inputText, pass);
                        outputView.setText(outputText);
                        Toast.makeText(AES.this, "Encrypt Button working", Toast.LENGTH_SHORT).show();
                        System.out.println("Message: " + inputText);
                    } else {
                        Toast.makeText(AES.this, "Input Field Empty", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(AES.this, "Button not working", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputText = inputView.getText().toString();
                    if (!TextUtils.isEmpty(inputText)) {
                        outputText = decrypt(inputText, pass);
                        outputView.setText(outputText);
                        Toast.makeText(AES.this, "Decrypt Button working", Toast.LENGTH_SHORT).show();
                        System.out.println("Message: " + outputText);
                    } else {
                        Toast.makeText(AES.this, "Input Field Empty", Toast.LENGTH_SHORT).show();

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
                    inputView.setText("");
                    outputView.setText("");
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
                    Toast.makeText(AES.this, "Output Message Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String decrypt(String inputText, String pass) throws Exception {
        SecretKey secretKey = generateKey(pass);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedValue = Base64.decode(inputText, Base64.DEFAULT);
        byte[] decVal = cipher.doFinal(decodedValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }

    private String encrypt(String inputText, String pass) throws Exception {
        SecretKey secretKey = generateKey(pass);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encVal = cipher.doFinal(inputText.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    /**
     * It will generate Random Hash Key and return to the secretKeySpec
     **/
    private SecretKey generateKey(String pass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = pass.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
        return secretKeySpec;
    }

    /**
     * onOptionsItemSelected shows Back option in Activity ActionBar
     **/
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