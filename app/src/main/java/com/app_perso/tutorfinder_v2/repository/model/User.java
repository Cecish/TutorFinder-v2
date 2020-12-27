package com.app_perso.tutorfinder_v2.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.app_perso.tutorfinder_v2.util.Role;
import com.app_perso.tutorfinder_v2.util.Status;
import com.app_perso.tutorfinder_v2.util.StringUtils;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable, Comparable<User> {
    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Status status; // for tutors
    private List<String> subjectIds;

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.subjectIds = new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //Only used at signIn and signUp time
    public User(FirebaseUser user){
        id = user.getUid();
        email = user.getEmail();
        username = user.getDisplayName();
    }

    //Only used to retrieve a user from a Firestore document
    public User(String id, String username, String email, Role role, Status status, List<String> subjectIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.subjectIds = subjectIds;
    }

    public  String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return this.role;
    }

    public Status getStatus() {
        return this.status;
    }

    public List<String> getSubjectIds() {
        return this.subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isNotValidEmail() {
        return StringUtils.isNotValidEmail(this.email);
    }

    public boolean isNotValidPassword() {
        return this.password == null || this.password.length() < 6;
    }

    public Map<String, Object> genUserForDb() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", id);
        userMap.put("username", username);
        userMap.put("email", email);
        userMap.put("role", role);
        userMap.put("status", status);
        userMap.put("subjects", subjectIds);

        return userMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", status=" + status + '\'' +
                ", subjects=" + subjectIds + '\'' +
                '}';
    }


    // Parcelling part
    public User(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.username = data[1];
        this.email = data[2];
        this.password = data[3];
        this.role = Role.valueOf(data[4]);
        this.status = Status.valueOf(data[5]);
        this.subjectIds = StringUtils.stringToList(data[6]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String subjects = "";

        if (subjectIds != null) {
            subjects = StringUtils.listToString(subjectIds);
        }

        dest.writeStringArray(new String[] {
                this.id,
                this.username,
                this.email,
                this.password,
                this.role.name(),
                this.status.name(),
                subjects
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
    public int compareTo(User user) {
        return this.username.compareTo(user.username);
    }
}
