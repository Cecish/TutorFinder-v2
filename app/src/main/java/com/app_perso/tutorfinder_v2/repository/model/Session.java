package com.app_perso.tutorfinder_v2.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.util.StatusSession;

import java.util.HashMap;
import java.util.Map;

public class Session implements Parcelable {
    private String id;
    private String date;
    private String subjectName;
    private StatusSession status; // for tutors

    public Session(String date, String subjectName) {
        this.date = date;
        this.subjectName = subjectName;
        this.status = StatusSession.PENDING;
    }

    public Session(String id, String date, String subjectName, StatusSession status) {
        this.id = id;
        this.date = date;
        this.subjectName = subjectName;
        this.status = status;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(StatusSession status) {
        this.status = status;
    }

    public Map<String, Object> genSessionForDb() {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("id", id);
        sessionMap.put("date", date);
        sessionMap.put("subjectName", subjectName);
        sessionMap.put("status", status);

        return sessionMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    // Parcelling part
    public Session(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.date = data[1];
        this.subjectName = data[2];
        this.status = StatusSession.valueOf(data[3]);
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
                this.status.name()
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
}