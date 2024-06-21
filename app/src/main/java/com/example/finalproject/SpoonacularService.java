package com.example.finalproject;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularService {
    @GET("recipes/complexSearch")
    Call<RecipeResponse> searchRecipes(
            @Query("diet") String query,
            @Query("apiKey") String apiKey
    );
}

