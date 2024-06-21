package com.example.finalproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecipeDetails extends AppCompatActivity {

    ImageView recipeImage;
    TextView recipeName, recipeDescription, recipeIngredients, recipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);

        recipeImage = findViewById(R.id.recipe_image);
        recipeName = findViewById(R.id.recipe_name);
        recipeDescription = findViewById(R.id.recipe_description);
        recipeIngredients = findViewById(R.id.recipeIngredientsContentTextView);
        recipeInstructions = findViewById(R.id.recipeInstructionsContentTextView);

        if (getIntent() != null && getIntent().hasExtra("recipe")) {
            Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

            if (recipe != null) {
                // Set the recipe details to the views
                Glide.with(this).load(recipe.getImage()).into(recipeImage);
                recipeName.setText(recipe.getTitle());
                recipeDescription.setText(recipe.getSummary());

                // Assuming Recipe model has getIngredients() and getInstructions() methods
                recipeIngredients.setText(recipe.getIngredients());
                recipeInstructions.setText(recipe.getInstructions());
            }
        }


    }
}