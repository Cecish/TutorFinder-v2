package com.app_perso.tutorfinder_v2.ui.user.admin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.AuthRepository;
import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class TutorsManagementViewModel extends ViewModel {
    private AuthRepository authRepository;
    private DatabaseHelper databaseHelper;
    public MutableLiveData<List<User>> pendingTutors = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();
    public MutableLiveData<Boolean> refresh = new MutableLiveData<>(false);

    public TutorsManagementViewModel() {
        authRepository = new AuthRepository();
        databaseHelper = new DatabaseHelper();
    }

    public void getAllPendingRequests() {
        authRepository.getAllPendingTutors(pendingTutorsSuccess, pendingTutorsFailure);
    }

    public void approveTutor(User user) {
        user.setStatus(Status.ACCEPTED);
        databaseHelper.upsertUser(user, tutorSuccess, tutorFailure);
        getAllPendingRequests();
    }

    public void declineTutor(User user) {
        user.setStatus(Status.DECLINED);
        databaseHelper.upsertUser(user, tutorSuccess, tutorFailure);
        getAllPendingRequests();
    }

    private OnSuccessListener pendingTutorsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            refresh.setValue(false);
            pendingTutors.setValue((List<User>) o);
        }
    };

    private OnFailureListener pendingTutorsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            refresh.setValue(false);
            setOutcome("Oops. An error occurred.");
        }
    };

    private OnSuccessListener tutorSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            Log.d("TUTOR FINDER DEBUG", "Tutor registration successfully handled");
        }
    };

    private OnFailureListener tutorFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            e.printStackTrace();
        }
    };

    public LiveData<String> getOutcome() {
        return outcome;
    }

    public MutableLiveData<List<User>> getPendingTutors() {
        return pendingTutors;
    }

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }

    public MutableLiveData<Boolean> getRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh.setValue(refresh);
    }
}