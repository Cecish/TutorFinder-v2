package com.app_perso.tutorfinder_v2.ui.user.studentTutor.sessions;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SessionsManagementViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;
    public MutableLiveData<List<Subject>> subjects = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();
    public MutableLiveData<Boolean> outcomeSession = new MutableLiveData<>(false);
    private MutableLiveData<Session> addedSessionLiveData = new MutableLiveData<>();

    public SessionsManagementViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public void addSession(String subjectName, String sessionDate) {
        databaseHelper.addSession(subjectName, sessionDate, addSessionSuccess, addSessionFailure);
    }

    private OnSuccessListener addSessionSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            if (o instanceof Session) {
                addedSessionLiveData.setValue((Session) o);
            } else if (o instanceof String) {
                setOutcome((String) o);
            }
        }
    };

    private OnFailureListener addSessionFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
        }
    };

    public LiveData<String> getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }

    public MutableLiveData<Boolean> getOutcomeSession() {
        return outcomeSession;
    }

    public void setOutcomeSession(Boolean outcomeSession) {
        this.outcomeSession.setValue(outcomeSession);
    }

    public void updateUsersSession(List<User> users) {
        for (User user: users) {
            databaseHelper.upsertUser(user, userSessionSuccess, userSessionFailure);
        }
    }

    private OnSuccessListener userSessionSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            Log.d("TUTOR FINDER DEBUG", "Session successfully recorded for student/tutor");
            outcomeSession.setValue(true);
        }
    };

    private OnFailureListener userSessionFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            outcomeSession.setValue(false);
            e.printStackTrace();
        }
    };

    public MutableLiveData<Session> getAddedSession() {
        return addedSessionLiveData;
    }

    public void setAddedSession(Session addedSession) {
        this.addedSessionLiveData.setValue(addedSession);
    }
}