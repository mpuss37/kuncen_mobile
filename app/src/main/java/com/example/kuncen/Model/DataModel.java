package com.example.kuncen.Model;

public class DataModel {
    private String name_website, username, password;
    private int id_data;

    public DataModel(int id_data, String name_website, String username, String password) {
        this.id_data = id_data;
        this.name_website = name_website;
        this.username = username;
        this.password = password;
    }

    public int getId_data() {
        return id_data;
    }

    public void setId_data(int id_data) {
        this.id_data = id_data;
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