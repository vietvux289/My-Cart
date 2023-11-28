package hanu.a2_1901040240.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040240.models.Product;

public class ProductManager {
    private static ProductManager instance;
    private SQLiteDatabase db;

    private ProductManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Returns the instance of ProductManager
    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    // Returns a list of all products in db
    public List<Product> all() {
        List<Product> listOfProducts = new ArrayList<>();
        Cursor resultGetAll = db.rawQuery("SELECT * FROM products", null);
        listOfProducts = getProducts(resultGetAll);
        return listOfProducts;
    }

    // Returns a product based on the provided cursor
    public Product getOneProduct(Cursor cursor) {
        // get the column indexes for the product fields
        int indexId = cursor.getColumnIndex("id");
        int indexThumbnail = cursor.getColumnIndex("thumbnail");
        int indexOfDescriptionItem = cursor.getColumnIndex("description");
        int indexPrice = cursor.getColumnIndex("price");
        int indexQuantity = cursor.getColumnIndex("quantity");

        cursor.moveToNext();
        int id = cursor.getInt(indexId);
        String thumbnail = cursor.getString(indexThumbnail);
        int price = cursor.getInt(indexPrice);
        int quantity = cursor.getInt(indexQuantity);
        String descriptionItem = cursor.getString(indexOfDescriptionItem);

        return new Product(id, thumbnail, descriptionItem, price, quantity);
    }

    // Returns a list of products based on the provided cursor
    public List<Product> getProducts(Cursor cursor) {
        List<Product> productsList = new ArrayList<>();
        while (!cursor.isLast()) {
            productsList.add(getOneProduct(cursor));
        }
        return productsList;
    }

    // Adds a new product to the database
    public boolean addProduct(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", product.getId());
        contentValues.put("thumbnail", product.getThumbnail());
        contentValues.put("description", product.getDescription());
        contentValues.put("price", product.getPrice());
        contentValues.put("quantity", product.getQuantity());

        db.insert("products", null, contentValues);
        System.out.println("Added product to the DB!");
        return true;
    }
}

