package com.app_perso.tutorfinder_v2.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.repository.model.ChatMessage;
import com.app_perso.tutorfinder_v2.repository.model.Session;
import com.app_perso.tutorfinder_v2.repository.model.Subject;
import com.app_perso.tutorfinder_v2.repository.model.User;
import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.StatusSession;
import com.app_perso.tutorfinder_v2.util.StringUtils;
import com.app_perso.tutorfinder_v2.util.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DatabaseHelper {
    private final String COLLECTION_USERS = "tutor_finder_users";
    private final String COLLECTION_SUBJECTS = "tutor_finder_subjects";
    private final String COLLECTION_SESSIONS = "tutor_finder_sessions";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceUsers = firebaseFirestore.collection(COLLECTION_USERS);
    private CollectionReference collectionReferenceSubjects = firebaseFirestore.collection(COLLECTION_SUBJECTS);
    private CollectionReference collectionReferenceSessions = firebaseFirestore.collection(COLLECTION_SESSIONS);
    private DatabaseReference realtimeDbReference = FirebaseDatabase.getInstance().getReference();

    //if the entity is present in the database update the record, else create new record
    public void upsertUser(final User user, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        DocumentReference uidRef;

        if (user.getId() == null) {
            uidRef = collectionReferenceUsers.document();
        } else {
            uidRef = collectionReferenceUsers.document(user.getId());
        }

        uidRef.set(user.genUserForDb())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void upsertSubject(final Subject subject, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        DocumentReference uidRef;

        if (subject.getId() == null) {
            uidRef = collectionReferenceSubjects.document();
            subject.setId(uidRef.getId());
        } else {
            uidRef = collectionReferenceSubjects.document(subject.getId());
        }

        uidRef.set(subject.genSubjectForDb())
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(subject);
                    }
                })
                .addOnFailureListener(failureListener);
    }

    public void addSubject(final String subjectName, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereEqualTo("name", subjectName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() == 0) {
                                //Add new subject
                                upsertSubject(new Subject(subjectName), successListener, failureListener);

                            } else {
                                successListener.onSuccess("Subject " + subjectName + " is already added");
                            }
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void updateSubject(final Subject subject, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereEqualTo("name", subject.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() == 0) {
                                //Add new subject
                                upsertSubject(subject, successListener, failureListener);

                            } else {
                                successListener.onSuccess("Subject " + subject.getName() + " is already added");
                            }
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getAllSubjects(@NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Subject> subjects = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subjects.add(castToSubject(document.getData()));
                            }
                            successListener.onSuccess(subjects);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getSubjectsById(List<String> subjectIds, @NonNull final OnSuccessListener successListener,
                                    @NonNull final OnFailureListener failureListener) {
        collectionReferenceSubjects
                .whereIn("id", subjectIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Subject> subjects = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subjects.add(castToSubject(document.getData()));
                            }
                            successListener.onSuccess(subjects);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private Subject castToSubject(Map<String, Object> map) {
        try {
            return new Subject(
                    Objects.requireNonNull(map.get("id")).toString(),
                    Objects.requireNonNull(map.get("name")).toString()
            );

        } catch(Exception e) {
            Log.d("ERROR", "Map Firebase document data is incorrect");
            throw e;
        }
    }

    private Session castToSession(Map<String, Object> map) {
        try {
            return new Session(
                    Objects.requireNonNull(map.get("id")).toString(),
                    Objects.requireNonNull(map.get("date")).toString(),
                    Objects.requireNonNull(map.get("subjectName")).toString(),
                    StatusSession.valueOf(Objects.requireNonNull(map.get("status")).toString()),
                    Objects.requireNonNull(map.get("studentId")).toString(),
                    Objects.requireNonNull(map.get("tutorId")).toString()
                    );

        } catch(Exception e) {
            Log.d("ERROR", "Map Firebase document data is incorrect");
            throw e;
        }
    }

    public void upsertSession(final Session session, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        DocumentReference uidRef;

        if (session.getId() == null) {
            uidRef = collectionReferenceSessions.document();
            session.setId(uidRef.getId());
        } else {
            uidRef = collectionReferenceSessions.document(session.getId());
        }

        uidRef.set(session.genSessionForDb())
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        successListener.onSuccess(session);
                    }
                })
                .addOnFailureListener(failureListener);
    }

    public void addSession(final String subjectName, final String sessionDate, String studentId, String tutorId,
                           @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSessions
                .whereEqualTo("subjectName", subjectName)
                .whereEqualTo("date", sessionDate)
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("tutorId", tutorId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() == 0) {
                                //Add new subject
                                upsertSession(new Session(sessionDate, subjectName, studentId, tutorId),
                                        successListener, failureListener);

                            } else {
                                successListener.onSuccess("Session at " + sessionDate + " for subject " + subjectName + " is already added");
                            }
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getAllSessionsForUser(String userId, Role roleUser, StatusSession statusSession, @NonNull final OnSuccessListener successListener,
                                      @NonNull final OnFailureListener failureListener) {
        String fieldName = "";

        switch (roleUser) {
            case STUDENT:
            fieldName = "studentId";
            break;

            case TUTOR:
            fieldName = "tutorId";
            break;

            default:
                throw new IllegalArgumentException("Role " + roleUser.name() + " is not permitted here");
        }

        collectionReferenceSessions
                .whereEqualTo("status", statusSession.name())
                .whereEqualTo(fieldName, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Session> sessions = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                sessions.add(castToSession(document.getData()));
                            }
                            successListener.onSuccess(sessions);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getUserFromId(String userId, @NonNull final OnSuccessListener successListener,
                                       @NonNull final OnFailureListener failureListener) {
        collectionReferenceUsers
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(UserUtils.castToUser(document.getData()));
                            }
                            successListener.onSuccess(users.get(0));
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void getUsersFromIds(List<String> userIds, @NonNull final OnSuccessListener successListener,
                              @NonNull final OnFailureListener failureListener) {
        collectionReferenceUsers
                .whereIn("id", userIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<User> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(UserUtils.castToUser(document.getData()));
                            }

                            //Sort by alphabetical order
                            Collections.sort(users);

                            successListener.onSuccess(users);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void pushMessageInRealtimeDb(ChatMessage chatMessage) {
        realtimeDbReference
                .push()
                .setValue(chatMessage);
    }

    public Query getChatMessagesBetween2Ids(String senderId, String targetId) {
        return realtimeDbReference.orderByChild("fromTo")
                .equalTo(StringUtils.appendStringsAlphabetically(senderId, targetId));
    }

    public void getMessagesInvolvingId(String userId, @NonNull final OnSuccessListener successListener,
                              @NonNull final OnFailureListener failureListener) {
        realtimeDbReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Set<String> usersId = new HashSet<>();
                            for (DataSnapshot document : dataSnapshot.getChildren()) {
                                ChatMessage message = document.getValue(ChatMessage.class);
                                assert message != null;
                                if (message.getFromTo().contains(userId)) {
                                    usersId.add(StringUtils.getOtherId(message.getFromTo(), userId));
                                }
                            }
                            successListener.onSuccess(usersId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        failureListener.onFailure(Objects.requireNonNull(error.toException()));
                    }
                });
    }

    public void getAllMyTutorsOrStudentsIds(String userId, String fieldName, Role userRole, @NonNull final OnSuccessListener successListener, @NonNull final OnFailureListener failureListener) {
        collectionReferenceSessions
                .whereEqualTo("status", StatusSession.ACCEPTED.name())
                .whereEqualTo(fieldName, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Set<String> userIds = new HashSet<>();

                            if (userRole == Role.STUDENT) {
                                userIds = getAllTutorsIds(task.getResult());
                            } else if (userRole == Role.TUTOR) {
                                userIds = getAllStudentsIds(task.getResult());
                            }

                            successListener.onSuccess(userIds);
                        } else {
                            failureListener.onFailure(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private Set<String> getAllTutorsIds(QuerySnapshot queryDocumentSnapshots) {
        Set<String> res = new HashSet<>();

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            res.add(castToSession(document.getData()).getTutorId());
        }

        return res;
    }

    private Set<String> getAllStudentsIds(QuerySnapshot queryDocumentSnapshots) {
        Set<String> res = new HashSet<>();

        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            res.add(castToSession(document.getData()).getStudentId());
        }

        return res;
    }
}
