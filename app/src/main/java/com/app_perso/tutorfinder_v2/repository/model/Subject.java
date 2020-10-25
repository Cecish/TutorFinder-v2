package com.app_perso.tutorfinder_v2.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Subject implements Parcelable, Comparable<Subject> {
    private String id;
    private String name;

    public Subject(String name){
        this.name = name;
    }

    public Subject(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Map<String, Object> genSubjectForDb() {
        Map<String, Object> subjectMap = new HashMap<>();
        subjectMap.put("id", id);
        subjectMap.put("name", name);

        return subjectMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Subject otherObject) {
        return this.name.compareTo(otherObject.name);
    }

    // Parcelling part
    public Subject(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.name = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.id,
                this.name
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

