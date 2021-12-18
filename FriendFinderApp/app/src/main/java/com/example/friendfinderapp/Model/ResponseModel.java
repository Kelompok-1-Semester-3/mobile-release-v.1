package com.example.friendfinderapp.Model;

import com.example.friendfinderapp.Category;

import java.util.List;

public class ResponseModel {
    public List<User_Model> dataUser;
    public List userEvent;

    public List getUserEvent() {
        return userEvent;
    }

    public void setUserEvent(List userEvent) {
        this.userEvent = userEvent;
    }

    public String pesan, fullname, status, profile, id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User_Model> getDataUser() {
        return dataUser;
    }

    public void setDataUser(List<User_Model> dataUser) {
        this.dataUser = dataUser;
    }
}
