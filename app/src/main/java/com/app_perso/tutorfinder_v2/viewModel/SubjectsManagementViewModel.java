package com.app_perso.tutorfinder_v2.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubjectsManagementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SubjectsManagementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}