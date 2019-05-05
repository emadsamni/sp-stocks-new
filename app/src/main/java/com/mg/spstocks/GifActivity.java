package com.mg.spstocks;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pl.droidsonroids.gif.GifImageView;


public class GifActivity extends AppCompatActivity {
    private static final String TAG = "GifActivity";
    private static int TIME_OUT = 3000;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        gifImageView= (GifImageView)findViewById(R.id.gifImage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(GifActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

    }

    private String getKeyHash() {
        PackageInfo info;
        String keyHash = "";
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e1) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }

        return keyHash;

    }
}
