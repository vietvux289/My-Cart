package hanu.a2_1901040240.models;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    // name of the database
    public static final String DB_NAME = "PRODUCTS.DB";

    // version of the database
    public static final int DB_VERSION = 1;

    // thread pool with 4 threads
    public static final ExecutorService executor = Executors.newFixedThreadPool(4);

    // handler to enqueue an action to be performed on different thread by Looper
    public static final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
}
