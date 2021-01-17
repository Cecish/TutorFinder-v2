package com.app_perso.tutorfinder_v2.ui.user.admin;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.util.StringUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SubjectsManagementViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;
    public MutableLiveData<List<Subject>> subjects = new MutableLiveData<>();
    private MutableLiveData<List<Subject>> subjectsSelection = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();

    public SubjectsManagementViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public void getAllSubjects() {
        databaseHelper.getAllSubjects(subjectsSuccess, subjectsFailure);
    }

    public void addSubject(String subjectName) {
        databaseHelper.addSubject(StringUtils.toUpperCaseFirstLetter(subjectName), addSubjectSuccess, addSubjectFailure);
    }

    public void updateSubject(Subject subject) {
        databaseHelper.updateSubject(subject, updateSubjectSuccess, updateSubjectFailure);
    }

    public void deleteSubject(String subjectId) {
        databaseHelper.deleteSubject(subjectId);
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

    private OnSuccessListener addSubjectSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            if (o instanceof Subject) {
                addSubjectLiveData((Subject) o);
            } else if (o instanceof String) {
                setOutcome((String) o);
            }
        }
    };

    private OnFailureListener addSubjectFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
        }
    };

    private OnSuccessListener updateSubjectSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            if (o instanceof Subject) {
                getAllSubjects();
            } else if (o instanceof String) {
                setOutcome((String) o);
            }
        }
    };

    private OnFailureListener updateSubjectFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
        }
    };

    public MutableLiveData<List<Subject>> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects.setValue(subjects);
    }

    private void addSubjectLiveData(Subject subject) {
        List<Subject> tempSubjects = subjects.getValue();
        tempSubjects.add(subject);
        subjects.setValue(tempSubjects);
    }

    public LiveData<String> getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }

    public void getSubjects(List<String> subjectIds) {
        databaseHelper.getSubjectsById(subjectIds, getSubjectsSuccess, getSubjectsFailure);
    }

    private OnSuccessListener getSubjectsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            subjectsSelection.setValue((List<Subject>) o);
        }
    };

    private OnFailureListener getSubjectsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
        }
    };

    public MutableLiveData<List<Subject>> getSubjectsSelection() {
        return subjectsSelection;
    }

    public void setSubjectsSelection(List<Subject> subjectsSelection) {
        this.subjectsSelection.setValue(subjectsSelection);
    }
}