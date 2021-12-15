package com.manager.models;

public record PasswordModel(String id, String username, String password,
                            String domain, String description, String category) {

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
    public String domain() {
        return domain;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String category() {
        return category;
    }
}
