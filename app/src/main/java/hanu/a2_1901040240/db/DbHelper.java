package hanu.a2_1901040240.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import hanu.a2_1901040240.models.Constants;
import hanu.a2_1901040240.models.Product;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    // Creates 'products' table with the given columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products(" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "thumbnail TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                " price INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL)");
    }

    // Drops the existing table and creates new one with updated schema
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE products");
        onCreate(db);

    }

    // Checks if 'products' table exists in the database
    public boolean isExistedTable() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor resultGetAll = db.rawQuery("SELECT * FROM products", null);

        boolean notEmptyRecord = resultGetAll.getCount() != 0;
        boolean notNullResult = resultGetAll != null;
        boolean isChecked = notEmptyRecord && notNullResult;
        if (isChecked)
            return true;
        return false;
    }

    // Adds a new product to the 'products' table in the database.
    public boolean add(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // put all values of product's info into ContentValues object
        contentValues.put("id", product.getId());
        contentValues.put("thumbnail", product.getThumbnail());
        contentValues.put("description", product.getDescription());
        contentValues.put("price", product.getPrice());
        contentValues.put("quantity", product.getQuantity());

        db.insert("products", null, contentValues);
        return true; //successfully
    }

    // Increase quantity of an existing product in the table
    public boolean increaseQuantity(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int idProduct = product.getId();
        Cursor resultFindById = db.rawQuery("select * from products where id='" + idProduct + "'", null);

        int indexQuantity = resultFindById.getColumnIndex("quantity");
        resultFindById.moveToNext();

        contentValues.put("id", product.getId());
        int quantity = resultFindById.getInt(indexQuantity);

        contentValues.put("quantity", quantity + 1);
        String idProductString = String.valueOf(idProduct);

        db.update("products", contentValues, "id = ?", new String[]{idProductString});
        return true; //successfully
    }

    // Decrease quantity of an existing product in the table
    public boolean decreaseQuantity(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String idProduct = String.valueOf(product.getId());
        contentValues.put("id", product.getId());
        contentValues.put("quantity", product.getQuantity() - 1);

        db.update("products", contentValues, "id = ?", new String[]{idProduct});
        return false; // false
    }

}
