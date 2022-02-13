package com.manager.services;

import com.google.protobuf.Empty;
import com.pass.grpc.AddPasswordsRequest;
import com.pass.grpc.AddPasswordsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class AddPasswordsServiceImpl extends AddPasswordsServiceGrpc.AddPasswordsServiceImplBase {
    @Override
    public void addPasswords(AddPasswordsRequest request, StreamObserver<Empty> responseObserver) {


        request.getPasswordsList().forEach(e->{



        });
    }
}
