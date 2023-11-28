package hanu.a2_1901040240.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040240.MainActivity;
import hanu.a2_1901040240.R;
import hanu.a2_1901040240.db.DbHelper;
import hanu.a2_1901040240.db.ProductManager;
import hanu.a2_1901040240.models.Constants;
import hanu.a2_1901040240.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private EditText edtSearch;
    private List<Product> productsList;
    private TextView tvTotalPrice;

    //defines constructor for ProductAdapter
    public ProductAdapter(List<Product> productsList, TextView textView, EditText edtSearch) {
        this.edtSearch = edtSearch;
        this.productsList = productsList;
        this.tvTotalPrice = textView;
    }

    // Defines a custom ViewHolder class to display in RecyclerView
    public class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        // binds product to the view base on which activity the view is in
        public void bind(Product product, int position) {
            // Get the simple name of the Context class
            Context context = itemView.getContext();
            String contextName = context.getClass().getSimpleName();

            // determine which activity the view is in
            if (contextName.equals("MainActivity")) {
                bindForMainActivity(product);
            } else {
                bindForCartActivity(product);
            }
            System.out.println("The view is in: " + contextName);
        }

        // Bind product data to the view in MainActivity
        private void bindForMainActivity(Product product) {
            // find views for product description, price, cart item, and thumbnail image
            TextView mainDescription = itemView.findViewById(R.id.descriptionMain);
            TextView mainPrice = itemView.findViewById(R.id.priceMain);
            ImageView addBtn = itemView.findViewById(R.id.iconAddToCart);
            ImageView mainImg = itemView.findViewById(R.id.imgProductMain);

            renderImageUI(mainImg, product.getThumbnail());
            mainDescription.setText(product.getDescription());

            String formattedPrice = formatPrice(product.getPrice());
            mainPrice.setText(formattedPrice);

            // Handle click event "add-to-cart" and Toast
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductManager productManager = ProductManager.getInstance(itemView.getContext());
                    DbHelper dbHelper = new DbHelper(itemView.getContext());
                    dbHelper.increaseQuantity(product);
                    Toast.makeText(view.getContext(), "Added Successfully!", Toast.LENGTH_SHORT).show();
                    System.out.println("Added product to cart!");
                }
            });
        }

        // Bind product data to the view in CartActivity
        private void bindForCartActivity(Product product) {
            // find views for product description, price, image, cart buttons, quantity
            TextView cartDescription = itemView.findViewById(R.id.descriptionInCart);
            TextView cartPrice = itemView.findViewById(R.id.priceInCart);
            TextView sumPrice = itemView.findViewById(R.id.sumPriceTitle);
            ImageView cartImg = itemView.findViewById(R.id.imgInCart);
            ImageView btnIncrease = itemView.findViewById(R.id.btnIncrease);
            ImageView btnDecrease = itemView.findViewById(R.id.btnDecrease);
            TextView cartQuantity = itemView.findViewById(R.id.tvQuantity);

            renderImageUI(cartImg, product.getThumbnail());
            cartDescription.setText(product.getDescription());

            String formattedPrice = formatPrice(product.getPrice());
            cartPrice.setText(formattedPrice);

            int productQuantity = product.getQuantity();
            String textProductQuantity = String.valueOf(productQuantity);
            cartQuantity.setText(textProductQuantity);

            int sumPriceOfOneProduct = product.getQuantity() * product.getPrice();
            String formattedSumPrice = formatPrice(sumPriceOfOneProduct).replace("₫", "");
            sumPrice.setText(formattedSumPrice);
            updateTotalPrice();

            // handle click event for Increase quantity button
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbHelper dbHelper = new DbHelper(itemView.getContext());
                    dbHelper.increaseQuantity(product);
                    updateProductList();
                    System.out.println("Increased quantity!");
                }
            });

            // handle click event for Decrease quantity button
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductManager productManager = ProductManager.getInstance(itemView.getContext());
                    // if there's only one product in the cart
                    if (product.getQuantity() == 1) {
                        // show a confirmation dialog
                        new AlertDialog.Builder(itemView.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm!")
                                .setMessage("Are you sure to delete this product?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    //confirmed
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DbHelper dbHelper = new DbHelper(itemView.getContext());
                                        dbHelper.decreaseQuantity(product);
                                        updateProductList();
                                        Toast.makeText(view.getContext(), "Deleted the product!", Toast.LENGTH_SHORT).show();
                                        System.out.println("Deleted from cart!");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    // not want to delete product
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        System.out.println("Not want to delete!");
                                    }
                                })
                                .show();
                    } else { //more than 1 product in the cart
                        // decrease quantity of product in the database
                        DbHelper dbHelper = new DbHelper(itemView.getContext());
                        dbHelper.decreaseQuantity(product);
                        updateProductList();
                        System.out.println("Decreased quantity!");
                    }
                }
            });
        }

        // Render image of a product into UI
        private void renderImageUI(ImageView imageView, String imageUrl) {
            imageView.setTag(imageUrl);
            LoadImage imgProduct = new LoadImage();
            imgProduct.loadDataImage(imageView);
        }

        // Format price into Vietnamese Dong currency format
        @SuppressLint("DefaultLocale")
        private String formatPrice(int price) {
            return String.format("₫%,d", price).replace(",", ".");
        }

        // Calculate and update the total price
        private void updateTotalPrice() {
            int totalPrice = 0;
            for (Product product : productsList) {
                int sumPriceOfProduct = product.getPrice() * product.getQuantity();
                totalPrice += sumPriceOfProduct;
            }
            String formattedTotalPrice = formatPrice(totalPrice);
            tvTotalPrice.setText(formattedTotalPrice);
        }

        // Update the product list displayed in RecyclerView
        private void updateProductList() {
            Context context = itemView.getContext();
            ProductManager productManager = ProductManager.getInstance(context);
            productsList = productManager.all();
            List<Product> addedToList = new ArrayList<>();
            for (int i = 0; i < productsList.size(); i++) {
                if (productsList.get(i).getQuantity() > 0) {
                    addedToList.add(productsList.get(i));
                }
            }
            productsList = addedToList;
            updateTotalPrice();
            notifyDataSetChanged();
        }
    }

    // Load image data of product fom links got from API
    protected static class LoadImage {
        public void loadDataImage(ImageView imageView) {
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String urlImage = imageView.getTag().toString();
                        URL url = new URL(urlImage);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null) {
                            Constants.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("Downloaded images of products!");
        }
    }


    // Display on MainActivity or CartActivity base on layout Id in RecyclerView
    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = context instanceof MainActivity ? R.layout.product_in_main : R.layout.product_in_cart;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(layoutId, parent, false);
        return new ProductHolder(itemView);
    }


    //Set data in each item view based on its position in the list
    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = productsList.get(position);
        holder.bind(product, position);
    }

    // Get the total number of items in the list
    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
