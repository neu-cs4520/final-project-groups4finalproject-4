package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailsActivity";
    ImageView recipeImage;
    TextView recipeName, recipeDescription, recipeIngredients, recipeInstructions, recipeDuration;
    ImageButton backButton, sendButton, addToFavorites;
    RecipeDetails recipeDetails;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recipeImage = findViewById(R.id.recipe_image);
        recipeName = findViewById(R.id.recipe_name);
        recipeDescription = findViewById(R.id.recipe_description);
        recipeIngredients = findViewById(R.id.recipeIngredientsContentTextView);
        recipeInstructions = findViewById(R.id.recipeInstructionsContentTextView);
        recipeDuration = findViewById(R.id.recipe_duration);
        backButton = findViewById(R.id.back_button);
        addToFavorites = findViewById(R.id.add_to_favorites_button);
        sendButton = findViewById(R.id.send_button);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        if (recipeId != -1) {
            getRecipeDetails(recipeId);
        } else {
            Log.e(TAG, "Invalid Recipe Id");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeDetails == null) {
                    Toast.makeText(RecipeDetailsActivity.this, "Recipe details not loaded yet :/", Toast.LENGTH_SHORT).show();
                } else {
                    String shareMessage = "Check out this recipe: " + recipeDetails.getTitle() + "\n" + recipeDetails.getSourceUrl();
                    Log.i("ShareMessage", shareMessage);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Recipe: " + recipeDetails.getTitle());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                    startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
                }
            }
        });
    }

    private void getRecipeDetails(int recipeId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            RecipeService service = RetrofitClient.getRetrofitInstance().create(RecipeService.class);
            Call<RecipeDetails> call = service.getRecipeDetails(recipeId, "e5afecc7fd7b418591fffe0909278f72");

            try {
                Response<RecipeDetails> response = call.execute();
                if (response.isSuccessful()) {
                    recipeDetails = response.body();
                    if (recipeDetails != null) {
                        runOnUiThread(() -> {
                            Glide.with(this).load(recipeDetails.getImage()).into(recipeImage);
                            recipeName.setText(recipeDetails.getTitle());
                            Log.i(TAG, "Recipe title" + recipeDetails.getTitle());
                            recipeDescription.setText(Html.fromHtml(recipeDetails.getSummary()));
                            Log.i(TAG, "Recipe description" + recipeDetails.getSummary());
                            recipeIngredients.setText(formatIngredients(recipeDetails.getExtendedIngredients()));
                            Log.i(TAG, "Recipe ingredients" + recipeDetails.getExtendedIngredients());
                            recipeInstructions.setText(Html.fromHtml(recipeDetails.getInstructions()));
                            Log.i(TAG, "Recipe instructions" + recipeDetails.getInstructions());
                            recipeDuration.setText(recipeDetails.getReadyInMinutes() + " minutes");
                            Log.i(TAG, "Recipe duration" + recipeDetails.getReadyInMinutes());

                        });
                    } else {
                        Log.e(TAG, "Recipe details are null");
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                } else {
                    Log.e(TAG, "Request failed with code: " + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Failed to load recipe details", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (IOException e) {
                Log.e(TAG, "Network request failed", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Network request failed", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private String formatIngredients(List<RecipeDetails.Ingredient> ingredients) {
        StringBuilder formattedIngredients = new StringBuilder();

        for (RecipeDetails.Ingredient ingredient : ingredients) {
            formattedIngredients.append(ingredient.getOriginal()).append("\n");
        }

        return formattedIngredients.toString();
    }

    private void addToFavorites() {
        String userId = auth.getCurrentUser().getUid();
        Recipe recipe = recipeDetails.toRecipe();
//        favoritesData.put("id", recipeDetails.getId());
//        favoritesData.put("title", recipeDetails.getTitle());
//        favoritesData.put("description", recipeDetails.getSummary());
//        favoritesData.put("image", recipeDetails.getImage());
//        favoritesData.put("readyInMinutes", recipeDetails.getReadyInMinutes());
//        favoritesData.put("ingredients", convertIngredients(recipeDetails.getExtendedIngredients()));
//        favoritesData.put("instructions", recipeDetails.getInstructions());
//        favoritesData.put("sourceUrl", recipeDetails.getSourceUrl());

        if (recipeDetails == null) {
            Toast.makeText(this, "No recipe details available", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "User ID: " + userId);
        Log.d(TAG, "Recipe ID: " + recipe.getId());
        Log.d(TAG, "Recipe Data: " + recipe);

        db.collection("users")
                .document(userId)
                .collection("favorites")
                .document(String.valueOf(recipeDetails.getId()))
                .set(recipe)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Successfully added to Firestore");
                    if (!isFinishing()) {
                        Toast.makeText(RecipeDetailsActivity.this, "Successfully added to favorites!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add recipe to Firestore", e);
                    if (!isFinishing()) {
                        Toast.makeText(RecipeDetailsActivity.this, "Failed to add recipe to favorites.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<Map<String, Object>> convertIngredients(List<RecipeDetails.Ingredient> ingredients) {
        List<Map<String, Object>> ingredientList = new ArrayList<>();
        for (RecipeDetails.Ingredient ingredient : ingredients) {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("original", ingredient.getOriginal());
            ingredientList.add(ingredientMap);
        }
        return ingredientList;
    }
}