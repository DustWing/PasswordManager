package com.manager.services;


import com.google.protobuf.ByteString;
import com.manager.model.Password;
import com.manager.util.AesGcmEncryption;
import com.manager.util.GsonUtils;
import com.pass.grpc.PasswordRequest;
import com.pass.grpc.PasswordResponse;
import com.pass.grpc.PasswordServiceGrpc;
import io.grpc.stub.StreamObserver;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    @Override
    public void password(PasswordRequest request, StreamObserver<PasswordResponse> responseObserver) {


        responseObserver.onNext(
                PasswordResponse.newBuilder()
                        .setPasswords(createResponseTest())
                        .build()
        );
        responseObserver.onCompleted();
    }

    private ByteString createResponseTest() {


        String json = GsonUtils.GSON.toJson(createPassList());


        return ByteString.copyFromUtf8(json);
    }

    private List<Password> createPassList() {
        List<Password> passwords = new ArrayList<>();

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
                        new Password(AesGcmEncryption.encryptToBase64(user.getBytes(StandardCharsets.UTF_8), secretKey, iv),
                                AesGcmEncryption.encryptToBase64(password.getBytes(StandardCharsets.UTF_8), secretKey, iv),
                                AesGcmEncryption.encryptToBase64(url.getBytes(StandardCharsets.UTF_8), secretKey, iv),
                                AesGcmEncryption.encryptToBase64(description.getBytes(StandardCharsets.UTF_8), secretKey, iv),
                                ivStr)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return passwords;

    }


}
