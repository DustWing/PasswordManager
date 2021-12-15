package com.manager.client;

import com.google.protobuf.Empty;
import com.manager.util.AesGcmEncryption;
import com.pass.grpc.PasswordDetails;
import com.pass.grpc.PasswordRequest;
import com.pass.grpc.PasswordResponse;
import com.pass.grpc.PasswordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;

public final class Client {

    private static final Logger logger = LogManager.getLogger(Client.class);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();


        PasswordServiceGrpc.PasswordServiceBlockingStub stub = PasswordServiceGrpc.newBlockingStub(channel);

        PasswordResponse passwordResponse = stub.password(PasswordRequest.newBuilder().setUsername("admin").build());

        channel.shutdown();

        logger.info(passwordResponse.toString());

        List<PasswordDetails> passwordList = passwordResponse.getPasswordsList();

        SecretKey secretKey;
        try {
            secretKey = AesGcmEncryption.generateKeyFromPassword("password".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return;
        }


        passwordList.forEach(e -> {
            final byte[] userNameIV = Base64.getDecoder().decode(e.getUsername().substring(0, 16).getBytes(StandardCharsets.UTF_8));
            final byte[] passwordIV = Base64.getDecoder().decode(e.getPassword().substring(0, 16).getBytes(StandardCharsets.UTF_8));


            try {
                final String username =
                        AesGcmEncryption.decryptFromBase64(
                                e.getUsername().substring(16), secretKey, userNameIV
                        );

                final String password = AesGcmEncryption.decryptFromBase64(
                        e.getPassword().substring(16), secretKey, passwordIV
                );

                final String url = e.getDomain();


                final String description = e.getDescription();

                logger.info("Username:" + username +
                        "\t Password:" + password +
                        "\t Url:" + url +
                        "\t Description:" + description);

            } catch (Exception ex) {
                logger.error("", ex);
            }
        });


    }
}
