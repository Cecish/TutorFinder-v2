package com.app_perso.tutorfinder_v2.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirestoreUtils {
    public static final int RESULT_LOAD_IMAGE = 10;

    public static void loadProfilePicture(ImageView iv, String userId, Context context) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageRef.child("images/" + userId + ".jpg");

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(context /* context */)
                .load(profilePicRef)
                .into(iv);
    }
}
