package com.app_perso.tutorfinder_v2.viewModel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;


public class ResetPasswordViewModel {
    public MutableLiveData<String> emailAddressReset = new MutableLiveData<>();

    public void onClickResetPasswd(View view) {
        //TODO emailAddressReset.getValue()
    }
}
