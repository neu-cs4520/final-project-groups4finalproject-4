package com.example.finalproject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recipe implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    // Add other necessary fields and their getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Implement methods for ingredients and instructions as needed
    public String getIngredients() {
        // Placeholder method for getting ingredients
        return "Ingredients list here";
    }

    public String getInstructions() {
        // Placeholder method for getting instructions
        return "Instructions here";
    }

    public int getSummary() {
        return 1;
    }
}
