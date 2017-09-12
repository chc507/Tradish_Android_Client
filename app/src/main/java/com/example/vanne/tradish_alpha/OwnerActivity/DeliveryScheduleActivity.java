package com.example.vanne.tradish_alpha.OwnerActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vanne.tradish_alpha.Adapters.OwnerAdapters.ScheduledListViewAdapter;
import com.example.vanne.tradish_alpha.Adapters.OwnerAdapters.UnscheduledListViewAdapter;
import com.example.vanne.tradish_alpha.Fragments.MapFragment;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.VolleyNetwork.AppSingleton;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static android.content.ContentValues.TAG;

public class DeliveryScheduleActivity extends AppCompatActivity {
    String layoutIs = "map";
    String RestId;
    private ArrayList<RestOrderModel> selectedList;
    //private ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    private ArrayList<RestOrderModel> scheduledList = new ArrayList<RestOrderModel>();
    private ArrayList<RestOrderModel> unscheduledList = new ArrayList<RestOrderModel>();
    private ArrayList<String>driverList = new ArrayList<String>();
    private ListView unscheduledView;
    private ListView scheduledView;
    private ArrayAdapter<String> driverListAdapter;
    private UnscheduledListViewAdapter unscheduledListViewAdapter;
    private ScheduledListViewAdapter scheduledListViewAdapter;
    //Network urls
    private String POST_URL_DELIVERY = "http://34.208.189.14:8080/delivery/createdelivery";
    private String GET_URL_DRIVER = "http://34.208.189.14:8080/drivers/getdrivers?key=";
    private ProgressDialog progressDialog;
    //Driver list dialog
    ListView driverListView = null;
    String selectedDriverID = "";



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delivery_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.BacktoMain:
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.send:
                Log.i("Menu item selected", "Schedule sended to the driver");
                /*send out the rest id to get drivers ids.*/
                getRestDrivers();
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_schedule);
        setTitle("Delivery Schedule Activity");
        selectedList = getIntent().getParcelableArrayListExtra("orderList");
        RestId = getIntent().getStringExtra("RestId");

        /*Convert Address into LatLng and populate the unscheduled list*/
        for (int i = 0; i < selectedList.size(); i++) {
            /*populate unscheudled list*/
            unscheduledList.add(selectedList.get(i));
            LatLng latLng = getLocationFromAddress(this, selectedList.get(i).getAddress());
            if(latLng == null){
                selectedList.get(i).setLatLng(null);
            } else {
                selectedList.get(i).setLatLng(latLng);
            }
        }

        /*diaglog*/
        progressDialog = new ProgressDialog(this);
        //Do you need custom array Adapter?
        unscheduledView = (ListView)findViewById(R.id.unscheduled);
        scheduledView = (ListView)findViewById(R.id.scheduled);
        unscheduledListViewAdapter = new UnscheduledListViewAdapter(this,unscheduledList);
        unscheduledView.setAdapter(unscheduledListViewAdapter);
        scheduledListViewAdapter = new ScheduledListViewAdapter(this, scheduledList);
        scheduledView.setAdapter(scheduledListViewAdapter);

        //Handlers for unscheduled list
        unscheduledView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                scheduledList.add(unscheduledList.get(position));
                scheduledList.get(scheduledList.size()-1).setSequence(scheduledList.size());
                unscheduledList.remove(position);
                updateListView();
            }
        });


        unscheduledView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RestOrderModel target = unscheduledList.get(position);
                String message = target.getOrderIdView() + "\n"
                        + target.getAddress() + "\n"
                        + target.getTotalPriceView() + "\n";
                //Generate an alert
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryScheduleActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
                alertDialog.setTitle("Order Details");
                alertDialog.setMessage(message);
                alertDialog.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("in no button", Integer.toString(i));
                    }
                });

                alertDialog.show();
                return true;
            }
        });

        scheduledView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                Log.i("in scheduled, pos ", Integer.toString(position));
            }
        });

        scheduledView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                RestOrderModel target = scheduledList.get(position);
                String message = "ID " + target.getOrderIdView() + "\n"
                        + "Address:" + target.getAddress() + "\n"
                        + "Price($): " + target.getTotalPriceView() + "\n";
                //Generate an alert
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryScheduleActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
                alertDialog.setTitle("Rescheduling the item");
                alertDialog.setMessage(message);

                alertDialog.setNegativeButton("No, keep it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("in no button", Integer.toString(i));
                    }

                });

                alertDialog.setPositiveButton("Yes, reschedule", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unscheduledList.add(scheduledList.get(position));
                        unscheduledList.get(unscheduledList.size()-1).setSequence(-1);
                        scheduledList.remove(position);
                        updateListView();
                    }
                });
                alertDialog.setNeutralButton("Select Driver and Update Map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Fragment fragment;
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("selectedList", scheduledList);
                            fragment = new MapFragment();
                            fragment.setArguments(bundle);
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.MapFragment, fragment);
                            ft.commit();
                        }
                });
                alertDialog.show();
                return true;
            }
        });
    }

    public void updateListView(){
        unscheduledListViewAdapter.updateOrders(unscheduledList);
        scheduledListViewAdapter.updateOrders(scheduledList);
    }
    public void changeFragment(View view){
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("selectedList", selectedList);
        //bundle.putParcelableArrayList("latLngs",latLngs);
        Button button = (Button)findViewById(R.id.buttonx);
        if(layoutIs == "map") {
            fragment = new MapFragment();
            button.setText("Close Map");
            layoutIs = "blank";
        } else {
            fragment = new Fragment();
            button.setText("Displace Order On Map");
            layoutIs = "map";
        }


        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.MapFragment, fragment);
        ft.commit();
        Log.i("completed", "executed fragment transcation");
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.i("Address", strAddress);

        Geocoder coder = new Geocoder(context);
        Log.i("Geo",Boolean.toString(coder.isPresent()));
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null || address.size() == 0) {
                return null;
            }else {
                Log.i("So it is not null", Integer.toString(address.size()));
                android.location.Address location = address.get(0);
                Log.i("What is it", address.get(0).toString());

                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                Log.i("LatLng",Double.toString(lat) + Double.toString(lng) );
                p1 = new LatLng(lat, lng);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }
    public void sendOrdersToServer(String POST_URL){
        Log.i("Sending Orders", "fuck the server");
        final String ordersJsonArray = JsonArrayBuilder();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,POST_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String Response){
                        Log.i("Fuck yeah ","Post completed");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.i("Fuck no", "Post error");
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Orders", ordersJsonArray);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String JsonArrayBuilder(){
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

    /*All network stuff using Volley Library */

    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonArrayRequest";
        progressDialog.setMessage("Refreshing Orders...");
        progressDialog.show();

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("getJson", response.toString());
                        progressDialog.hide();
                        parseDriver(response);
                        //updateListView();
                        driverListAdapter.notifyDataSetChanged();
                        Log.i("driverlistsizeupdate",Integer.toString(driverList.size()));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }

    public void parseDriver(JSONArray driverJsonArray){
        for (int i = 0; i < driverJsonArray.length(); i++) {
            try {
                JSONObject jsonPart = driverJsonArray.getJSONObject(i);
                int driverId = jsonPart.getInt("Driver_ID");
                //String driverName = jsonPart.getString("Driver_Name");
                String driverName = jsonPart.getString("drivername");
                String driverContent = Integer.toString(driverId) +" - " + driverName;
                driverList.add(driverContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getRestDrivers(){
        Log.i("new url", GET_URL_DRIVER);
        driverList.clear();
        volleyJsonArrayRequest(GET_URL_DRIVER+RestId);
        Log.i("new url for get",GET_URL_DRIVER+RestId );
        driverListAdapter = new ArrayAdapter<String>(DeliveryScheduleActivity.this, android.R.layout.select_dialog_singlechoice, driverList);
        driverListView = new ListView(this);
        driverListView.setAdapter(driverListAdapter);
        driverListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        driverListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String driverContent = driverList.get(position);
                Log.i("driver content", driverContent);
                StringTokenizer st = new StringTokenizer(driverContent, " - ");
                selectedDriverID = st.nextToken();
                Log.i("driver id", selectedDriverID);
            }
        });


        final AlertDialog.Builder driverDialog = new AlertDialog.Builder(DeliveryScheduleActivity.this);

        driverDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
        driverDialog.setTitle("Select your drivers");

        driverDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Log.i("in no driver dialog", Integer.toString(i));
                dialogInterface.dismiss();
            }
        });


        driverDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //populate the driver ID to all the selected ID
                for(int j = 0; j < selectedList.size(); j++){
                    selectedList.get(j).setDriverId(Integer.parseInt(selectedDriverID));
                    selectedList.get(j).setStatusCode(1); //set status to be "Delivery in Progress;
                }
                sendOrdersToServer(POST_URL_DELIVERY);
                /*Navigate to previous activity*/
                Intent orderListIntent = new Intent(getApplicationContext(), OrderListActivity.class);
                orderListIntent.putExtra("RestId", RestId);
                orderListIntent.putExtra("ShowUpdateButton", true);
                startActivity(orderListIntent);

                dialogInterface.dismiss();
            }
        });

        Log.i("driverlistsize",Integer.toString(driverList.size()));
        driverDialog.setView(driverListView);

        AlertDialog dialog = driverDialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        progressDialog.dismiss();
        finish();
    }
}
