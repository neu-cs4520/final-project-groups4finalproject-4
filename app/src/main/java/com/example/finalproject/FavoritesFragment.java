package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements OnRecipeClickListener{

    private static final String TAG = "FavoritesFragment";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView recyclerViewFavorites;
    private List<Recipe> favorites;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        favorites = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerViewFavorites = view.findViewById(R.id.favorites_recycler_view);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesAdapter = new FavoritesAdapter(getContext(), favorites, this);
        recyclerViewFavorites.setAdapter(favoritesAdapter);

        loadFavorites();

        return view;
    }


    private void loadFavorites() {
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "User is not authenticated");
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            favorites.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                favorites.add(recipe);
                            }
                            favoritesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show();
                            Log.e("FavoritesFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
        Recipe recipe = favorites.get(position);
        //intent.putExtra("recipe_details", recipe);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}