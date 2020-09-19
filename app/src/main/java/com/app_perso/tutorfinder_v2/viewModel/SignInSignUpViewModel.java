package com.app_perso.tutorfinder_v2.viewModel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.model.Role;
import com.app_perso.tutorfinder_v2.model.User;
import com.app_perso.tutorfinder_v2.model.repository.AuthRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class SignInSignUpViewModel extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> signUpOutcome = new MutableLiveData<>();
    public MutableLiveData<String> signInOutcome = new MutableLiveData<>();
    public final ObservableBoolean optionStudent = new ObservableBoolean(true);
    public final ObservableBoolean optionTutor = new ObservableBoolean();

    private MutableLiveData<User> userSignUpMutableLiveData;
    private MutableLiveData<User> userSignInMutableLiveData;
    private AuthRepository authRepository;

    public SignInSignUpViewModel() {
        authRepository = new AuthRepository();
    }

    public MutableLiveData<User> getSignUpUser() {

        if (userSignUpMutableLiveData == null) {
            userSignUpMutableLiveData = new MutableLiveData<>();
        }
        return userSignUpMutableLiveData;
    }

    public MutableLiveData<User> getSignInUser() {

        if (userSignInMutableLiveData == null) {
            userSignInMutableLiveData = new MutableLiveData<>();
        }
        return userSignInMutableLiveData;
    }

    public MutableLiveData<String> getSignUpOutcome() {

        if (signUpOutcome == null) {
            signUpOutcome = new MutableLiveData<>();
        }

        return signUpOutcome;
    }

    public MutableLiveData<String> getSignInOutcome() {

        if (signInOutcome == null) {
            signInOutcome = new MutableLiveData<>();
        }

        return signInOutcome;
    }

    public void onClickSignIn(View view) {
        User signInUser = new User(emailAddress.getValue(), password.getValue());
        userSignInMutableLiveData.setValue(signInUser);
    }

    public void onClickSignUp(View view) {
        User signUpUser = new User(username.getValue(), emailAddress.getValue(), password.getValue(), genSelectedRole());
        userSignUpMutableLiveData.setValue(signUpUser);
    }

    public void signUpUser(User user) {
        authRepository.signUpUserFirebase(user, signUpSuccess, signUpFailure);
    }

    public void signInUser(User user) {
        authRepository.signinUserFirebase(user, signInSuccess, signInFailure);
    }

    private Role genSelectedRole() {
        Role res = null;

        if (optionStudent.get()) {
            res = Role.STUDENT;

        } else if (optionTutor.get()) {
            res = Role.TUTOR;
        }

        return res;
    }

    private OnSuccessListener signUpSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            signUpOutcome.setValue("Account has been successfully recorded!" +
                    "\nPlease check verification email before sign in.");
        }
    };

    private OnSuccessListener signInSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            signInOutcome.setValue("Sign in successfull.");
        }
    };

    private OnFailureListener signUpFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            String failure = "Sign up failed. Please try again.";
            // If sign up fails, display a message to the user.
            if (e instanceof FirebaseAuthUserCollisionException) {
                failure = "An account using this email address already exists";
            }

            signUpOutcome.setValue(failure);
        }
    };

    private OnFailureListener signInFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            String failure = "Authentication failed.";
            // If sign up fails, display a message to the user.
            signInOutcome.setValue(failure);
        }
    };
}
