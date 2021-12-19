package com.manager.util;

import com.manager.models.PasswordModel;
import com.manager.models.UserModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache {

    private static final Logger logger = LogManager.getLogger(Cache.class);


    public static final ConcurrentHashMap<String, List<PasswordModel>> cachedPasswords;
    public static final ConcurrentMap<String, UserModel> cachedUsers;


    static {
        cachedPasswords = new ConcurrentHashMap<>();
        cachedUsers = new ConcurrentHashMap<>();

        final List<PasswordModel> passwordModels = initPasswords();
        cachedPasswords.put("admin", passwordModels);

        cachedUsers.put("admin", new UserModel(
                        UUID.randomUUID().toString(),
                        "admin",
                        "password",
                        LocalDateTime.now()
                )
        );

    }


    private static List<PasswordModel> initPasswords() {
        List<PasswordModel> passwords = new ArrayList<>();

        SecretKey secretKey;
        try {
            secretKey = AesGcmEncryption.generateKeyFromPassword("password".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return passwords;
        }

        for (int i = 0; i < 100; i++) {


            byte[] userNameIVBytes = AesGcmEncryption.generateIv();
            byte[] passwordIVBytes = AesGcmEncryption.generateIv();

            final String user = "user" + i;
            final String password = "pass1" + i;
            final String domain = "www.example" + i + ".com";
            final String description = "yousuck1" + i;


            final String userNameIV = Base64.getEncoder().encodeToString(userNameIVBytes);
            final String passwordIV = Base64.getEncoder().encodeToString(passwordIVBytes);

            try {

                passwords.add(
                        new PasswordModel(
                                UUID.randomUUID().toString(),
                                userNameIV + AesGcmEncryption.encryptToBase64(user.getBytes(StandardCharsets.UTF_8), secretKey, userNameIVBytes),
                                passwordIV + AesGcmEncryption.encryptToBase64(password.getBytes(StandardCharsets.UTF_8), secretKey, passwordIVBytes),
                                domain,
                                description,
                                "default")
                );
            } catch (Exception e) {
                logger.error("PasswordServiceImpl [createPassList]", e);
            }


        }

        return passwords;

    }
}
