package com.app_perso.tutorfinder_v2.viewModel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.AuthRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class ResetPasswordViewModel extends ViewModel {
    public String emailAddressReset;
    private AuthRepository authRepository;
    public MutableLiveData<String> emailAddressResetMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> passwordResetOutcome = new MutableLiveData<>();

    public ResetPasswordViewModel() {
        authRepository = new AuthRepository();
    }

    public void onClickResetPasswd(View view) {
        emailAddressResetMutableLiveData.setValue(emailAddressReset);
    }

    public MutableLiveData<String> getEmailAddressResetMutableLiveData() {
        return emailAddressResetMutableLiveData;
    }

    public MutableLiveData<String> getPasswordResetOutcome() {
        return passwordResetOutcome;
    }

    public void setPasswordResetOutcome(String passwordResetOutcome) {
        this.passwordResetOutcome.setValue(passwordResetOutcome);
    }

    public void sendResetPasswordEmail(String emailAddressReset) {
        authRepository.resetPasswordFirebase(emailAddressReset, resetPasswordSuccess, resetPasswordFailure);
    }

    private OnSuccessListener resetPasswordSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            passwordResetOutcome.setValue("A password reset email has been successfully sent to: " + ((String) o));
        }
    };

    private OnFailureListener resetPasswordFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            String failure = "A password reset email could not be sent. Please try again.";
            // If no user in the db matches the email address
            if (e instanceof FirebaseAuthInvalidUserException) {
                failure = "There is no account associated with this email address";
            }

            // If sign up fails, display a message to the user.
            passwordResetOutcome.setValue(failure);
        }
    };
}
