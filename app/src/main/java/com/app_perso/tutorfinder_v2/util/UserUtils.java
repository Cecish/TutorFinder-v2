package com.app_perso.tutorfinder_v2.util;

import android.util.Log;

import com.app_perso.tutorfinder_v2.repository.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserUtils {
    public static User castToUser(Map<String, Object> map) {
        try {
            User user;

            if (map.get("subjects") == null && map.get("sessions") == null) {
                user = new User(
                        Objects.requireNonNull(map.get("id")).toString(),
                        Objects.requireNonNull(map.get("username")).toString(),
                        Objects.requireNonNull(map.get("email")).toString(),
                        Role.valueOf(Objects.requireNonNull(map.get("role")).toString()),
                        StatusUser.valueOf(Objects.requireNonNull(map.get("status")).toString()),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            } else  if (map.get("subjects") != null && map.get("sessions") == null) {
                user = new User(
                        Objects.requireNonNull(map.get("id")).toString(),
                        Objects.requireNonNull(map.get("username")).toString(),
                        Objects.requireNonNull(map.get("email")).toString(),
                        Role.valueOf(Objects.requireNonNull(map.get("role")).toString()),
                        StatusUser.valueOf(Objects.requireNonNull(map.get("status")).toString()),
                        (List<String>) Objects.requireNonNull(map.get("subjects")),
                        new ArrayList<>()
                );
            } else  if (map.get("subjects") == null && map.get("sessions") != null) {
                user = new User(
                        Objects.requireNonNull(map.get("id")).toString(),
                        Objects.requireNonNull(map.get("username")).toString(),
                        Objects.requireNonNull(map.get("email")).toString(),
                        Role.valueOf(Objects.requireNonNull(map.get("role")).toString()),
                        StatusUser.valueOf(Objects.requireNonNull(map.get("status")).toString()),
                        new ArrayList<>(),
                        (List<String>) Objects.requireNonNull(map.get("sessions"))
                );
            } else {
                user = new User(
                        Objects.requireNonNull(map.get("id")).toString(),
                        Objects.requireNonNull(map.get("username")).toString(),
                        Objects.requireNonNull(map.get("email")).toString(),
                        Role.valueOf(Objects.requireNonNull(map.get("role")).toString()),
                        StatusUser.valueOf(Objects.requireNonNull(map.get("status")).toString()),
                        (List<String>) Objects.requireNonNull(map.get("subjects")),
                        (List<String>) Objects.requireNonNull(map.get("sessions"))
                );
            }

            return user;

        } catch(Exception e) {
            Log.d("ERROR", "Map Firebase document data is incorrect");
            throw e;
        }
    }
}
