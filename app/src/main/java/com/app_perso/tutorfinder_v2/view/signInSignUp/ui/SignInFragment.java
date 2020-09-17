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

import com.app_perso.tutorfinder_v2.R;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SignInSignUpViewModel signInSignUpViewModel = ViewModelProviders.of(this).get(SignInSignUpViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();

        binding.setSignInSignUpViewModel(signInSignUpViewModel);

        signInSignUpViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
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

        return view;
    }
}