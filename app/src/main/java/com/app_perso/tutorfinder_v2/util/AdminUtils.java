package com.app_perso.tutorfinder_v2.util;

import android.view.View;
import android.widget.ViewFlipper;

public class AdminUtils {

    public static void configViewFlipper(ViewFlipper viewFlipper, int displayedChild) {
        viewFlipper.setVisibility(View.INVISIBLE);
        viewFlipper.setDisplayedChild(displayedChild);
        viewFlipper.postDelayed(new Runnable() {
            public void run() {
                viewFlipper.setVisibility(View.VISIBLE);
            }
        }, 250);
    }
}
