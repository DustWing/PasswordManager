package com.manager.model;

public class Password {

    private final String username;

    private final String password;

    private final String url;

    private final String description;

    private final String iv;

    public Password(String username, String password, String url, String description, String iv) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.description = description;
        this.iv = iv;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getIv() {
        return iv;
    }
}
