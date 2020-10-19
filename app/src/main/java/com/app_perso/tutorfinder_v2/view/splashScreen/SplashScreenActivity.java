package com.app_perso.tutorfinder_v2.view.splashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.AnimationSplashScreenUtils;
import com.app_perso.tutorfinder_v2.view.user.admin.AdminHomeActivity;
import com.app_perso.tutorfinder_v2.view.user.studentOrTutor.StudentTutorHomeActivity;
import com.app_perso.tutorfinder_v2.view.signInSignUp.ui.SignInSignUpActivity;
import com.app_perso.tutorfinder_v2.viewModel.SplashViewModel;

public class SplashScreenActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView logo = (ImageView) findViewById(R.id.appLogo);
        final TextView textView =  (TextView) findViewById(R.id.appName);

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        AnimationSplashScreenUtils.animateLogo(logo);
        AnimationSplashScreenUtils.animateAppName(textView, getString(R.string.app_name));

        /* New Handler to start the main app activity and close this splash screen after some seconds
         * Duration of wait (i.e. time of splash screen display):2.5 seconds
         * If a task is processing in the background and takes >= 4s, need to notify users in the splash screen!
         */
        new Handler().postDelayed(() -> {
            //checkIfUserIsAuthenticated(); TODO

            if (intent != null) {
                startActivity(intent);
            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInSignUpActivity.class));
            }
            finish();
        }, AnimationSplashScreenUtils.SPLASH_DISPLAY_LENGTH);

    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.getIsUserAuthenticatedLiveData().observe(this, firebaseUser -> {

            if (firebaseUser == null) { //i.e. user is not authenticated
                intent = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);

            } else {
                splashViewModel.getAuthenticatedUser(firebaseUser);
                splashViewModel.getAuthenticatedUserLiveData().observe(this, user -> {
                    if (user == null) {
                        intent = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);

                    } else if ((user.getRole().equals(Role.STUDENT)) || (user.getRole().equals(Role.TUTOR))) {
                        intent = new Intent(SplashScreenActivity.this, StudentTutorHomeActivity.class);
                        intent.putExtra("AuthenticatedUser", user);

                    } else if (user.getRole().equals(Role.ADMIN)) {
                        intent = new Intent(SplashScreenActivity.this, AdminHomeActivity.class);
                        intent.putExtra("AuthenticatedUser", user);
                    }
                });
            }
        });
    }
}