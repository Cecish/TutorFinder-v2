package com.app_perso.tutorfinder_v2.view.signInSignUp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.databinding.FragmentSignUpBinding;
import com.app_perso.tutorfinder_v2.model.User;
import com.app_perso.tutorfinder_v2.viewModel.SignInSignUpViewModel;

import java.util.Objects;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SignInSignUpViewModel signInSignUpViewModel = ViewModelProviders.of(this).get(SignInSignUpViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        View view = binding.getRoot();

        binding.setSignInSignUpViewModel(signInSignUpViewModel);

        //Init radio buttons checks
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioStudent);
        radioButton.setChecked(true);

        signInSignUpViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User signUpUser) {

                //Reset error messages
                resetErrorMessages();

                //Username is a require field
                if (TextUtils.isEmpty(Objects.requireNonNull(signUpUser).getUsername())) {
                    binding.filledTextFieldUsername.setError(getString(R.string.username_required_field));
                    binding.filledTextFieldUsername.requestFocus();

                //Email address is a required field
                } else if (TextUtils.isEmpty(Objects.requireNonNull(signUpUser).getEmail())) {
                    binding.filledTextFieldEmail.setError(getString(R.string.email_required_field));
                    binding.filledTextFieldEmail.requestFocus();

                //Check validity of the email address input
                } else if (signUpUser.isNotValidEmail()) {
                    binding.filledTextFieldEmail.setError(getString(R.string.email_required_field));
                    binding.filledTextFieldEmail.requestFocus();

                //Password is a required field
                } else if (TextUtils.isEmpty(Objects.requireNonNull(signUpUser).getPassword())) {
                    binding.filledTextFieldPasswd.setError(getString(R.string.password_required_field));
                    binding.filledTextFieldPasswd.requestFocus();

                //Check validity of the password
                } else if (signUpUser.isNotValidPassword()) {
                    binding.filledTextFieldPasswd.setError(getString(R.string.password_validity));
                    binding.filledTextFieldPasswd.requestFocus();

                } else {
                    //Register new user
                    Log.d("CECILE", String.valueOf(signUpUser.getRole()));
                }

            }
        });

        return view;
    }

    private void resetErrorMessages() {
        binding.filledTextFieldUsername.setError(null);
        binding.filledTextFieldEmail.setError(null);
        binding.filledTextFieldPasswd.setError(null);
    }
}