package com.app_perso.tutorfinder_v2.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TutorsManagementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TutorsManagementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}