package com.app_perso.tutorfinder_v2.repository;

import androidx.lifecycle.MutableLiveData;

import com.app_perso.tutorfinder_v2.util.SignInSignUpUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection(SignInSignUpUtils.TAG);



    public MutableLiveData<FirebaseUser> checkIfUserIsAuthenticatedInFirebase() {
        MutableLiveData<FirebaseUser> isUserAuthenticateInFirebaseMutableLiveData = new MutableLiveData<>();

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        final FirebaseAuth finalFirebaseAuth = firebaseAuth;

        FirebaseUser firebaseUser = finalFirebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            isUserAuthenticateInFirebaseMutableLiveData.postValue(null);

        } else {
            isUserAuthenticateInFirebaseMutableLiveData.postValue(firebaseUser);
        }

        return isUserAuthenticateInFirebaseMutableLiveData;
    }
}
