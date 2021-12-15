package com.abdulkarimalbaik.dev.photogram.Model;

public class CipherImage {

    private int id , length_encrypt_image;
    private byte[] picture;
    private String title , display_name , description , date_added;

    public CipherImage() {
    }

    public CipherImage(int id, byte[] picture, String title, String display_name, String description, String date_added , int length_encrypt_image) {
        this.id = id;
        this.picture = picture;
        this.title = title;
        this.display_name = display_name;
        this.description = description;
        this.date_added = date_added;
        this.length_encrypt_image = length_encrypt_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public int getLength_encrypt_image() {
        return length_encrypt_image;
    }

    public void setLength_encrypt_image(int length_encrypt_image) {
        this.length_encrypt_image = length_encrypt_image;
    }
}
