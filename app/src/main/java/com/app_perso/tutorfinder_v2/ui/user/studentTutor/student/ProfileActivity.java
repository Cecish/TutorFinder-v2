package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphabetik.Alphabetik;
import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.adapter.SubjectAdapter;
import com.app_perso.tutorfinder_v2.util.AlphabetikUtils;
import com.app_perso.tutorfinder_v2.util.FirestoreUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {
    private SignInSignUpViewModel signInSignUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameTv = (TextView) findViewById(R.id.username);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        Alphabetik alphabetik = (Alphabetik) findViewById(R.id.subject_sectionindex);
        RecyclerView subjectsRv = (RecyclerView) findViewById(R.id.subject_rv);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);
        SubjectsManagementViewModel subjectsManagementViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SubjectsManagementViewModel.class);

        User tutor = Objects.requireNonNull(getIntent().getExtras()).getParcelable("tutorUser");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_white_arrow_back_24);
        }

        //Populate profile info
        usernameTv.setText(Objects.requireNonNull(tutor).getUsername());
        FirestoreUtils.loadProfilePicture(profilePic, tutor.getId(), this);

        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        subjectsManagementViewModel.getSubjects(tutor.getSubjectIds());
        subjectsManagementViewModel.getSubjectsSelection().observe(
                this,
                (Observer<List<Subject>>) subjects -> {
                    if (subjects.size() == 0) {
                        Toast.makeText(this, getResources().getString(R.string.error_no_subjects), Toast.LENGTH_SHORT).show();
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
                        SubjectAdapter subjectAdapter = new SubjectAdapter(this, subjects);
                        subjectsRv.setAdapter(subjectAdapter);

                        if (subjectsRv.getItemDecorationCount() == 0) {
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(subjectsRv.getContext(),
                                    layoutManager.getOrientation());
                            subjectsRv.addItemDecoration(dividerItemDecoration);
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_log_out_option:
                //Logout
                signInSignUpViewModel.signOut();
                startActivity(new Intent(ProfileActivity.this, SignInSignUpActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}