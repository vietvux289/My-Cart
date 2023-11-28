package hanu.a2_1901040240.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapterMain extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    // Create and inflate the view holder layout
    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    // Set the data in each item view based on its position
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
    }

    // Get the total number of items in the list
    @Override
    public int getItemCount() {
        return 0;
    }
}
