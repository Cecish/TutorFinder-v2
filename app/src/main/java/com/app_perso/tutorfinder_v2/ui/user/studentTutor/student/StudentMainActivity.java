package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.UserSingleton;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class StudentMainActivity extends AppCompatActivity {
    private SignInSignUpViewModel signInSignUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        if (savedInstanceState != null){
            UserSingleton.getInstance(savedInstanceState.getParcelable("currentUser"));
        }

        BottomNavigationView navView = findViewById(R.id.navigation);
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,
                R.id.navigation_learning_subjects, R.id.navigation_search, R.id.navigation_chat).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        signInSignUpViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInSignUpViewModel.class);
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
            UserSingleton.reset();
            signInSignUpViewModel.signOut();
            startActivity(new Intent(StudentMainActivity.this, SignInSignUpActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onSaveInstanceState(@NonNull Bundle mBundle) {
        super.onSaveInstanceState(mBundle);
        mBundle.putParcelable("currentUser", Objects.requireNonNull(UserSingleton.getInstance(null).getUser()));
    }
}