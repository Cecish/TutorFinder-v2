package com.app_perso.tutorfinder_v2.ui.user.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.util.CombinedLiveData2;
import com.app_perso.tutorfinder_v2.util.ViewFlipperUtils;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.util.DialogUtils;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class SubjectsManagementFragment extends Fragment implements SubjectAdapter.ItemClickListener {
    private Fragment mFragment;
    private SubjectsManagementViewModel subjectsManagementViewModel;
    private Observer<List<Subject>> subjectsObserver = null;
    private Observer<String> outcomeObserver = null;

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
        mFragment = this;

        subjectsObserver = new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable final List<Subject> subjects) {
                if (Objects.requireNonNull(subjects).size() == 0) {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, fabAddSubject, 0);
                } else {
                    ViewFlipperUtils.configViewFlipper(viewFlipper, fabAddSubject, 1);

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
                    subjectAdapter.addItemClickListener(SubjectsManagementFragment.this::onItemClick);
                    recyclerView.setAdapter(subjectAdapter);

                    if (recyclerView.getItemDecorationCount() == 0) {
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                layoutManager.getOrientation());
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }

                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            //Remove swiped item from list and notify the RecyclerView
                            int position = viewHolder.getAdapterPosition();
                            String subjectId = subjects.get(position).getId();
                            subjectsManagementViewModel.deleteSubject(subjectId);

                            //Get email addresses of students and tutors having sessions that match sessions in the present or future
                            subjectsManagementViewModel.getEmailsOfUsersWithAcceptedSessions(subjectId);

                            //Get email addresses of students and tutors having the subject to delete in their teaching or tutoring needs list
                            subjectsManagementViewModel.getEmailsOfUsersListingSubject(subjectId);

                            (new CombinedLiveData2(subjectsManagementViewModel.getEmailsSessions(),
                                    subjectsManagementViewModel.getEmailsSubjects())).observe(getViewLifecycleOwner(), new Observer<Pair<Set<String>, List<String>>>() {
                                @Override
                                public void onChanged(Pair<Set<String>, List<String>> pairSetList) {
                                    Set<String> emails = pairSetList.first;
                                    emails.addAll(pairSetList.second);

                                    //List<String> emailList = new ArrayList<>(chatBuddiesIds);
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("message/rfc822");
                                    i.putExtra(Intent.EXTRA_EMAIL  , emails.toArray(new String[emails.size()]));
                                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_deletion, subjectId));
                                    i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.subject_deletion_body, subjectId));
                                    try {
                                        startActivity(Intent.createChooser(i, "Send mail..."));
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(requireContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            subjects.remove(position);
                            subjectAdapter.notifyDataSetChanged();
                            subjectsManagementViewModel.setSubjects(subjects);
                        }
                    };

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            }
        };

        outcomeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String outcome) {
                Toast.makeText(requireContext(), outcome, Toast.LENGTH_LONG).show();
            }
        };

        subjectsManagementViewModel.getAllSubjects();
        subjectsManagementViewModel.getSubjects().observe(getViewLifecycleOwner(), subjectsObserver);

        subjectsManagementViewModel.getOutcome().observe(getViewLifecycleOwner(), outcomeObserver);

        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.buildDialog(requireContext(), R.string.add_subject, R.string.add, mFragment, subjectsManagementViewModel, null);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        DialogUtils.buildDialog(requireContext(), R.string.edit_subject, R.string.edit, mFragment, subjectsManagementViewModel,
                Objects.requireNonNull(subjectsManagementViewModel.subjects.getValue()).get(position));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        subjectsManagementViewModel.getSubjects().removeObserver(subjectsObserver);
        subjectsManagementViewModel.getOutcome().removeObserver(outcomeObserver);
    }
}