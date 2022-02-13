package com.manager.dao;

import com.manager.models.PasswordModel;
import com.manager.util.Cache;

import java.util.List;

public class PasswordDao {



    public static List<PasswordModel> getPasswordList(String username) {
        return Cache.cachedPasswords.get(username);
    }

}
