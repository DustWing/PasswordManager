package com.manager.server;

import com.manager.services.CreateUserServiceImpl;
import com.manager.services.PasswordServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class ServerStarter {

    private static final Logger logger = LogManager.getLogger(ServerStarter.class);


    public static void main(String[] args) {

        logger.info("Staring server....");

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
