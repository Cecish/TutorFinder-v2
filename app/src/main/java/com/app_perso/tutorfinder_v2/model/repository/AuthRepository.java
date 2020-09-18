package com.app_perso.tutorfinder_v2.model.repository;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.model.Role;
import com.app_perso.tutorfinder_v2.model.Status;
import com.app_perso.tutorfinder_v2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void signUpUserFirebase(User user, @NonNull final OnSuccessListener successListener,
                                   @NonNull final OnFailureListener failureListener) throws RuntimeException {

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        final FirebaseAuth finalFirebaseAuth = firebaseAuth;

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Objects.requireNonNull(finalFirebaseAuth.getCurrentUser()).sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //verification email was sent
                                                User createdUser = new User(finalFirebaseAuth.getCurrentUser());
                                                createdUser.setUsername(user.getUsername());
                                                createdUser.setRole(user.getRole());

                                                if (user.getRole().equals(Role.TUTOR)) {
                                                    createdUser.setStatus(Status.PENDING);
                                                }
                                                successListener.onSuccess(createdUser);

                                            } else {
                                                //problems with sending verification email
                                                failureListener.onFailure(new UnsupportedOperationException("Error sending the email verification"));
                                            }
                                        }
                                    });
                        } else {
                            failureListener.onFailure(task.getException());
                        }
                    }
                });
    }
}
