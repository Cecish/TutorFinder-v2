package com.app_perso.tutorfinder_v2.ui.user.studentTutor.ui.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app_perso.tutorfinder_v2.repository.DatabaseHelper;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.Role;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ChatViewModel extends ViewModel {
    private DatabaseHelper databaseHelper;
    private MutableLiveData<String> outcome = new MutableLiveData<>();
    private MutableLiveData<Set<String>> chattingBuddiesId = new MutableLiveData<>(new HashSet<>());
    private MutableLiveData<Set<String>> approvedStudentsTutorsIds = new MutableLiveData<>(new HashSet<>());
    private MutableLiveData<List<User>> usersFromIds = new MutableLiveData<>(new ArrayList<>());

    public ChatViewModel() {
        this.databaseHelper = new DatabaseHelper();
    }

    public void getChattingBuddiesIds(String senderId) {
        databaseHelper.getMessagesInvolvingId(senderId, getChattingBuddiesIdsSuccess, getChattingBuddiesIdsFailure);
    }

    private OnSuccessListener getChattingBuddiesIdsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            Log.d("TUTOR FINDER DEBUG", "Chatting buddies id successfully retrieved");
            chattingBuddiesId.setValue((Set<String>) o);
        }
    };

    private OnFailureListener getChattingBuddiesIdsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            e.printStackTrace();
        }
    };

    public void getAllMyStudentsOrTutorsIds(User user) {
        switch (user.getRole()) {
            case STUDENT:
                databaseHelper.getAllMyTutorsOrStudentsIds(user.getId(), "studentId", Role.STUDENT,
                        getAllMyStudentsOrTutorsIdsSuccess, getAllMyStudentsOrTutorsIdsFailure);
                break;

            case TUTOR:
                databaseHelper.getAllMyTutorsOrStudentsIds(user.getId(), "tutorId",Role.TUTOR,
                        getAllMyStudentsOrTutorsIdsSuccess, getAllMyStudentsOrTutorsIdsFailure);
                break;

            default:
                throw new IllegalArgumentException("Role " + user.getRole() + " is not supported by getAllMyStudentsOrTutorsIds");
        }
    }

    private OnSuccessListener getAllMyStudentsOrTutorsIdsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            approvedStudentsTutorsIds.setValue((Set<String>) o);
        }
    };

    private OnFailureListener getAllMyStudentsOrTutorsIdsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            e.printStackTrace();
        }
    };

    public void getUsersFromIds(List<String> userIds) {
        databaseHelper.getUsersFromIds(userIds, getUsersFromIdsSuccess, getUsersFromIdsFailure);
    }

    private OnSuccessListener getUsersFromIdsSuccess = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            usersFromIds.setValue((List<User>) o);
        }
    };

    private OnFailureListener getUsersFromIdsFailure = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            setOutcome("Oops. An error occurred.");
            e.printStackTrace();
        }
    };

    public MutableLiveData<String> getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome.setValue(outcome);
    }

    public MutableLiveData<Set<String>> getChatBuddiesIds() {
        return this.chattingBuddiesId;
    }

    public MutableLiveData<Set<String>> getApprovedStudentsTutorsIds() {
        return this.approvedStudentsTutorsIds;
    }


    public MutableLiveData<List<User>> getUsersToChatWith() {
        return this.usersFromIds;
    }
}