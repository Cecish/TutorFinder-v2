package com.app_perso.tutorfinder_v2.ui.user.student.ui.searchTutors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchTutorsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchTutorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}