package com.app_perso.tutorfinder_v2.viewModel;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.model.Role;
import com.app_perso.tutorfinder_v2.model.User;


public class SignInSignUpViewModel extends ViewModel {
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public final ObservableBoolean optionStudent = new ObservableBoolean(true);
    public final ObservableBoolean optionTutor = new ObservableBoolean();

    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClickSignIn(View view) {
        User signInUser = new User(emailAddress.getValue(), password.getValue());
        userMutableLiveData.setValue(signInUser);
    }

    public void onClickSignUp(View view) {
        User signUpUser = new User(username.getValue(), emailAddress.getValue(), password.getValue(), genSelectedRole());
        userMutableLiveData.setValue(signUpUser);
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
}
