package com.example.wbs.core.models;

public class SharedUserModel {
    private int user_id;
    private String username;
    private String role;
    private String nama;
    private boolean isLogin;

    public SharedUserModel(int user_id, String username, String role, String nama, boolean isLogin) {
        this.user_id = user_id;
        this.username = username;
        this.role = role;
        this.nama = nama;
        this.isLogin = isLogin;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
