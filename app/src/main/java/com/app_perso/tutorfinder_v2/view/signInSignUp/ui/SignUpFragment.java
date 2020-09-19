package com.app_perso.tutorfinder_v2.view.signInSignUp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.databinding.FragmentSignUpBinding;
import com.app_perso.tutorfinder_v2.model.User;
import com.app_perso.tutorfinder_v2.viewModel.SignInSignUpViewModel;

import java.util.Objects;

public class SignUpFragment extends Fragment {
    private SignInSignUpViewModel signInSignUpViewModel;
    private FragmentSignUpBinding binding;
    private Button signUpButton;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        signInSignUpViewModel = new ViewModelProvider(requireActivity()).get(SignInSignUpViewModel.class);
        binding.setSignInSignUpViewModel(signInSignUpViewModel);

        signInSignUpViewModel.getSignUpUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User signUpUser) {

                //Reset error messages
                resetErrorMessages();

                //Disable sign up button
                signUpButton.setEnabled(false);

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
                    signInSignUpViewModel.signUpUser(signUpUser);
                    signInSignUpViewModel.getSignUpOutcome().observe(getViewLifecycleOwner(), stringOutcome -> {
                        //Re-enable sign up button
                        signUpButton.setEnabled(true);

                        if (!stringOutcome.isEmpty()) {
                            Toast.makeText(getContext(), stringOutcome, Toast.LENGTH_LONG).show();

                            //Reset sign up outcome
                            signInSignUpViewModel.setSignUpOutcome("");
                        }
                    });
                }

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        signUpButton = (Button) view.findViewById(R.id.signUp);

        //Init radio buttons checks
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioStudent);
        radioButton.setChecked(true);

        return view;
    }

    private void resetErrorMessages() {
        binding.filledTextFieldUsername.setError(null);
        binding.filledTextFieldEmail.setError(null);
        binding.filledTextFieldPasswd.setError(null);
    }
}