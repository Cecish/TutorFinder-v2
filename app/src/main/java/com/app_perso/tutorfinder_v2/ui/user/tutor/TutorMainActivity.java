package com.app_perso.tutorfinder_v2.ui.user.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.app_perso.tutorfinder_v2.ui.user.student.ui.chat.ChatFragment;
import com.app_perso.tutorfinder_v2.ui.user.student.ui.home.HomeFragment;
import com.app_perso.tutorfinder_v2.ui.user.student.ui.learningSubjects.LearningSubjectsFragment;
import com.app_perso.tutorfinder_v2.ui.user.student.ui.searchTutors.SearchTutorsFragment;
import com.app_perso.tutorfinder_v2.util.Role;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class TutorMainActivity extends AppCompatActivity {
    private SignInSignUpViewModel signInSignUpViewModel;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        User user = Objects.requireNonNull(getIntent().getExtras()).getParcelable("AuthenticatedUser");
        mActionBar = getSupportActionBar();

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);

        setPortalName(Objects.requireNonNull(user).getRole());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_log_out_option) {
            //Logout
            signInSignUpViewModel.signOut();
            startActivity(new Intent(TutorMainActivity.this, SignInSignUpActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPortalName(Role role) {
        switch (role) {
            case STUDENT:
                mActionBar.setTitle(R.string.student_portal);
                break;

            case TUTOR:
                mActionBar.setTitle(R.string.tutor_portal);
                break;

            default:
                throw new IllegalArgumentException("User with role " + role.toString() + " cannot access this portal!");
        }
    }
}