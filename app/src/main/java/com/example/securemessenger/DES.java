package com.example.securemessenger;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES extends AppCompatActivity {
    /**
     * ALL FOR KEY GENERATION
     **/
    public static final String DES = "DES";
    public static final String UNICODE_FORMAT = "UTF8";
    public static final String myEncKey = "PASSWORD_FOR_KEY";
    Button enc, dec, share, reset;
    EditText inputView;
    TextView outputView;
    String inputText;
    String outputText = "";
    byte[] KeyAsBytes;
    SecretKey key;
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    private String myEncryptionKey;
    private String getMyEncryptionScheme;

    private static String bytes2String(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);

        }
        return stringBuffer.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);
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
        enc = findViewById(R.id.encrpytBtn);
        dec = findViewById(R.id.decryptBtn);
        reset = findViewById(R.id.resetBtn);
        share = findViewById(R.id.shareBtn);

        inputView = findViewById(R.id.inputView);
        outputView = findViewById(R.id.outputView);

        /**INITIALIZING KEY GENERATION PART**/
        myEncryptionKey = myEncKey;
        getMyEncryptionScheme = DES;
        try {
            KeyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            myKeySpec = new DESKeySpec(KeyAsBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            mySecretKeyFactory = SecretKeyFactory.getInstance(DES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher = Cipher.getInstance(DES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            key = mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = inputView.getText().toString();
                if (!TextUtils.isEmpty(inputText)) {
                    outputText = encrypt(inputText);
                    //Log.d("CHECK ", "MY KEY" + outputText);
                    outputView.setText(outputText);
                    //Toast.makeText(DES.this, "Encrypt Button working!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DES.this, "Output Field Empty!", Toast.LENGTH_SHORT).show();
                }


                // Toast.makeText(DES.this, "Input Field Empty! ", Toast.LENGTH_SHORT).show();

            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputText = inputView.getText().toString();

                    if (TextUtils.isEmpty(inputText)) {
                        Toast.makeText(DES.this, "Input Field Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        outputText = decrypt(inputText);
                        outputView.setText(outputText);
                        //Toast.makeText(DES.this, "Decrypt Button Working!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DES.this, "Output Message Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public String decrypt(String encrytedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encrytedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {

        }
        return decryptedText;
    }

    private String encrypt(String inputText) {
        String encryptedValue = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = inputText.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedValue = Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {

        }

        return encryptedValue;
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
