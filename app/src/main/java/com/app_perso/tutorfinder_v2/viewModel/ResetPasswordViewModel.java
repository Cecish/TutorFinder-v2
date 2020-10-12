package com.app_perso.tutorfinder_v2.viewModel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ResetPasswordViewModel extends ViewModel {
    public String emailAddressReset;
    public MutableLiveData<String> emailAddressResetMutableLiveData = new MutableLiveData<>();

    public void onClickResetPasswd(View view) {
        emailAddressResetMutableLiveData.setValue(emailAddressReset);
    }

    public MutableLiveData<String> getEmailAddressResetMutableLiveData() {
        return emailAddressResetMutableLiveData;
    }
}
