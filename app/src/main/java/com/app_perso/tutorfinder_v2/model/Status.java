package com.app_perso.tutorfinder_v2.model;

import androidx.annotation.NonNull;

public enum Status {
    PENDING("PENDING"), ACCEPTED("ACCEPTED"), DECLINED("DECLINED");
    private String text;
    Status(String text){
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
