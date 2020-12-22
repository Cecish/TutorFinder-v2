package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.mySubjects;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LearningSubjectsViewModel extends ViewModel {
    private MutableLiveData<Integer> viewFlipperPos = new MutableLiveData<>();
    private DatabaseHelper databaseHelper;
    private boolean isDisableState = true;

    public LearningSubjectsViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public boolean getIsDisableState() {
        return isDisableState;
    }

    public void setIsDisableState(boolean isDisanleState) {
        this.isDisableState = isDisanleState;
    }

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

    public void updateUserinRemoteDb(User user) {
        databaseHelper.upsertUser(user, updateUserSuccess, updateUserFailure);
    }

    private OnSuccessListener updateUserSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            Log.d("TUTOR FINDER DEBUG", "User successfully updated in the cloud-hosted database");
        }
    };

    private OnFailureListener updateUserFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            e.printStackTrace();
        }
    };
}