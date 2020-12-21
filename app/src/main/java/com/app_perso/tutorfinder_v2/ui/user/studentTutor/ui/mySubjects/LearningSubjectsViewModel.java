package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.mySubjects;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LearningSubjectsViewModel extends ViewModel {
    private MutableLiveData<Integer> viewFlipperPos = new MutableLiveData<>();

    public LearningSubjectsViewModel() { }

    public void updateViewFlipperPos(boolean noSubjects) {
        if (noSubjects) {
            viewFlipperPos.setValue(0);

        } else {
            viewFlipperPos.setValue(1);
        }
    }

    public MutableLiveData<Integer> getViewFlipperPos() {
        return viewFlipperPos;
    }
}