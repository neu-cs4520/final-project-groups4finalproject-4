package com.example.finalproject;

import android.content.Context;
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


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    OnRecipeClickListener onRecipeClickListener;

    public SearchAdapter(Context context, List<Recipe> recipeList, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.recipeList = recipeList;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view, onRecipeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        if (recipe != null) {
            holder.recipeTitleTextView.setText(recipe.getTitle());
            Glide.with(holder.itemView.getContext()).load(recipe.getImage()).into(holder.recipeImageView);

//            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(context, RecipeDetailsActivity.class);
//                intent.putExtra("recipe_id", recipe.getId());
//                context.startActivity(intent);
//            });
        } else {
            Log.e("RecipeAdapter", "Recipe data is null");
        }

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        OnRecipeClickListener onRecipeClickListener;
        ImageView recipeImageView;
        TextView recipeTitleTextView;

        public RecipeViewHolder(@NonNull View itemView, OnRecipeClickListener onRecipeClickListener) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.favorite_image_view);
            recipeTitleTextView = itemView.findViewById(R.id.favorite_title);
            this.onRecipeClickListener = onRecipeClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecipeClickListener != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            onRecipeClickListener.onRecipeClick(pos);
                        }
                    }
                }
            });


        }
    }
}
