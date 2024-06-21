package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {


        Recipe recipe = recipeList.get(position);
        if (recipe != null) {
            holder.recipeTitleTextView.setText(recipe.getTitle());
            Glide.with(holder.itemView.getContext()).load(recipe.getImage()).into(holder.recipeImageView);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RecipeDetails.class);
                intent.putExtra("recipe", recipe);
                context.startActivity(intent);
            });
        } else {
            Log.e("RecipeAdapter", "Recipe data is null");
        }

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImageView;
        TextView recipeTitleTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.favorite_image_view);
            recipeTitleTextView = itemView.findViewById(R.id.favorite_title);
        }
    }
}
