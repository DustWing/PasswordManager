package com.manager.dao;

import com.manager.models.UserModel;
import com.manager.util.Cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserDao {


    public static void createUser(UserModel user) {
        Cache.cachedUsers.put(user.username(), user);
    }

    public static UserModel getUser(String userName) {
        return Cache.cachedUsers.get(userName);
    }

}
