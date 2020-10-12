package com.app_perso.tutorfinder_v2.view.signInSignUp.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.view.AdminHomeActivity;
import com.app_perso.tutorfinder_v2.view.StudentHomeActivity;
import com.app_perso.tutorfinder_v2.view.TutorHomeActivity;
import com.app_perso.tutorfinder_v2.util.SignInSignUpUtils;
import com.app_perso.tutorfinder_v2.view.forgottenPassword.ForgottenPasswordActivity;
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
        binding.setFragment(this);

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
                            doOutcome(stringOutcome, signInSignUpViewModel.getSignedInUser(), getActivity());
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

    private void doOutcome(String outcome, User signInUser, Activity activity) {
        //Sign in successful
        if (outcome.equals(SignInSignUpUtils.SIGNED_IN)) {
            Intent intent = null;

            if (signInUser.getRole().equals(Role.STUDENT)) {
                intent = new Intent(activity, StudentHomeActivity.class);

            } else if (signInUser.getRole().equals(Role.TUTOR)) {
                intent = new Intent(activity, TutorHomeActivity.class);

            } else if (signInUser.getRole().equals(Role.ADMIN)) {
                intent = new Intent(activity, AdminHomeActivity.class);
            }

            if (intent != null) {
                intent.putExtra("AuthenticatedUser", signInUser);
                startActivity(intent);
                activity.finish();
            }

            //Authentication failed
        } else {
            Toast.makeText(getContext(), outcome, Toast.LENGTH_LONG).show();

            //Reset sign in outcome
            signInSignUpViewModel.setSignInOutcome("");

            //Re-enable sign up button
            signInButton.setEnabled(true);
        }
    }

    public void goToResetPasswdActivity(View view) {
        startActivity(new Intent(requireContext(), ForgottenPasswordActivity.class));
    }
}