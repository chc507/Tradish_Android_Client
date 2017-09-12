package com.example.vanne.tradish_alpha.Service;

import android.app.AlertDialog;
import android.app.Service;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by vanne on 8/15/2017.
 */

public class LocationService extends Service
{
    private static final String TAG = "GPSTEST";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL =  5000;//on sec ?
    private static final float LOCATION_DISTANCE = 10f;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    final String LOCATION_POST_URL = "http://34.208.189.14:8080/orders/postloc";
    //private LocationRequest mLocationRequest = new LocationRequest();



    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            //Send my last location to server
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();
            DateFormat df = new SimpleDateFormat("h:mm a");
            String date = df.format(Calendar.getInstance().getTime());
            String jsonObjectLocation = "{" +
                    "\"lat\":" + lat + "," +
                    "\"lng\":" + lng + "," +
                    "\"time\":" + date +
                    "}";
            sendLocationsToServer(LOCATION_POST_URL, jsonObjectLocation);
        }
        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {

        Log.e(TAG, "onCreate");


        initializeLocationManager();
        /*Debugging from GPS*/
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d("debug", Boolean.toString(isGPSEnabled) + Boolean.toString(isNetworkEnabled));

        //Try to get location via GPS module
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

    }
    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    //dummy, will be configured later
    public void sendLocationsToServer(String POST_URL, String message){
        Log.e(TAG, "Send location to server");

        final String locationJson = message;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,POST_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String Response){
                        Log.e(TAG,"Post completed");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e(TAG, "Post error");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //String locationMessage = "(" + lat  + ","+lng+")";
                params.put("Location", locationJson);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
    public String JsonObjectBuilder(){

        String jsonArrayMessage = "[";
        for(int i = 0; i < scheduledList.size(); i++){
            jsonArrayMessage += "{" +
                    "\"Order_ID\":" + scheduledList.get(i).getOrderIdView() + "," +
                    "\"Driver_ID\":" + scheduledList.get(i).getDriverId() + "," +
                    "\"Sequence\":" + scheduledList.get(i).getSequence() +
                    "}";
            if(i != scheduledList.size() - 1 ) jsonArrayMessage += ",";
        }
        jsonArrayMessage += "]";
        Log.i("jsonArrayMessage", jsonArrayMessage);
        return jsonArrayMessage;

    }
    */

}
