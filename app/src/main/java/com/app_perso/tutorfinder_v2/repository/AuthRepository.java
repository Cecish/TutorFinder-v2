package com.app_perso.tutorfinder_v2.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.Status;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.SignInSignUpUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;
import java.util.Objects;

public class AuthRepository {
    private final String COLLECTION_NAME = "tutor_finder_users";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NAME);

    public void signUpUserFirebase(User user, @NonNull final OnSuccessListener successListener,
                                   @NonNull final OnFailureListener failureListener) throws RuntimeException {
        final FirebaseAuth finalFirebaseAuth = initFirebaseAuth();

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
                                                } else {
                                                    createdUser.setStatus(Status.NOT_APPLICABLE);
                                                }

                                                initCurrentUser(createdUser, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        firebaseAuth = null;
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
                }
            } else {
                failureListener.onFailure(Objects.requireNonNull(uidTask.getException()));
            }
        });
    }

    private void getUserInDb(final String userId, @NonNull final OnSuccessListener successListener,
                                 @NonNull final OnFailureListener failureListener) throws RuntimeException {

        if (userId == null) {
            throw new RuntimeException("Initialization user is null");
        }

        DocumentReference uidRef = collectionReference.document(userId);
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    successListener.onSuccess(document.getData());
                } else {
                    failureListener.onFailure(Objects.requireNonNull(uidTask.getException()));
                }
            } else {
                failureListener.onFailure(Objects.requireNonNull(uidTask.getException()));
            }
        });

    }

    //Use UnsupportedOperationException for fatal errors
    //Use InstantiationException for warnings
    public void signInUserFirebase(User user, @NonNull final OnSuccessListener successListener,
                                  @NonNull final OnFailureListener failureListener) {
        final FirebaseAuth finalFirebaseAuth = initFirebaseAuth();

        finalFirebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = finalFirebaseAuth.getCurrentUser();

                            //Retrieve corresponding user's info in the database
                            getUserInDbAux(firebaseUser, successListener, failureListener);
                        } else {
                            //problems with sign in
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getUserInDbAux(FirebaseUser firebaseUser, OnSuccessListener successListener, OnFailureListener failureListener) {
        getUserInDb(firebaseUser.getUid(), new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                User signedInUser = new User(firebaseUser);

                try {
                    Map<String, Object> userInfoInDb = (Map<String, Object>) o;
                    signedInUser.setUsername((String) userInfoInDb.get("username"));
                    signedInUser.setRole(Role.valueOf((String) userInfoInDb.get("role")));
                    signedInUser.setStatus(Status.valueOf((String) userInfoInDb.get("status")));

                    //User can sign in if email is verified or he/she is the admin
                    if (Objects.requireNonNull(firebaseUser).isEmailVerified() || signedInUser.getRole().equals(Role.ADMIN)) {
                        successListener.onSuccess(signedInUser);
                    } else {
                        Log.d(SignInSignUpUtils.TAG, "Email not verified!");
                        failureListener.onFailure(new InstantiationException("Warning email has not been verified yet"));
                    }

                } catch (Exception e) {
                    Log.d(SignInSignUpUtils.TAG, "Firestore document's casting failed");
                    failureListener.onFailure(Objects.requireNonNull(e));
                }
            }
        }, failureListener);
    }

    public void resetPasswordFirebase(String emailAddress, @NonNull final OnSuccessListener successListener,
                                      @NonNull final OnFailureListener failureListener) throws RuntimeException {
        final FirebaseAuth finalFirebaseAuth = initFirebaseAuth();

        finalFirebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            successListener.onSuccess(emailAddress);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private FirebaseAuth initFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }
}
