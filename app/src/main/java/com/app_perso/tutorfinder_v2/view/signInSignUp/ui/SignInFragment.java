package com.app_perso.tutorfinder_v2.view.signInSignUp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app_perso.tutorfinder_v2.R;
import com.app_perso.tutorfinder_v2.databinding.FragmentSignInBinding;
import com.app_perso.tutorfinder_v2.model.User;
import com.app_perso.tutorfinder_v2.view.signInSignUp.SignInSignUpUtils;
import com.app_perso.tutorfinder_v2.viewModel.SignInSignUpViewModel;

import java.util.Objects;


public class SignInFragment extends Fragment  {
    private SignInSignUpViewModel signInSignUpViewModel;
    private FragmentSignInBinding binding;
    private Button signInButton;

    public SignInFragment() {
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

        signInSignUpViewModel.getSignInUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User signInUser) {

                //Reset error messages
                SignInSignUpUtils.resetErrorMessages(binding.filledTextFieldEmail,
                        binding.filledTextFieldPasswd);

                //Disable sign up button
                signInButton.setEnabled(false);

                if (TextUtils.isEmpty(Objects.requireNonNull(signInUser).getEmail())) {
                    binding.filledTextFieldEmail.setError("Enter an E-Mail Address");
                    binding.filledTextFieldEmail.requestFocus();

                    //Re-enable sign up button
                    signInButton.setEnabled(true);

                } else if (signInUser.isNotValidEmail()) {
                    binding.filledTextFieldEmail.setError("Enter a Valid E-mail Address");
                    binding.filledTextFieldEmail.requestFocus();

                    //Re-enable sign up button
                    signInButton.setEnabled(true);

                } else if (TextUtils.isEmpty(Objects.requireNonNull(signInUser).getPassword())) {
                    binding.filledTextFieldPasswd.setError("Enter a Password");
                    binding.filledTextFieldPasswd.requestFocus();

                    //Re-enable sign up button
                    signInButton.setEnabled(true);

                } else if (signInUser.isNotValidPassword()) {
                    binding.filledTextFieldPasswd.setError("Enter at least 6 Digit password");
                    binding.filledTextFieldPasswd.requestFocus();

                    //Re-enable sign up button
                    signInButton.setEnabled(true);

                } else {
                    //Sign in user
                    signInSignUpViewModel.signInUser(signInUser);
                    signInSignUpViewModel.getSignInOutcome().observe(getViewLifecycleOwner(), stringOutcome -> {
                        if (!stringOutcome.isEmpty()) {
                            Toast.makeText(getContext(), stringOutcome, Toast.LENGTH_LONG).show();

                            //Reset sign in outcome
                            signInSignUpViewModel.setSignInOutcome("");

                            //Re-enable sign up button
                            signInButton.setEnabled(true);
                        }
                    });
                }

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        signInButton = (Button) view.findViewById(R.id.signIn);

        //set variables in Binding
        return binding.getRoot();
    }
}