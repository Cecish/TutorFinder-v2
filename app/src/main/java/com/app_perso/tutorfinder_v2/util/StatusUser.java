package com.app_perso.tutorfinder_v2.util;

import androidx.annotation.NonNull;

public enum StatusUser {
    PENDING("PENDING"), ACCEPTED("ACCEPTED"), DECLINED("DECLINED"), NOT_APPLICABLE("NA"), NOT_VERIFIED("NOT VERIFIED");
    private String text;
    StatusUser(String text){
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
