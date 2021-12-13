package com.manager.services;


import com.manager.util.AesGcmEncryption;
import com.pass.grpc.PasswordDetails;
import com.pass.grpc.PasswordRequest;
import com.pass.grpc.PasswordResponse;
import com.pass.grpc.PasswordServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    private static final Logger logger = LogManager.getLogger(PasswordServiceImpl.class);


    @Override
    public void password(PasswordRequest request, StreamObserver<PasswordResponse> responseObserver) {

        logger.info("PasswordServiceImpl [password] started...");

        responseObserver.onNext(
                PasswordResponse.newBuilder()
                        .addAllPasswords(createPassList())
                        .build()
        );
        responseObserver.onCompleted();
        logger.info("PasswordServiceImpl [password] finish.");

    }


    private List<PasswordDetails> createPassList() {
        List<PasswordDetails> passwords = new ArrayList<>();

        SecretKey secretKey;
        try {
            secretKey = AesGcmEncryption.generateKeyFromPassword("password".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return passwords;
        }

        for (int i = 0; i < 100; i++) {


            byte[] iv = AesGcmEncryption.generateIv();

            final String user = "user" + i;
            final String password = "pass1" + i;
            final String url = "www.example" + i + ".com";
            final String description = "yousuck1" + i;
            final String ivStr = Base64.getEncoder().encodeToString(iv);

            try {

                passwords.add(
                        PasswordDetails.newBuilder()
                                .setUsername(AesGcmEncryption.encryptToBase64(user.getBytes(StandardCharsets.UTF_8), secretKey, iv))
                                .setPassword(AesGcmEncryption.encryptToBase64(password.getBytes(StandardCharsets.UTF_8), secretKey, iv))
                                .setUrl(AesGcmEncryption.encryptToBase64(url.getBytes(StandardCharsets.UTF_8), secretKey, iv))
                                .setDescription(AesGcmEncryption.encryptToBase64(description.getBytes(StandardCharsets.UTF_8), secretKey, iv))
                                .setIv(ivStr)
                                .build()

                );
            } catch (Exception e) {
                logger.error("PasswordServiceImpl [createPassList]", e);
            }


        }

        return passwords;

    }


}
