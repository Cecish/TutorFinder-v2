package com.app_perso.tutorfinder_v2.util;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


public class AnimationSplashScreenUtils {
    public final static int SPLASH_DISPLAY_LENGTH = 2500;
    private final static int APP_NAME_DELAY = 1000;

    public static void animateAppName(final TextView textView, final String text) {
        animateLogo(textView);

        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                // Actions to do after 3 seconds
                textView.setText(text);
            }
        }, APP_NAME_DELAY);
    }

    public static void animateLogo(View logo) {
        int delay = APP_NAME_DELAY;

        if (logo instanceof TextView) {
            delay = 0;
        }

        Animation anim = new TranslateAnimation(logo.getX(), logo.getX(), logo.getY(), logo.getY() - 50);
        anim.setFillAfter(true);
        anim.setDuration(delay);
        logo.startAnimation(anim);
    }
}
