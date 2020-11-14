package com.app_perso.tutorfinder_v2.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.app_perso.tutorfinder_v2.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirestoreUtils {
    public static final int RESULT_LOAD_IMAGE = 10;

    public static void loadProfilePicture(ImageView iv, String userId, Context context) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageRef.child("images/" + userId + ".jpg");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                // Download directly from StorageReference using Glide
                Glide.with(context /* context */)
                        .load(downloadUrl)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .circleCrop()
                        .into(iv);
            }
        });
    }
}
