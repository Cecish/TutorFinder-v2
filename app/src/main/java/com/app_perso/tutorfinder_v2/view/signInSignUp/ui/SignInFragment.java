package com.app_perso.tutorfinder_v2.view.signInSignUp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app_perso.tutorfinder_v2.databinding.FragmentSignInBinding;
import com.app_perso.tutorfinder_v2.model.User;
import com.app_perso.tutorfinder_v2.viewModel.SignInSignUpViewModel;

import java.util.Objects;


public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;

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

        SignInSignUpViewModel signInSignUpViewModel = new ViewModelProvider(requireActivity()).get(SignInSignUpViewModel.class);
        binding.setSignInSignUpViewModel(signInSignUpViewModel);

        signInSignUpViewModel.getSignInUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User signInUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(signInUser).getEmail())) {
                    binding.filledTextFieldEmail.setError("Enter an E-Mail Address");
                    binding.filledTextFieldEmail.requestFocus();
                }
                else if (signInUser.isNotValidEmail()) {
                    binding.filledTextFieldEmail.setError("Enter a Valid E-mail Address");
                    binding.filledTextFieldEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(signInUser).getPassword())) {
                    binding.filledTextFieldPasswd.setError("Enter a Password");
                    binding.filledTextFieldPasswd.requestFocus();
                }
                else if (signInUser.isNotValidPassword()) {
                    binding.filledTextFieldPasswd.setError("Enter at least 6 Digit password");
                    binding.filledTextFieldPasswd.requestFocus();
                }
                else {
                    Log.d("CECILE", signInUser.getEmail());
                }

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }
}