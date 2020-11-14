package com.app_perso.tutorfinder_v2.ui.user.student.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.app_perso.tutorfinder_v2.util.FirestoreUtils.RESULT_LOAD_IMAGE;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private ImageView profilePic;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView usernameTv = (TextView) view.findViewById(R.id.username);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        ImageView editProfilePic = (ImageView) view.findViewById(R.id.edit_profile_pic);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        user = ((StudentMainActivity) Objects.requireNonNull(getActivity())).user;

        //Populate profile info
        usernameTv.setText(user.getUsername());
        FirestoreUtils.loadProfilePicture(profilePic, user.getId(), getContext());

        //Edit profile picture
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Context context = getContext();

        if (reqCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePic.setImageBitmap(selectedImage);

                //Rescale image
                profilePic.getLayoutParams().height = (int) getResources().getDimension(R.dimen.height_profile_pic);
                profilePic.getLayoutParams().height = (int) getResources().getDimension(R.dimen.height_profile_pic);

                //Save the new current user's profile picture to Firestore
                homeViewModel.uploadImage(imageUri, user.getId(), context);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(context, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}