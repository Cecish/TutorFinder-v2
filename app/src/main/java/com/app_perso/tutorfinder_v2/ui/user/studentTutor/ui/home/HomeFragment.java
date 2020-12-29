package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.TutorMainActivity;
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
    private Observer<Boolean> refreshProfilePicObserver = null;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

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

        if (getActivity() instanceof StudentMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else if (getActivity() instanceof TutorMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else {
            throw new IllegalStateException("User not found");
        }

        refreshProfilePicObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {
                FirestoreUtils.loadProfilePicture(profilePic, user.getId(), getContext());
            }
        };

        //Populate profile info
        usernameTv.setText(user.getUsername());
        FirestoreUtils.loadProfilePicture(profilePic, user.getId(), getContext());

        //Refresh profile pic when a new one is uploaded
        homeViewModel.getRefreshProfilePic().observe(getViewLifecycleOwner(), refreshProfilePicObserver);

        //Edit profile picture
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissionReadExternalStorage(requireContext())) {
                    editProfilePic();
                }
            }
        });
    }

    private void editProfilePic() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    private boolean checkPermissionReadExternalStorage(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                editProfilePic();
            } else {
                Toast.makeText(requireContext(), "Permission READ EXTERNAL STORAGE denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Context context = getContext();

        if (reqCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri);
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

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        homeViewModel.getRefreshProfilePic().removeObserver(refreshProfilePicObserver);
    }
}