package com.example.friendfinderapp;

public class ThumbnailPlace {
    private String id, place_name, place_owner, price, location, description,
    place_picture, place_open_time, place_close_time, contact_person, category;

    public ThumbnailPlace(String id, String place_name, String place_owner, String price, String location, String description, String place_picture, String place_open_time, String place_close_time, String contact_person, String category) {
        this.id = id;
        this.place_name = place_name;
        this.place_owner = place_owner;
        this.price = price;
        this.location = location;
        this.description = description;
        this.place_picture = place_picture;
        this.place_open_time = place_open_time;
        this.place_close_time = place_close_time;
        this.contact_person = contact_person;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_owner() {
        return place_owner;
    }

    public void setPlace_owner(String place_owner) {
        this.place_owner = place_owner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_picture() {
        return place_picture;
    }

    public void setPlace_picture(String place_picture) {
        this.place_picture = place_picture;
    }

    public String getPlace_open_time() {
        return place_open_time;
    }

    public void setPlace_open_time(String place_open_time) {
        this.place_open_time = place_open_time;
    }

    public String getPlace_close_time() {
        return place_close_time;
    }

    public void setPlace_close_time(String place_close_time) {
        this.place_close_time = place_close_time;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
