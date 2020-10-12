package com.app_perso.tutorfinder_v2.util;

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
