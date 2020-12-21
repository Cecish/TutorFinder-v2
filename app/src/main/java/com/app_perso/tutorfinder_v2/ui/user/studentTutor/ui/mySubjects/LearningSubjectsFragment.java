package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.mySubjects;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.tutor.TutorMainActivity;
import com.app_perso.tutorfinder_v2.util.ArrayUtils;

import java.util.Objects;

public class LearningSubjectsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_learning_needs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LearningSubjectsViewModel learningSubjectsViewModel =
                ViewModelProviders.of(this).get(LearningSubjectsViewModel.class);
        TextView learningNeedsTv = (TextView) view.findViewById(R.id.text_subjects);
        ViewFlipper viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper_subjects);
        RecyclerView subjectsRv = (RecyclerView) view.findViewById(R.id.subject_rv);
        Alphabetik alphabetik = (Alphabetik) view.findViewById(R.id.subject_sectionindex);
        User user;

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
        learningSubjectsViewModel.updateViewFlipperPos(ArrayUtils.isNullOrEmpty(user.getSubjectIds()));
        learningSubjectsViewModel.getViewFlipperPos().observe(
                getViewLifecycleOwner(),
                pos -> {
                    viewFlipper.setDisplayedChild(pos);

                    if (pos == 1) {
                        //TODO renderSubjects(user);
                    }
                }
        );
    }

    /**
     * Render the current user's list of subjects (learning needs for a student, tutoring subjects for a tutor)
     * @param populated_user current logged-in user
     */
    /*private void renderSubjects(User populated_user) {
        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        orderedSubjects = populated_user.getOrderedSubjects();

        // Display instructions on how to add subjects if the user's subject list is empty
        if (orderedSubjects.first.size() == 0) {
            instructions.setText(R.string.no_subjects_specified);
            //Manage visibility
            //Show instructions for adding subjects when the user has no subjects associated with his profile
            instructions.setVisibility(View.VISIBLE);
            //Hide alphabet scroller on the right side + List of item (indeed, a user can uncheck all his subjects and go back to view his profile)
            alphabetik.setVisibility(View.GONE);
            subjectsRv.setVisibility(View.GONE);

            // Alphabetik implementation & ListView population
        } else {
            //Handle visibility
            instructions.setVisibility(View.GONE); //Hide instructions for adding subjects when the user has no subjects associated with his profile
            alphabetik.setVisibility(View.VISIBLE); //Show alphabet scroller
            subjectsRv.setVisibility(View.VISIBLE); //Show list of subjects

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderedSubjects.first);
            subjectsRv.setAdapter(adapter);

            //Set alphabet relevant with the subjects' names
            alphabetik.setAlphabet(orderedSubjects.second);

            alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                @Override
                public void onItemClick(View view, int position, String character) {
                    String info = " Position = " + position + " Char = " + character;
                    Log.i("View: ", view + "," + info);
                    //Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                    subjectsRv.smoothScrollToPosition(Util.getPositionFromData(character, orderedSubjects.first));
                }
            });
        }
    }*/
}