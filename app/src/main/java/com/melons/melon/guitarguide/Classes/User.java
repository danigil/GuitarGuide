package com.melons.melon.guitarguide;

public class User{
    public String password;
    public String username;

    public User(){

    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
