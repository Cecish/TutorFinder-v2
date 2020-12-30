package com.app_perso.tutorfinder_v2.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.util.ArrayUtils;
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.StatusUser;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.SignInSignUpUtils;
import com.app_perso.tutorfinder_v2.util.UserUtils;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
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
                                                    createdUser.setStatus(StatusUser.NOT_VERIFIED);
                                                } else {
                                                    createdUser.setStatus(StatusUser.NOT_APPLICABLE);
                                                    createdUser.setSubjectIds(new ArrayList<>());
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
                    signedInUser.setStatus(StatusUser.valueOf((String) userInfoInDb.get("status")));

                    if (userInfoInDb.get("subjects") == null || (userInfoInDb.get("subjects") != null &&
                            Objects.requireNonNull(userInfoInDb.get("subjects")).toString().equals("[]"))) {
                        List<String> emptyList = new ArrayList<>();
                        emptyList.add("");
                        signedInUser.setSubjectIds(emptyList);
                    } else {
                        signedInUser.setSubjectIds((ArrayList<String>) userInfoInDb.get("subjects"));
                    }

                    //User can sign in if email is verified or he/she is the admin
                    if (Objects.requireNonNull(firebaseUser).isEmailVerified() || signedInUser.getRole().equals(Role.ADMIN)) {
                        if (signedInUser.getRole().equals(Role.TUTOR) && signedInUser.getStatus().equals(StatusUser.NOT_VERIFIED)) {
                            signedInUser.setStatus(StatusUser.PENDING);
                            collectionReference.document(signedInUser.getId()).update(signedInUser.genUserForDb());
                        }

                        //check if tutor has been accepted
                        if (signedInUser.getRole().equals(Role.TUTOR) && signedInUser.getStatus().equals(StatusUser.PENDING)){
                            failureListener.onFailure(new InstantiationException("Your tutor registration request is still pending"));
                        } else if (signedInUser.getRole().equals(Role.TUTOR) && signedInUser.getStatus().equals(StatusUser.DECLINED)) {
                            failureListener.onFailure(new InstantiationException("Your tutor registration request has been declined"));
                        } else {
                            successListener.onSuccess(signedInUser);
                        }

                    } else {
                        Log.d(SignInSignUpUtils.TAG, "Email not verified!");
                        failureListener.onFailure(new InstantiationException("Sign in failed: you need to verify your email address"));
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

    public void signOutFirebase() {
        firebaseAuth.signOut();
    }

    public void getAllPendingTutors(@NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReference
                .whereEqualTo("status", StatusUser.PENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(UserUtils.castToUser(document.getData()));
                            }
                            successListener.onSuccess(users);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getTutorsMatchingSubjects(List<String> subjectIds, @NonNull final OnSuccessListener successListener,
                                          @NonNull final OnFailureListener failureListener) {
        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<User> tutors = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = UserUtils.castToUser(document.getData());
                                if (user.getRole() == Role.TUTOR && !ArrayUtils.isNullOrEmpty(user.getSubjectIds()) &&
                                        ArrayUtils.intersectionListStr(subjectIds, user.getSubjectIds()).size() > 0) {
                                    tutors.add(UserUtils.castToUser(document.getData()));
                                }
                            }
                            successListener.onSuccess(tutors);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }
}
