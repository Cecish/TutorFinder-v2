package com.app_perso.tutorfinder_v2.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Session implements Parcelable {
    private String id;
    private String date;
    private String subjectName;

    public Session(String date, String subjectName) {
        this.date = date;
        this.subjectName = subjectName;
    }

    public Session(String id, String date, String sessionId) {
        this.id = id;
        this.date = date;
        this.subjectName = subjectName;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Map<String, Object> genSessionForDb() {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("id", id);
        sessionMap.put("date", date);
        sessionMap.put("subjectName", subjectName);

        return sessionMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }

    // Parcelling part
    public Session(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.date = data[1];
        this.subjectName = data[2];
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
                this.subjectName
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