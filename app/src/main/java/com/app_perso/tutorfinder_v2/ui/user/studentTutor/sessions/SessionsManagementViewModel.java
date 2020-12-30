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
import com.app_perso.tutorfinder_v2.util.StatusSession;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class SessionsManagementViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;
    public MutableLiveData<List<Subject>> subjects = new MutableLiveData<>();
    public MutableLiveData<String> outcome = new MutableLiveData<>();
    public MutableLiveData<Boolean> outcomeSession = new MutableLiveData<>(false);
    private MutableLiveData<Session> addedSessionLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Session>> pendingSessions = new MutableLiveData<>();
    public MutableLiveData<Boolean> refresh = new MutableLiveData<>(false);

    public SessionsManagementViewModel() {
        databaseHelper = new DatabaseHelper();
    }

    public void addSession(String subjectName, String sessionDate, String studentId, String tutorId) {
        databaseHelper.addSession(subjectName, sessionDate, studentId, tutorId, addSessionSuccess, addSessionFailure);
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

    public MutableLiveData<Boolean> getRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh.setValue(refresh);
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

    public MutableLiveData<List<Session>> getPendingSessions() {
        return pendingSessions;
    }

    public MutableLiveData<Session> getAddedSession() {
        return addedSessionLiveData;
    }

    public void setAddedSession(Session addedSession) {
        this.addedSessionLiveData.setValue(addedSession);
    }

    public void approveSession(Session session) {
        session.setStatus(StatusSession.ACCEPTED);
        databaseHelper.upsertSession(session, sessionsSuccess, sessionFailure);
        getAllPendingSessionsForTutor(session.getTutorId());
    }

    public void declineSession(Session session) {
        session.setStatus(StatusSession.DECLINED);
        databaseHelper.upsertSession(session, sessionsSuccess, sessionFailure);
        getAllPendingSessionsForTutor(session.getTutorId());
    }

    private OnSuccessListener sessionsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            Log.d("TUTOR FINDER DEBUG", "Session successfully updated");
        }
    };

    private OnFailureListener sessionFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            e.printStackTrace();
        }
    };

    public void getAllPendingSessionsForTutor(String userId) {
        databaseHelper.getAllPendingSessionsForTutor(userId, pendingSessionsSuccess, pendingSessionsFailure);
    }

    private OnSuccessListener pendingSessionsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            refresh.setValue(false);
            pendingSessions.setValue((List<Session>) o);
        }
    };

    private OnFailureListener pendingSessionsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            refresh.setValue(false);
            setOutcome("Oops. An error occurred.");
        }
    };
}