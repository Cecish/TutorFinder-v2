package com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app_perso.tutorfinder_v2.R;

public class SessionRequestsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_session_requests, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        return root;
    }
}
