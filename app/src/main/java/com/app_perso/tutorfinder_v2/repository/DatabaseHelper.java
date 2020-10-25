package com.app_perso.tutorfinder_v2.repository;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {
    private final String COLLECTION_NAME = "tutor_finder_users";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NAME);

    //if the entity is present in the database update the record, else create new record
    public void upsertUser(final User user, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener){
        DocumentReference uidRef;

        if (user.getId() == null) {
            uidRef = collectionReference.document();
        } else {
            uidRef = collectionReference.document(user.getId());
        }

        uidRef.set(user.genUserForDb())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
