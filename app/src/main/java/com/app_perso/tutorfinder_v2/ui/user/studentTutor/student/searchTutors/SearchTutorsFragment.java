package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.searchTutors;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementFragment;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapter;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapterCheckBox;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.SearchResultActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.util.AdminUtils;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchTutorsFragment extends Fragment implements SubjectAdapterCheckBox.ItemClickListener {
    private SubjectsManagementViewModel subjectsManagementViewModel;
    private List<String> newUserSubjectIds = new ArrayList<>();
    private Observer<List<Subject>> subjectsObserver = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        subjectsManagementViewModel =
                ViewModelProviders.of(this).get(SubjectsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_search_tutors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button searchOpt1 = (Button) view.findViewById(R.id.searchOpt1);
        Button searchOpt2 = (Button) view.findViewById(R.id.searchOpt2);
        RecyclerView subjectsRv = (RecyclerView) view.findViewById(R.id.subject_rv);
        Alphabetik alphabetik = (Alphabetik) view.findViewById(R.id.subject_sectionindex);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        User user;
        subjectsRv.setNestedScrollingEnabled(false);

        if (getActivity() instanceof StudentMainActivity) {
            user = Objects.requireNonNull(UserSingleton.getInstance(null).getUser());
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

        searchOpt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("USER_SUBJECT_IDS",(Serializable) newUserSubjectIds);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });

        subjectsObserver = new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable final List<Subject> subjects) {
                if (subjects.size() == 0) {
                    Toast.makeText(requireContext(), getResources().getString(R.string.error_no_subjects), Toast.LENGTH_SHORT).show();
                } else {
                    //sort subject list
                    Collections.sort(subjects);

                    //Set alphabet relevant with the subjects' names
                    String[] alphabet = AlphabetikUtils.getCustomAlphabetList(subjects);
                    alphabetik.setAlphabet(alphabet);

                    alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String character) {
                            subjectsRv.smoothScrollToPosition(AlphabetikUtils.getPositionFromData(character, subjects));
                        }
                    });

                    subjectsRv.setLayoutManager(layoutManager);
                    SubjectAdapterCheckBox subjectAdapter = new SubjectAdapterCheckBox(requireContext(),
                            subjects, new ArrayList<>(), false);
                    subjectAdapter.addItemClickListener(SearchTutorsFragment.this::onItemClick);
                    subjectsRv.setAdapter(subjectAdapter);

                    if (subjectsRv.getItemDecorationCount() == 0) {
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(subjectsRv.getContext(),
                                layoutManager.getOrientation());
                        subjectsRv.addItemDecoration(dividerItemDecoration);
                    }
                }
            }
        };

        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        subjectsManagementViewModel.getAllSubjects();
        subjectsManagementViewModel.getSubjects().observe(getViewLifecycleOwner(), subjectsObserver);
    }

    @Override
    public void onItemClick(Subject subject, boolean isChecked) {
        if (isChecked) {
            newUserSubjectIds.add(subject.getId());
        } else {
            newUserSubjectIds.remove(subject.getId());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        subjectsManagementViewModel.getSubjects().removeObserver(subjectsObserver);
    }
}