package com.app_perso.tutorfinder_v2.util;

import com.google.android.material.textfield.TextInputLayout;

public class SignInSignUpUtils {
    public final static String TAG = "TUTOR_FINDER_DEBUG";
    public final static String SIGNED_IN = "SIGNED_IN";

    public static void resetErrorMessages(TextInputLayout... values) {
        for (TextInputLayout value : values) {
            value.setError(null);
        }
    }
}
