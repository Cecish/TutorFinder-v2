package com.app_perso.tutorfinder_v2.util;

import android.view.View;
import android.widget.ViewFlipper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminUtils {

    public static void configViewFlipper(ViewFlipper viewFlipper, FloatingActionButton fab, int displayedChild) {
        viewFlipper.setVisibility(View.INVISIBLE);
        if (fab != null)
            fab.setVisibility(View.INVISIBLE);

        viewFlipper.setDisplayedChild(displayedChild);
        viewFlipper.postDelayed(new Runnable() {
            public void run() {
                viewFlipper.setVisibility(View.VISIBLE);
                if (fab != null)
                    fab.setVisibility(View.VISIBLE);
            }
        }, 250);
    }
}
