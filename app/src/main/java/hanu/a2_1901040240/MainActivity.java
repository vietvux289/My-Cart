package hanu.a2_1901040240;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1901040240.adapter.ProductAdapter;
import hanu.a2_1901040240.db.DbHelper;
import hanu.a2_1901040240.db.ProductManager;
import hanu.a2_1901040240.models.Constants;
import hanu.a2_1901040240.models.Product;

public class MainActivity extends AppCompatActivity {
    // Declare and initialize fields
    boolean isCheckedInDb = false;
    EditText edtOfSearch;
    ImageView btnSearch;
    List<Product> productList;
    List<Product> productListForCheckDb;
    ProductAdapter productAdapter;
    ProductManager productManager;
    String jsonURL = "https://hanu-congnv.github.io/mpr-cart-api/products.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtOfSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        RecyclerView rvListProducts = findViewById(R.id.rvProductList);

        // populate all product from JSON data
        LoadDataJSON fetchJson = new LoadDataJSON();
        fetchJson.populateProduct(jsonURL);

        productManager = ProductManager.getInstance(MainActivity.this);
        productList = new ArrayList<>();

        // set up the RecyclerView with 2 cols GridLayout and the productAdapter
        rvListProducts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        productAdapter = new ProductAdapter(productList, null, edtOfSearch);
        rvListProducts.setAdapter(productAdapter);

        // handle onClick event for Search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                List<Product> productsListMain = productManager.all();
                List<Product> searchList = new ArrayList<>();
                String keyword = edtOfSearch.getText().toString().toLowerCase();

                // Iterate through each product in the productsListMain
                for (Product product : productsListMain) {
                    if (product.getDescription().toLowerCase().contains(keyword)) {
                        searchList.add(product);
                    }
                }
                productList.clear();
                productList.addAll(searchList);
                productAdapter.notifyDataSetChanged();
                System.out.println("Filter Done!");
            }
        });
    }

    // Create the options menu for the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle select menu Cart icon click event "go to CartActivity"
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int menuId = menuItem.getItemId();
        if (menuId == R.id.cartMenu) {
            List<Product> productListInCart = productManager.all();
            Intent actIntent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(actIntent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // Load JSON data of products list
    protected class LoadDataJSON {
        public void populateProduct(String link) {
            // Send task to the thread pool & start executing
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(link);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        Scanner sc = new Scanner(inputStream);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while (sc.hasNextLine()) {
                            line = sc.nextLine();
                            stringBuilder.append(line);
                        }
                        final String result = stringBuilder.toString();

                        Constants.handler.post(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                                if (result == null) {
                                    Toast.makeText(MainActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                productListForCheckDb = new ArrayList<>();

                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    DbHelper dbHelper = new DbHelper(MainActivity.this);
                                    boolean isExistedList = dbHelper.isExistedTable();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        int id = jsonObject.getInt("id");
                                        String description = jsonObject.getString("name");
                                        int unitPrice = jsonObject.getInt("unitPrice");
                                        String thumbnail = jsonObject.getString("thumbnail");

                                        productList.add(new Product(id, thumbnail, description, unitPrice, 0));
                                        if ((!isExistedList)) {
                                            productManager.addProduct(new Product(id, thumbnail, description, unitPrice, 0));
                                        }
                                    }
                                    isCheckedInDb = true;
                                    productAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        System.out.println("Populated products from JSON data!");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}