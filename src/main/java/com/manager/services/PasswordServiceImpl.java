package com.manager.services;


import com.manager.dao.PasswordDao;
import com.pass.grpc.PasswordDetails;
import com.pass.grpc.PasswordRequest;
import com.pass.grpc.PasswordResponse;
import com.pass.grpc.PasswordServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.stream.Collectors;


public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {



    @Override
    public void password(PasswordRequest request, StreamObserver<PasswordResponse> responseObserver) {


        responseObserver.onNext(
                PasswordResponse.newBuilder()
                        .addAllPasswords(
                                PasswordDao.getPasswordList(request.getUsername()).stream().map(e -> PasswordDetails.newBuilder()
                                        .setId(e.id())
                                        .setUsername(e.username())
                                        .setPassword(e.password())
                                        .setDescription(e.description())
                                        .setCategory(e.category())
                                        .setDomain(e.domain())
                                        .build()
                                ).collect(Collectors.toList())
                        )
                        .build()
        );
        responseObserver.onCompleted();

    }

}
