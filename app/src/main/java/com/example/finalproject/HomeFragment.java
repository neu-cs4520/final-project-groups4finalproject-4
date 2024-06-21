package com.example.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    EditText searchEditText;
    Button searchButton;
    
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeAdapter(getContext(), recipeList);
        recipesRecyclerView.setAdapter(recipeAdapter);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            searchRecipes(query);
        });

        return view;
    }

    private void searchRecipes(String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            SpoonacularService service = RetrofitClient.getRetrofitInstance().create(SpoonacularService.class);
            Call<RecipeResponse> call = service.searchRecipes(query, "e5afecc7fd7b418591fffe0909278f72");

            try {
                Response<RecipeResponse> response = call.execute();
                if (response.isSuccessful()) {
                    RecipeResponse recipeResponse = response.body();
                    if (recipeResponse != null) {
                        recipeList.clear();
                        recipeList.addAll(recipeResponse.getResults());
                        getActivity().runOnUiThread(() -> recipeAdapter.notifyDataSetChanged());
                    }
                } else {
                    Log.e("HomeFragment", "Request failed with code: " + response.code());
                }
            } catch (IOException e) {
                Log.e("HomeFragment", "Network request failed", e);
            }
        });
    }
}