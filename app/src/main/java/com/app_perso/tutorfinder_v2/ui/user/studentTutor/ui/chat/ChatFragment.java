package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions.SessionsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.TutorMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.home.HomeViewModel;

import java.util.Objects;

public class ChatFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChatViewModel chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        final ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper_chat);
        User user;

        if (getActivity() instanceof StudentMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else if (getActivity() instanceof TutorMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
        } else {
            throw new IllegalStateException("User not found");
        }


    }
}