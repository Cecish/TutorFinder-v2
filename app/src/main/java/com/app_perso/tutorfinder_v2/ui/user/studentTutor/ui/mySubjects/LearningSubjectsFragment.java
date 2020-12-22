package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.mySubjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapterCheckBox;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.TutorMainActivity;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.util.ArrayUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class LearningSubjectsFragment extends Fragment implements SubjectAdapterCheckBox.ItemClickListener {
    private SubjectsManagementViewModel subjectsManagementViewModel;
    private List<String> userSubjectIds = new ArrayList<>();
    private List<String> newUserSubjectIds = new ArrayList<>();
    private FloatingActionButton saveFab;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        subjectsManagementViewModel =
                ViewModelProviders.of(this).get(SubjectsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_learning_needs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LearningSubjectsViewModel learningSubjectsViewModel =
                ViewModelProviders.of(this).get(LearningSubjectsViewModel.class);
        SwitchMaterial editToggle = (SwitchMaterial) view.findViewById(R.id.edit_toggle);
        TextView learningNeedsTv = (TextView) view.findViewById(R.id.text_subjects);
        ViewFlipper viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper_subjects);
        RecyclerView subjectsRv = (RecyclerView) view.findViewById(R.id.subject_rv);
        Alphabetik alphabetik = (Alphabetik) view.findViewById(R.id.subject_sectionindex);
        saveFab = (FloatingActionButton) view.findViewById(R.id.fabSave);
        User user;
        boolean hasSubjects;

        if (getActivity() instanceof StudentMainActivity) {
            user = ((StudentMainActivity) requireActivity()).user;
            learningNeedsTv.setText(getString(R.string.my_learning_needs));
        } else if (getActivity() instanceof TutorMainActivity) {
            user = ((TutorMainActivity) requireActivity()).user;
            learningNeedsTv.setText(getString(R.string.my_teaching_subjects));
        } else {
            throw new IllegalStateException("User not found");
        }

        //Render the user's subjects (learning needs for subjects vs tutoring subjects for tutors) if any
        userSubjectIds = user.getSubjectIds();
        newUserSubjectIds = ArrayUtils.copyOf(userSubjectIds);
        hasSubjects = ArrayUtils.isNullOrEmpty(userSubjectIds);
        learningSubjectsViewModel.updateViewFlipperPos(hasSubjects);
        learningSubjectsViewModel.getViewFlipperPos().observe(
                getViewLifecycleOwner(),
                pos -> {
                    viewFlipper.setDisplayedChild(pos);

                    if (pos == 1) {
                        renderSubjects(userSubjectIds, alphabetik, subjectsRv);
                    }
                }
        );

        //Handle edit subjects button
        editToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (viewFlipper.getDisplayedChild() == 0) {
                        learningSubjectsViewModel.updateViewFlipperPos(false);
                    }
                } else {
                    if (ArrayUtils.isNullOrEmpty(user.getSubjectIds())) {
                        learningSubjectsViewModel.updateViewFlipperPos(true);
                    }
                }
            }
        });

        //Saving user's selection to the cloud hosted db
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setSubjectIds(newUserSubjectIds);
                learningSubjectsViewModel.updateUserinRemoteDb(user);
            }
        });
    }

    /**
     * Render the current user's list of subjects (learning needs for a student, tutoring subjects for a tutor)
     * @param userSubjectIds subject ids of the current logged-in user
     */
    private void renderSubjects(List<String> userSubjectIds, Alphabetik alphabetik, RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        subjectsManagementViewModel.getAllSubjects();
        subjectsManagementViewModel.getSubjects().observe(
                getViewLifecycleOwner(),
                (Observer<List<Subject>>) subjects -> {
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
                                recyclerView.smoothScrollToPosition(AlphabetikUtils.getPositionFromData(character, subjects));
                            }
                        });

                        recyclerView.setLayoutManager(layoutManager);
                        SubjectAdapterCheckBox subjectAdapter = new SubjectAdapterCheckBox(requireContext(), subjects, new ArrayList<>());
                        subjectAdapter.addItemClickListener(this);
                        recyclerView.setAdapter(subjectAdapter);

                        if (recyclerView.getItemDecorationCount() == 0) {
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                    layoutManager.getOrientation());
                            recyclerView.addItemDecoration(dividerItemDecoration);
                        }
                    }
                }
        );
    }

    @Override
    public void onItemClick(Subject subject, boolean isChecked) {
        if (isChecked) {
            newUserSubjectIds.add(subject.getId());
        } else {
            newUserSubjectIds.remove(subject.getId());
        }

        //Handle save fab visibility
        if (!userSubjectIds.equals(newUserSubjectIds)) {
            saveFab.setVisibility(View.VISIBLE);
        } else {
            saveFab.setVisibility(View.GONE);
        }
    }
}