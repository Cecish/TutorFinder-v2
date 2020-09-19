package com.app_perso.tutorfinder_v2.view.signInSignUp;

import com.google.android.material.textfield.TextInputLayout;

public class SignInSignUpUtils {
    public final static String TAG = "TUTOR_FINDER_DEBUG";

    public static void resetErrorMessages(TextInputLayout... values) {
        for (TextInputLayout value : values) {
            value.setError(null);
        }
    }
}
