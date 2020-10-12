package com.app_perso.tutorfinder_v2.util;

import androidx.annotation.NonNull;

public enum Status {
    PENDING("PENDING"), ACCEPTED("ACCEPTED"), DECLINED("DECLINED"), NOT_APPLICABLE("NA");
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
