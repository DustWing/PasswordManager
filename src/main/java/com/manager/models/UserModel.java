package com.manager.models;

import java.time.LocalDateTime;

public record UserModel(String id, String username, String password,
                        LocalDateTime createdOn) {

    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public LocalDateTime createdOn() {
        return createdOn;
    }
}
