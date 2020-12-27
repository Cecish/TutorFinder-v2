package com.app_perso.tutorfinder_v2.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DatabaseHelper {
    private final String COLLECTION_USERS = "tutor_finder_users";
    private final String COLLECTION_SUBJECTS = "tutor_finder_subjects";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceUsers = firebaseFirestore.collection(COLLECTION_USERS);
    private CollectionReference collectionReferenceSubjects = firebaseFirestore.collection(COLLECTION_SUBJECTS);

    //if the entity is present in the database update the record, else create new record
    public void upsertUser(final User user, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        DocumentReference uidRef;

        if (user.getId() == null) {
            uidRef = collectionReferenceUsers.document();
        } else {
            uidRef = collectionReferenceUsers.document(user.getId());
        }

        uidRef.set(user.genUserForDb())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void upsertSubject(final Subject subject, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        DocumentReference uidRef;

        if (subject.getId() == null) {
            uidRef = collectionReferenceSubjects.document();
            subject.setId(uidRef.getId());
        } else {
            uidRef = collectionReferenceSubjects.document(subject.getId());
        }

        uidRef.set(subject.genSubjectForDb())
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(subject);
                    }
                })
                .addOnFailureListener(failureListener);
    }

    public void addSubject(final String subjectName, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereEqualTo("name", subjectName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() == 0) {
                                //Add new subject
                                upsertSubject(new Subject(subjectName), successListener, failureListener);

                            } else {
                                successListener.onSuccess("Subject " + subjectName + " is already added");
                            }
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void updateSubject(final Subject subject, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereEqualTo("name", subject.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() == 0) {
                                //Add new subject
                                upsertSubject(subject, successListener, failureListener);

                            } else {
                                successListener.onSuccess("Subject " + subject.getName() + " is already added");
                            }
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getAllSubjects(@NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Subject> subjects = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subjects.add(castToSubject(document.getData()));
                            }
                            successListener.onSuccess(subjects);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getSubjectsById(List<String> subjectIds, @NonNull final OnSuccessListener successListener,
                                    @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereIn("id", subjectIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Subject> subjects = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subjects.add(castToSubject(document.getData()));
                            }
                            successListener.onSuccess(subjects);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private Subject castToSubject(Map<String, Object> map) {
        try {
            return new Subject(
                    Objects.requireNonNull(map.get("id")).toString(),
                    Objects.requireNonNull(map.get("name")).toString()
            );

        } catch(Exception e) {
            Log.d("ERROR", "Map Firebase document data is incorrect");
            throw e;
        }
    }
}
