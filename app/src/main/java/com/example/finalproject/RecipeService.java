package com.example.finalproject;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("recipes/complexSearch")
    Call<Recipe> curatedRecipes(
            @Query("diet") String query,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/complexSearch")
    Call<Recipe> searchRecipes(
            @Query("query") String query,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeDetails(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );
}

