package com.app_perso.tutorfinder_v2.ui.splashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.ui.user.tutor.TutorMainActivity;
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.AnimationSplashScreenUtils;
import com.app_perso.tutorfinder_v2.ui.user.admin.AdminHomeActivity;
import com.app_perso.tutorfinder_v2.ui.user.student.StudentMainActivity;
import com.app_perso.tutorfinder_v2.ui.signInSignUp.SignInSignUpActivity;
import com.google.firebase.auth.FirebaseUser;

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

        checkIfUserIsAuthenticated();

        /* New Handler to start the main app activity and close this splash screen after some seconds
         * Duration of wait (i.e. time of splash screen display):2.5 seconds
         * If a task is processing in the background and takes >= 4s, need to notify users in the splash screen!
         */
        new Handler().postDelayed(() -> {
            if (intent != null) {
                startActivity(intent);
            } else {
                startActivity(new Intent(SplashScreenActivity.this, SignInSignUpActivity.class));
            }
            finish();
        }, AnimationSplashScreenUtils.SPLASH_DISPLAY_LENGTH);

    }

    private void checkIfUserIsAuthenticated() {
        FirebaseUser firebaseUser = splashViewModel.getUserAuthenticated();
        if (firebaseUser == null) { //i.e. user is not authenticated
            intent = new Intent(SplashScreenActivity.this, SignInSignUpActivity.class);

        } else {
            splashViewModel.getAuthenticatedUser(firebaseUser);
            splashViewModel.getAuthenticatedUserLiveData().observe(this, user -> {
                switch (user.getRole()) {
                    case STUDENT:
                        intent = new Intent(SplashScreenActivity.this, StudentMainActivity.class);
                        intent.putExtra("AuthenticatedUser", user);
                        break;

                    case TUTOR:
                        intent = new Intent(SplashScreenActivity.this, TutorMainActivity.class);
                        intent.putExtra("AuthenticatedUser", user);
                        break;

                    case ADMIN:
                        intent = new Intent(SplashScreenActivity.this, AdminHomeActivity.class);
                        intent.putExtra("AuthenticatedUser", user);
                        break;

                    default:
                        break;
                }
            });
        }
    }
}