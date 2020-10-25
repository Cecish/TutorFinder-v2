package com.app_perso.tutorfinder_v2.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SubjectsManagementViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;
    public MutableLiveData<List<Subject>> subjects = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();


    public SubjectsManagementViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public void getAllSubjects() {
        databaseHelper.getAllSubjects(subjectsSuccess, subjectsFailure);
    }

    private OnSuccessListener subjectsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            subjects.setValue((List<Subject>) o);
        }
    };

    private OnFailureListener subjectsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
        }
    };

    public MutableLiveData<List<Subject>> getSubjects() {
        return subjects;
    }

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }
}