package com.example.finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.OnRecipeClickListener;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{

    Context context;
    private List<Recipe> favorites;
    private static final String TAG = "FavoritesAdapter";
    OnRecipeClickListener onRecipeClickListener;

    public FavoritesAdapter(Context context, List<Recipe> favorites, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.favorites = favorites != null ? favorites : new ArrayList<>();
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoritesAdapter.FavoritesViewHolder(view, onRecipeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {
        Recipe recipe = favorites.get(position);
        String recipeId = recipe.getTitle();
        Log.d(TAG, "Binding data for place: " + recipe);
        holder.recipeName.setText(recipeId);
        Glide.with(holder.itemView.getContext()).load(recipe.getImage()).into(holder.recipeImage);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = holder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userId = auth.getCurrentUser().getUid();

                    db.collection("users")
                            .document(userId)
                            .collection("favorites")
                            .document(recipeId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                favorites.remove(adapterPos);
                                notifyItemRemoved(adapterPos);
                                notifyItemRangeChanged(adapterPos, favorites.size());
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "error deleting item from favorites", e);
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updatePlacesList(List<Recipe> newFavorites) {
        if (newFavorites == null) {
            Log.d(TAG, "New favorites list is null");
            return;
        }
        Log.d(TAG, "Current favorites list before update: " + favorites);
        Log.d(TAG, "Updating favorites list in adapter with: " + newFavorites);
        favorites.clear();
        Log.d(TAG, "Favorites list after update: " + favorites);
        notifyDataSetChanged();
        Log.d(TAG, "Favorites list updated in adapter: " + favorites);
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        OnRecipeClickListener onRecipeClickListener;
        TextView recipeName;
        ImageView recipeImage;
        Button removeButton;

        FavoritesViewHolder(@NonNull View itemView, OnRecipeClickListener onRecipeClickListener) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.favorite_item_title);
            recipeImage = itemView.findViewById(R.id.favorite_item_image_view);
            removeButton = itemView.findViewById(R.id.remove_button);
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
