package com.app_perso.tutorfinder_v2.repository;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public FirebaseUser getUserAuthenticatedInFirebase() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        final FirebaseAuth finalFirebaseAuth = firebaseAuth;

        return finalFirebaseAuth.getCurrentUser();
    }
}
