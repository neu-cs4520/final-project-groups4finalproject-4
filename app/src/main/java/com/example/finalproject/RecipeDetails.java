package com.example.finalproject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetails implements Serializable {

    public RecipeDetails() {}

    // Constructor
    public RecipeDetails(int id, String title, String summary, String image, int readyInMinutes, List<Ingredient> extendedIngredients, String instructions, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
        this.extendedIngredients = extendedIngredients;
        this.instructions = instructions;
        this.sourceUrl = sourceUrl;
    }

    public Recipe toRecipe() {
        List<String> ingredients = new ArrayList<>();
        for (Ingredient ingredient : extendedIngredients) {
            ingredients.add(ingredient.getOriginal());
        }
        return new Recipe(id, title, summary, image, readyInMinutes, ingredients, instructions, sourceUrl);
    }


    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("summary")
    private String summary;

    @SerializedName("readyInMinutes")
    private int readyInMinutes;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("sourceUrl")
    private String sourceUrl;

    @SerializedName("extendedIngredients")
    private List<Ingredient> extendedIngredients;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public static class Ingredient implements Serializable {

        @SerializedName("original")
        private String original;

        public Ingredient(String original) {
            this.original = original;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
    }



}
