package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.admin.SubjectsManagementViewModel;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.adapter.MatchingTutorsAdapter;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.searchTutors.SearchTutorsViewModel;
import com.app_perso.tutorfinder_v2.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class SearchResultActivity extends AppCompatActivity implements MatchingTutorsAdapter.ItemClickListener {
    private SearchTutorsViewModel searchTutorsViewModel;
    private SignInSignUpViewModel signInSignUpViewModel;
    private SubjectsManagementViewModel subjectsManagementViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        RecyclerView matchingTutorsRv = (RecyclerView) findViewById(R.id.matched_tutors_rv);
        ViewFlipper viewFlipperMatchingTutors = (ViewFlipper) findViewById(R.id.viewFlipperTutorsMatching);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_white_arrow_back_24);
        }

        searchTutorsViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SearchTutorsViewModel.class);
        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);
        subjectsManagementViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SubjectsManagementViewModel.class);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<String> userSubjectIds = (ArrayList<String>) Objects.requireNonNull(args).getSerializable("USER_SUBJECT_IDS");

        searchTutorsViewModel.searchTutors(userSubjectIds);

        searchTutorsViewModel.getOutcome().observe(this, outcome -> {
            if (outcome != null && !outcome.isEmpty()) {
                Toast.makeText(this, outcome, Toast.LENGTH_SHORT).show();
                searchTutorsViewModel.setOutcome("");
            }
        });

        if (ArrayUtils.isNullOrEmpty(userSubjectIds)) {
            viewFlipperMatchingTutors.setDisplayedChild(0);
        } else {
            searchTutorsViewModel.getMatchingTutors().observe(this, matchingTutors -> {
                viewFlipperMatchingTutors.setDisplayedChild((matchingTutors.size() > 0)? 1 : 0);

                final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

                //sort tutors list (by alphabetical order)
                Collections.sort(matchingTutors);

                subjectsManagementViewModel.getSubjects(userSubjectIds);
                subjectsManagementViewModel.getSubjectsSelection().observe(this, subjectsSelection -> {
                    Collections.sort(subjectsSelection);
                    matchingTutorsRv.setLayoutManager(layoutManager);
                    MatchingTutorsAdapter matchingTutorsAdapter = new MatchingTutorsAdapter(this, matchingTutors, subjectsSelection);
                    matchingTutorsAdapter.addItemClickListener(this);
                    matchingTutorsRv.setAdapter(matchingTutorsAdapter);

                    if (matchingTutorsRv.getItemDecorationCount() == 0) {
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(matchingTutorsRv.getContext(),
                                layoutManager.getOrientation());
                        matchingTutorsRv.addItemDecoration(dividerItemDecoration);
                    }
                });
            });
        }

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
                startActivity(new Intent(SearchResultActivity.this, SignInSignUpActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position) {
        //TODO in FR-S10
    }
}