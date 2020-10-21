package com.app_perso.tutorfinder_v2.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.AuthRepository;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class TutorsManagementViewModel extends ViewModel {
    private AuthRepository authRepository;
    public MutableLiveData<List<User>> pendingTutors = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();

    public TutorsManagementViewModel() {
        authRepository = new AuthRepository();
    }

    public void getAllPendingRequests() {
        authRepository.getAllPendingTutors(pendingTutorsSuccess, pendingTutorsFailure);
    }

    private OnSuccessListener pendingTutorsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            pendingTutors.setValue((List<User>) o);
        }
    };

    private OnFailureListener pendingTutorsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
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
}