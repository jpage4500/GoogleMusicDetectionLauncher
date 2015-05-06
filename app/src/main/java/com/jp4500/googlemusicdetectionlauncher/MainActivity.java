package com.jp4500.googlemusicdetectionlauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    public static final String TAG = "GML";
    public static final int NETWORK_TIMEOUT_MS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: waiting for network connection (up to " + NETWORK_TIMEOUT_MS + "ms)");
        new Thread(new NetworkMonitor()).start();
    }

    private class NetworkMonitor implements Runnable {
        public void run() {
            long startTimeMs = System.currentTimeMillis();
            while (true) {
                try {
                    boolean isConnected = isNetworkConnected();
                    if (isConnected) {
                        runGoogleNow();
                        break;
                    }

                    long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
                    if (elapsedTimeMs >= NETWORK_TIMEOUT_MS) {
                        Log.e(TAG, "NetworkMonitor: timeout!!!");
                        runGoogleNow();
                        break;
                    }

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "NetworkMonitor: InterruptedException!", e);
                }
            }

            finish();
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //Log.d(TAG, "activeNetworkInfo: " + activeNetworkInfo);
        return activeNetworkInfo != null;
    }

    private void runGoogleNow() {
        Log.d(TAG, "runGoogleNow");
        try {
            // start google now in music detection mode
            startActivity(new Intent("com.google.android.googlequicksearchbox.MUSIC_SEARCH").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            Log.e(TAG, "runGoogleNow: InterruptedException!", e);
        }
    }

}