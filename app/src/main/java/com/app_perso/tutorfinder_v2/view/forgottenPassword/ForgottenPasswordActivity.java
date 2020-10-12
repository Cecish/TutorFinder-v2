package com.app_perso.tutorfinder_v2.view.forgottenPassword;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.databinding.ActivityForgottenPasswordBinding;
import com.app_perso.tutorfinder_v2.util.SignInSignUpUtils;
import com.app_perso.tutorfinder_v2.util.StringUtils;
import com.app_perso.tutorfinder_v2.viewModel.ResetPasswordViewModel;


public class ForgottenPasswordActivity extends AppCompatActivity {
    private ActivityForgottenPasswordBinding binding;
    private Button resetButton;
    private LifecycleOwner mLifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgotten_password);
        ResetPasswordViewModel resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        binding.setResetPasswordViewModel(resetPasswordViewModel);
        resetButton = (Button) findViewById(R.id.resetPassword);
        mLifecycleOwner = this;

        Observer<String> passwordResetObserver = stringOutcome -> {
            if (!stringOutcome.isEmpty()) {
                Toast.makeText(this, stringOutcome, Toast.LENGTH_LONG).show();

                //Reset sign in outcome
                resetPasswordViewModel.setPasswordResetOutcome("");

                //Re-enable sign up button
                resetButton.setEnabled(true);
            }
        };

        setUpToolbar();

        resetPasswordViewModel.getEmailAddressResetMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String emailAddress) {

                //Reset error messages
                SignInSignUpUtils.resetErrorMessages(binding.filledTextFieldEmail);

                //Disable reset button
                resetButton.setEnabled(false);

                if (TextUtils.isEmpty(emailAddress)) {
                    binding.filledTextFieldEmail.setError("Enter an E-Mail Address");
                    binding.filledTextFieldEmail.requestFocus();

                    //Re-enable sign up button
                    resetButton.setEnabled(true);

                } else if (StringUtils.isNotValidEmail(emailAddress)) {
                    binding.filledTextFieldEmail.setError("Enter a Valid E-mail Address");
                    binding.filledTextFieldEmail.requestFocus();

                    //Re-enable sign up button
                    resetButton.setEnabled(true);

                } else {
                    //Reset password
                    resetPasswordViewModel.sendResetPasswordEmail(emailAddress);
                    resetPasswordViewModel.getPasswordResetOutcome().observe(mLifecycleOwner, passwordResetObserver);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        ActionBar mActionBar;
        Toolbar mToolbar = findViewById(R.id.toolbar_actionbar);

        ((TextView) mToolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.password_reset));
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}