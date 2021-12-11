package com.manager.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manager.model.Password;
import com.manager.util.AesGcmEncryption;
import com.pass.grpc.PasswordRequest;
import com.pass.grpc.PasswordResponse;
import com.pass.grpc.PasswordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();


        PasswordServiceGrpc.PasswordServiceBlockingStub stub = PasswordServiceGrpc.newBlockingStub(channel);

        PasswordResponse passwordResponse = stub.password(PasswordRequest.newBuilder().build());

        channel.shutdown();


        List<Password> passwordList = new Gson().fromJson(
                passwordResponse.getPasswords().toStringUtf8(),
                new TypeToken<ArrayList<Password>>() {
                }.getType()
        );

        SecretKey secretKey;
        try {
            secretKey = AesGcmEncryption.generateKeyFromPassword("password".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return;
        }


        System.out.println(passwordResponse.getPasswords().toStringUtf8());


        passwordList.forEach(e -> {
            final byte[] IV = Base64.getDecoder().decode(e.getIv().getBytes(StandardCharsets.UTF_8));

            try {
                final String user =
                        AesGcmEncryption.decryptFromBase64(
                                e.getUsername(), secretKey, IV
                        );

                final String password = AesGcmEncryption.decryptFromBase64(
                        e.getPassword(), secretKey, IV
                );

                final String url = AesGcmEncryption.decryptFromBase64(
                        e.getUrl(), secretKey, IV
                );

                final String description = AesGcmEncryption.decryptFromBase64(
                        e.getDescription(), secretKey, IV
                );


                System.out.println("User:" + user +
                        "\t Password:" + password +
                        "\t Url:" + url +
                        "\t Description:" + description
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


    }
}
