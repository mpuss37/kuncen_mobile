package com.example.kuncen.Model;

public class DataModel {
    //    private int id_data, id_user;
    private int id_user;
    private String name_website, username, password;

    public DataModel(int id_user, String name_website, String username, String password) {
        this.id_user = id_user;
        this.name_website = name_website;
        this.username = username;
        this.password = password;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getName_website() {
        return name_website;
    }

    public void setName_website(String name_website) {
        this.name_website = name_website;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}