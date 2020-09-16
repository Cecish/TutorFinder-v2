package com.app_perso.tutorfinder_v2.view.splashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_perso.tutorfinder_v2.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView logo = (ImageView) findViewById(R.id.appLogo);
        final TextView textView =  (TextView) findViewById(R.id.appName);

        /* New Handler to start the main app activity and close this splash screen after some seconds
         * Duration of wait (i.e. time of splash screen display):2.5 seconds
         * If a task is processing in the background and takes >= 4s, need to notify users in the splash screen!
         */

        AnimationSplashScreenUtils.animateLogo(logo);
        AnimationSplashScreenUtils.delayNextScreen(SplashScreenActivity.this);
        AnimationSplashScreenUtils.animateAppName(textView, getString(R.string.app_name));
    }
}