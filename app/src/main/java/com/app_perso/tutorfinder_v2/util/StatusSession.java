package com.app_perso.tutorfinder_v2.util;

import androidx.annotation.NonNull;

public enum StatusSession {
    PENDING("PENDING"), ACCEPTED("ACCEPTED"), DECLINED("DECLINED");
    private String text;
    StatusSession(String text){
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
