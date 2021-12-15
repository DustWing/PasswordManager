package com.manager.services;

import com.google.protobuf.Empty;
import com.pass.grpc.CreateUserRequest;
import com.pass.grpc.CreateUserServiceGrpc;
import io.grpc.stub.StreamObserver;

public class CreateUserServiceImpl extends CreateUserServiceGrpc.CreateUserServiceImplBase {

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<Empty> responseObserver) {
        request.getUsername();
        request.getMasterPassword();
    }
}
