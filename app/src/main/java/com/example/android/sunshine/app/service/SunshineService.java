package com.example.android.sunshine.app.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sunshine.app.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Viet on 11/19/2016.
 */
public class SunshineService extends IntentService {
    public static final String LOCATION_QUERY_EXTRA = "lqe";
    private final String LOG_TAG = SunshineService.class.getSimpleName();
    public SunshineService() {
        super("SunshineService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {}
    public static class AlarmReceiver extends BroadcastReceiver {
        public AlarmReceiver() {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intentService=new Intent(context,SunshineService.class);
            intentService.putExtra(LOCATION_QUERY_EXTRA,intent.getStringExtra(LOCATION_QUERY_EXTRA));
            context.startService(intentService);
        }
    }
}
