package com.app_perso.tutorfinder_v2.ui.user.student.ui.learningSubjects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LearningSubjectsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LearningSubjectsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}