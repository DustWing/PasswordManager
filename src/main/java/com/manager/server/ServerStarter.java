package com.manager.server;

import com.manager.services.CreateUserServiceImpl;
import com.manager.services.PasswordServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public final class ServerStarter {


    public static void main(String[] args) {

        Server server = ServerBuilder
                .forPort(8080)
                .addService(new PasswordServiceImpl())
                .addService(new CreateUserServiceImpl())
                .addService(new PasswordServiceImpl())
                .build();

        try {
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
