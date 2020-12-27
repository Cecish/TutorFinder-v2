package com.app_perso.tutorfinder_v2.ui.user.studentTutor.student.searchTutors;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.AuthRepository;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTutorsViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<List<User>> matchingTutors = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> outcome = new MutableLiveData<>();

    public SearchTutorsViewModel() {
        authRepository = new AuthRepository();
    }

    public void searchTutors(List<String> subjectsIds) {
        authRepository.getTutorsMatchingSubjects(subjectsIds, searchSuccess, searchFailure);
    }

    private OnSuccessListener searchSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            matchingTutors.setValue((List<User>) o);
        }
    };

    private OnFailureListener searchFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred. Try again later");
        }
    };

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }

    public MutableLiveData<List<User>> getMatchingTutors() {
        return this.matchingTutors;
    }

    public MutableLiveData<String> getOutcome() {
        return this.outcome;
    }
}