package com.example.finalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    List<String> shoppingListItems;

    public ShoppingListAdapter(List<String> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        String item = shoppingListItems.get(position);
        holder.itemTextView.setText(item);
        holder.delete.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = auth.getCurrentUser().getUid();

                db.collection("users")
                        .document(userId)
                        .collection("shoppingList")
                        .whereEqualTo("item", item)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("users")
                                            .document(userId)
                                            .collection("shoppingList")
                                            .document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                shoppingListItems.remove(adapterPos);
                                                notifyItemRemoved(adapterPos);
                                                notifyItemRangeChanged(adapterPos, shoppingListItems.size());
                                            })
                                            .addOnFailureListener(e -> Log.w("ShoppingListAdapter", "Error deleting item from shopping list", e));
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTextView;
        public ImageButton delete;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
            delete = itemView.findViewById(R.id.delete_button);
        }
    }
}


