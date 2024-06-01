package com.bunny.weather.weago.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.bunny.weather.WeaGo.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private boolean keep = true;
    ObjectAnimator slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(SplashActivity.this);
        splashScreen.setKeepOnScreenCondition(() -> keep);
        Handler handler = new Handler(Looper.getMainLooper());
        int DELAY = 900;
        handler.postDelayed(() -> keep = false, DELAY);

        getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
            // Slide out animation
            slideOut = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_X,
                    0f,
                    splashScreenView.getWidth());
            slideOut.setInterpolator(new AccelerateInterpolator());
            slideOut.setDuration(800);

            nextStep();

            // Call SplashScreenView.remove at the end of your custom animation.
            slideOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenView.remove();
                }
            });
            slideOut.start();
        });
    }

    private void nextStep() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
