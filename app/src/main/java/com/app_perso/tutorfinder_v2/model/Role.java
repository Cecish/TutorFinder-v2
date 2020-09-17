package com.app_perso.tutorfinder_v2.model;

import androidx.annotation.NonNull;

public enum Role {
    STUDENT("STUDENT"),TUTOR("TUTOR"),ADMIN("ADMIN");
    private String text;
    Role(String text){
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
