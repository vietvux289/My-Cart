package hanu.a2_1901040240;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040240.adapter.ProductAdapter;
import hanu.a2_1901040240.db.ProductManager;
import hanu.a2_1901040240.models.Product;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView totalPrice = findViewById(R.id.tvTotalPrice);
        ProductManager productManager = ProductManager.getInstance(this);

        // Retrieve all products and filter out those with quantity zero
        List<Product> productsList = productManager.all();
        List<Product> productListCart = new ArrayList<>();
        for (Product product : productsList) {
            int quantity = product.getQuantity();
            if (quantity > 0) {
                productListCart.add(product);
            }
        }

        // Setup the RecyclerView and adapter
        RecyclerView rvSelectedProducts = findViewById(R.id.rvAddedProduct);
        rvSelectedProducts.setLayoutManager(new GridLayoutManager(this, 1));
        ProductAdapter productAdapter;
        productAdapter = new ProductAdapter(productListCart, totalPrice, null);

        rvSelectedProducts.setAdapter(productAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu layout for the ActionBar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Select icon in action bar to enter Main or Cart Activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle ActionBar item clicks
        if (item.getItemId() == R.id.homeMenu) {
            // start MainActivity when home icon is clicked
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
