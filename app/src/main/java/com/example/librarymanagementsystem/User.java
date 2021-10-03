package com.example.librarymanagementsystem;

public class User {
    String user_name,organization,email;

    public User() {
    }

    public User(String user_name, String organization, String email) {
        this.user_name = user_name;
        this.organization = organization;
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
