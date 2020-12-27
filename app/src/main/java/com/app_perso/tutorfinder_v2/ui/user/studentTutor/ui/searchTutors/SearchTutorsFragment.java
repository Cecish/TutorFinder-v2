package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.searchTutors;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.SearchResultActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;

import java.io.Serializable;

public class SearchTutorsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_tutors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button searchOpt1 = (Button) view.findViewById(R.id.searchOpt1);
        User user;

        if (getActivity() instanceof StudentMainActivity) {
            user = ((StudentMainActivity) requireActivity()).user;
        } else {
            throw new IllegalStateException("User not found");
        }

        searchOpt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("USER_SUBJECT_IDS",(Serializable) user.getSubjectIds());
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });
    }
}