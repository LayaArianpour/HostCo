package com.example.hostco;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imgLogoSplashScreen;
    private ObjectAnimator animator;
    private int delay=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imgLogoSplashScreen=findViewById(R.id.imgLogoSplashScreen);
        animator=ObjectAnimator.ofFloat(imgLogoSplashScreen,"translationY",-2000);
        animator.setDuration(delay);
        animator.start();

        new CountDownTimer(1500, 1) {
            public void onFinish() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(SplashScreenActivity.this,UserTypeSelectorActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashScreenActivity.this, StartActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(SplashScreenActivity.this,UserTypeSelectorActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }
}