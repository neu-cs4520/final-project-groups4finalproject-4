package com.example.finalproject;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";

    private FloatingActionButton fab;

    private RecyclerView shoppingListRecycler;
    private ShoppingListAdapter shoppingListAdapter;
    private List<String> shoppingList = new ArrayList<>();

    FirebaseAuth auth;
    FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shoppingListRecycler = view.findViewById(R.id.shopping_list_recycler);
        shoppingListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        shoppingListAdapter = new ShoppingListAdapter(shoppingList);
        shoppingListRecycler.setAdapter(shoppingListAdapter);


        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewShoppingItemDialog();
            }
        });

        loadShoppingList();

    }

    private void addNewShoppingItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("What Do You Need? Add It to Your Shopping List!");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_shopping_list, null);

        EditText itemEt = customLayout.findViewById(R.id.shoppingListItemInput);

        builder.setView(customLayout);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String shoppingListItem = itemEt.getText().toString().trim();

            if (!TextUtils.isEmpty(shoppingListItem)) {
                addShoppingListItemToFirestore(shoppingListItem);
                loadShoppingList();
            } else {
                Toast.makeText(getActivity(), "Please add an item!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) ->
                dialog.cancel());
        builder.show();
    }

    private void addShoppingListItemToFirestore(String item) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Adding shopping list item to Firestore: " + item);

        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> shoppingListItem = new HashMap<>();
        shoppingListItem.put("item", item);

        db.collection("users")
                .document(userId)
                .collection("shoppingList")
                .add(shoppingListItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Item successfully added to shopping list!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, shoppingListItem + "was added to the shopping list with ID" + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to add item to shopping list :/", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error adding item to shopping list" + e);
                });
    }

    private void loadShoppingList() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("shoppingList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> newShoppingList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String item = document.getString("item");
                                newShoppingList.add(item);
                                ShoppingListAdapter adapter = new ShoppingListAdapter(newShoppingList);
                                shoppingListRecycler.setAdapter(adapter);
                                Log.i(TAG, "Shopping List in Fragment:" + newShoppingList);
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to load shopping list", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}