package com.app_perso.tutorfinder_v2.ui.user.student.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.util.Objects;

public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HomeViewModel homeViewModel;
        User user;
        TextView usernameTv = (TextView) view.findViewById(R.id.username);
        ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        user = ((StudentMainActivity) Objects.requireNonNull(getActivity())).user;

        //Populate profile info
        usernameTv.setText(user.getUsername());
        FirestoreUtils.loadProfilePicture(profilePic, user.getId(), getContext());
    }
}