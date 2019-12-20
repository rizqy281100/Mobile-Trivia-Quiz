package com.example.quizapp.Model;

public class Catagory {
    private String Name;
    private String Image, Description;

    public Catagory() {
    }

    public Catagory(String name, String image) {
        Name = name;
        Image = image;
    }

    public Catagory(String name, String image, String description) {
        Name = name;
        Image = image;
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
