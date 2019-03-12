package com.kontrakanelite.test;

public class User {
    private String email;
    private String password;
    private String idUser;

    public User(){

    }

    public User(String idUser, String email, String password) {
        this.email = email;
        this.password = password;
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
