package com.app_perso.tutorfinder_v2.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.util.StatusSession;

import java.util.HashMap;
import java.util.Map;

public class Session implements Parcelable, Comparable<Session> {
    private String id;
    private String date;
    private String subjectName;
    private String subjectId;
    private StatusSession status;
    private String studentId;
    private String tutorId;

    public Session(String date, String subjectName, String subjectId, String studentId, String tutorId) {
        this.date = date;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.status = StatusSession.PENDING;
        this.studentId = studentId;
        this.tutorId = tutorId;
    }

    public Session(String id, String date, String subjectName, String subjectId, StatusSession status, String studentId, String tutorId) {
        this.id = id;
        this.date = date;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.status = status;
        this.studentId = studentId;
        this.tutorId = tutorId;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public StatusSession getStatus() {
        return this.status;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(StatusSession status) {
        this.status = status;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Map<String, Object> genSessionForDb() {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("id", id);
        sessionMap.put("date", date);
        sessionMap.put("subjectName", subjectName);
        sessionMap.put("subjectId", subjectId);
        sessionMap.put("status", status);
        sessionMap.put("studentId", studentId);
        sessionMap.put("tutorId", tutorId);

        return sessionMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", status='" + status + '\'' +
                ", [studentId='" + studentId + ", tutorId=" + tutorId + "]" + '\'' +
                '}';
    }

    // Parcelling part
    public Session(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.date = data[1];
        this.subjectName = data[2];
        this.subjectId = data[3];
        this.status = StatusSession.valueOf(data[4]);
        this.studentId = data[5];
        this.tutorId = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.id,
                this.date,
                this.subjectName,
                this.subjectId,
                this.status.name(),
                this.studentId,
                this.tutorId
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int compareTo(Session otherSession) {

        if (Long.parseLong(this.date) < Long.parseLong(otherSession.date)) {
            return -1;

        } else if (Long.parseLong(this.date) > Long.parseLong(otherSession.date)) {
            return 1;

        } else {
            return this.subjectName.compareTo(otherSession.subjectName);
        }
    }
}