package com.manager.dao;

import com.manager.models.PasswordModel;
import com.manager.util.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PasswordDao {

    private static final Logger logger = LogManager.getLogger(PasswordDao.class);


    public static List<PasswordModel> getPasswordList(String username) {
        logger.info("Getting password for user :" + username);
        return Cache.cachedPasswords.get(username);
    }

}
