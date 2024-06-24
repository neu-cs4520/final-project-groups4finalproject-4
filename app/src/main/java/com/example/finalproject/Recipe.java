package com.example.finalproject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {


    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("results")
    private List<Recipe> results;

    private int readyInMinutes;
    private List<String> ingredients;
    private String instructions;
    private String description;
    private String sourceUrl;

    // Empty constructor required for Firestore
    public Recipe() {}

    // Constructor
    public Recipe(int id, String title, String description, String image, int readyInMinutes, List<String> ingredients, String instructions, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.sourceUrl = sourceUrl;
    }

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }

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
    }

