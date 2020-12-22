package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.searchTutors.SearchTutorsViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResultActivity extends AppCompatActivity {
    private SearchTutorsViewModel searchTutorsViewModel;
    private SignInSignUpViewModel signInSignUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_white_arrow_back_24);
        }

        searchTutorsViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SearchTutorsViewModel.class);
        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);

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

        searchTutorsViewModel.getMatchingTutors().observe(this, matchingTutors -> {
            Log.d("CECILE", matchingTutors.toString());
        });

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
}