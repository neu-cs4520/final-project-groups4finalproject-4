package com.example.finalproject.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.Recipe;
import com.example.finalproject.RecipeResponse;
import com.example.finalproject.RetrofitClient;
import com.example.finalproject.SpoonacularService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {

    private MutableLiveData<List<Recipe>> recipeList;
    private SpoonacularService spoonacularService;

    public RecipeViewModel() {
        spoonacularService = RetrofitClient.getRetrofitInstance().create(SpoonacularService.class);
    }

    public LiveData<List<Recipe>> getRecipes(String diet) {
        if (recipeList == null) {
            recipeList = new MutableLiveData<>();
            loadRecipes(diet);
        }
        return recipeList;
    }

    private void loadRecipes(String diet) {
        new FetchRecipesTask().execute(diet);
    }

    private class FetchRecipesTask extends AsyncTask<String, Void, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(String... params) {
            String diet = params[0];
            return fetchRecipesFromApi(diet);
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            recipeList.setValue(recipes);
        }

        private List<Recipe> fetchRecipesFromApi(String diet) {
            try {
                Call<RecipeResponse> call = spoonacularService.searchRecipes(diet, "0bb9c0152f8e4cceb77330f6e5abca54");
                Response<RecipeResponse> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().getResults();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }
}
