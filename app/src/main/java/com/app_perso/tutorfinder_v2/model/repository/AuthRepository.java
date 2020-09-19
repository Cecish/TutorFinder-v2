package com.app_perso.tutorfinder_v2.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app_perso.tutorfinder_v2.model.Role;
import com.app_perso.tutorfinder_v2.model.Status;
import com.app_perso.tutorfinder_v2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Objects;

public class AuthRepository {
    private final String COLLECTION_NAME = "tutor_finder_users";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NAME);

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

                                                initCurrentUser(createdUser, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        successListener.onSuccess(createdUser);
                                                    }
                                                },failureListener);

                                            } else {
                                                //problems with sending verification email
                                                failureListener.onFailure(new UnsupportedOperationException("Error sending the email verification"));
                                            }
                                        }
                                    });
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    //Acts as an initializer
    private void initCurrentUser(final User user, @NonNull final OnSuccessListener successListener,
                                                  @NonNull final OnFailureListener failureListener) throws RuntimeException {

        if (user == null) {
            throw new RuntimeException("Initialization user is null");
        }

        DocumentReference uidRef = collectionReference.document(user.getId());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (!Objects.requireNonNull(document).exists()) {
                    uidRef.set(user.genUserForDb()).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            successListener.onSuccess(user);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(userCreationTask.getException()));
                        }
                    });
                } else {
                    //Todo: sessions
                }
            } else {
                failureListener.onFailure(Objects.requireNonNull(uidTask.getException()));
            }
        });
    }

    //Use UnsupportedOperationException for fatal errors
    //Use InstantiationException for warnings
    public void signinUserFirebase(User user, @NonNull final OnSuccessListener successListener,
                                  @NonNull final OnFailureListener failureListener) {

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        final FirebaseAuth finalFirebaseAuth = firebaseAuth;

        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            User signedInUser;

                            //check if email wasn't verified unless the user is an admin
                            if (!Objects.requireNonNull(firebaseUser).isEmailVerified()) {
                                signedInUser = new User(firebaseUser);
                                signedInUser.setRole(user.getRole());
                                successListener.onSuccess(signedInUser);
                            } else {
                                failureListener.onFailure(new InstantiationException("Warning email has not been verified yet"));
                            }
                        } else {
                            //problems with sign in
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

}
