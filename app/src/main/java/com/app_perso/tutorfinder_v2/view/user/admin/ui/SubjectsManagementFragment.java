package com.app_perso.tutorfinder_v2.view.user.admin.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.util.AdminUtils;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.view.user.admin.adapter.SubjectAdapter;
import com.app_perso.tutorfinder_v2.viewModel.SubjectsManagementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;


public class SubjectsManagementFragment extends Fragment implements SubjectAdapter.ItemClickListener {

    private SubjectsManagementViewModel subjectsManagementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subjectsManagementViewModel =
                ViewModelProviders.of(this).get(SubjectsManagementViewModel.class);

        return inflater.inflate(R.layout.fragment_subjects_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ViewFlipper viewFlipper = view.findViewById(R.id.view_flipper);
        final Alphabetik alphabetik = view.findViewById(R.id.admin_subject_sectionindex);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.admin_subject_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        final FloatingActionButton fabAddSubject = view.findViewById(R.id.fab_add_subject);

        subjectsManagementViewModel.getAllSubjects();
        subjectsManagementViewModel.getSubjects().observe(
                getViewLifecycleOwner(),
                (Observer<List<Subject>>) subjects -> {
                    if (subjects.size() == 0) {
                        AdminUtils.configViewFlipper(viewFlipper, fabAddSubject, 0);
                    } else {
                        AdminUtils.configViewFlipper(viewFlipper, fabAddSubject, 1);

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
                        SubjectAdapter subjectAdapter = new SubjectAdapter(requireContext(), subjects);
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

        subjectsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), (Observer<String>) it -> {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show();
        });

        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open a dialog window
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(getResources().getString(R.string.add_subject));
                EditText input = (EditText) LayoutInflater.from(getContext()).inflate(R.layout.text_input_subject, (ViewGroup) getView(), false);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        subjectsManagementViewModel.addSubject(input.getText().toString());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Log.d("CECILE", subjectsManagementViewModel.subjects.getValue().get(position).getName());
    }
}