package com.app_perso.tutorfinder_v2;

import com.app_perso.tutorfinder_v2.repository.model.User;

public class UserSingleton {
    // private instance, so that it can be
    // accessed by only by getInstance() method
    private static UserSingleton userInstance;
    private User user;

    private UserSingleton(User user) {
        this.user = user;
    }

    public static UserSingleton getInstance(User user) {
        if (userInstance == null) {
            //synchronized block to remove overhead
            synchronized (UserSingleton.class) {
                if(userInstance==null) {
                    // if instance is null, initialize
                    userInstance = new UserSingleton(user);
                }

            }
        }
        return userInstance;
    }

    public User getUser() {
        return user;
    }

    public static void reset() {
        userInstance = null;
    }
}