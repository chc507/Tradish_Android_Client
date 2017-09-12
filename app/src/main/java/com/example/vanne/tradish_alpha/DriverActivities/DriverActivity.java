package com.example.vanne.tradish_alpha.DriverActivities;

import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ProgressDialog;
        import android.content.BroadcastReceiver;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.ServiceConnection;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.IBinder;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.DefaultRetryPolicy;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.RetryPolicy;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.android.volley.TimeoutError;
        import com.example.vanne.tradish_alpha.Adapters.DriverAdapters.CompletedListViewAdapter;
        import com.example.vanne.tradish_alpha.Adapters.DriverAdapters.IncompletedListViewAdapter;
        import com.example.vanne.tradish_alpha.Fragments.driverMapFragment;
        import com.example.vanne.tradish_alpha.MainActivity;
        import com.example.vanne.tradish_alpha.Manifest;
        import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
        import com.example.vanne.tradish_alpha.R;
        import com.example.vanne.tradish_alpha.Service.DummyLocationService;
        import com.example.vanne.tradish_alpha.Service.GetLocationService;
        import com.google.android.gms.maps.model.LatLng;

        import org.json.JSONArray;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class DriverActivity extends AppCompatActivity implements ServiceConnection{
    /*Buttons for showing and getting the locations*/
    Button btnGetLocation;
    Button btnShowLocation;

    /*All the services */
    GetLocationService getLocationService;
    boolean isSeriviceStarted;
    DummyLocationService dummyLocationService;
    LatLng currentLoc;

    /*Lat Lng View*/
    TextView latLngTextView;

    /*Delivery_ID*/
    int DeliveryId;

    /*Driver_ID*/
    String DriverId;

    static MyReceiver myReceiver;
    IntentFilter filter;
    /*The delivery list from the previous intent*/
    private ArrayList<DeliveryOrderModel> incompletedList;
    private ArrayList<DeliveryOrderModel> completedList;
    /*List views for incompleted and completed lists*/
    private ListView incompletedView;
    private ListView completedView;


    /*Adapters for incompleted and completed lists*/
    private IncompletedListViewAdapter incompletedListAdapter;
    private CompletedListViewAdapter completedListAdapter;
    /*Select which layout the drivers want */
    String layoutIs = "map";
    /*Dialog*/
    private ProgressDialog progressDialog;
    /*URL for getting orders*/
    //private static String JSON_ARRAY_REQUEST_URL = "http://34.208.189.14:8080/orders/getorders?key=";
    private static String JSON_POST_REQUEST_URL = "http://34.208.189.14:8080/delivery/postloc";
    private static String Volley_Put_Request_URL = "";
    /*Debug Tag*/
    private static String DriverActivityTag = "In Driver Activity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tracking_driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.updateStatus:
                /*a put request*/
                volleyPutRequest(Volley_Put_Request_URL);
                return true;
            case R.id.BacktoMain:
                /*send out the rest id to get drivers*/
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        setTitle("Driver Activity");

        completedList = new ArrayList<DeliveryOrderModel>();
        incompletedList = getIntent().getParcelableArrayListExtra("DeliveryOrderList");
        DriverId = getIntent().getStringExtra("DriverId");
        Log.i("sizing for incompleted", Integer.toString(incompletedList.size()));

        /*Convert Address into LatLng and populate the unscheduled list*/
        for (int i = 0; i < incompletedList.size(); i++) {
            /*populate the latlng*/
            LatLng latLng = getLocationFromAddress(this, incompletedList.get(i).getAddress());
            if(latLng == null){
                Log.i("latlng", "nothing");
            } else {
                incompletedList.get(i).setLatLng(latLng);
            }
        }

        /*Obtain Delivery ID*/
        DeliveryId = incompletedList.get(0).getDeliveryId();

        myReceiver = new MyReceiver();
        // Set When broadcast event will fire.
        filter = new IntentFilter("Location_Intent_Broadcast");
        // Register new broadcast receiver
        this.registerReceiver(myReceiver, filter);


        /*diaglog*/
        progressDialog = new ProgressDialog(this);
        //Do you need custom array Adpter?
        incompletedView = (ListView)findViewById(R.id.incompleted);
        completedView = (ListView)findViewById(R.id.completed);
        latLngTextView = (TextView)findViewById(R.id.latlng);
        incompletedListAdapter = new IncompletedListViewAdapter(this, incompletedList);
        completedListAdapter = new CompletedListViewAdapter(this, completedList);
        incompletedView.setAdapter(incompletedListAdapter);
        completedView.setAdapter(completedListAdapter);

        //Handlers for unscheduled list
        incompletedView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                completedList.add(incompletedList.get(position));
                incompletedList.remove(position);
                updateListView();
            }
        });

        completedView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?>parent, View view, int position, long id){
                incompletedList.add(completedList.get(position));
                completedList.remove(position);
                updateListView();
                return true;
            }
        });

        //A map fragment is needed as well.
        isSeriviceStarted = false;
        btnGetLocation = (Button) findViewById(R.id.get_location);
        btnGetLocation.setText("Start Service");
        btnShowLocation = (Button) findViewById(R.id.show_location);
    }

    public void changeFragment(View view){
        Fragment fragment;
        //fragment = new DeliveryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("incompletedList", incompletedList);
        if(layoutIs == "map") {
            fragment = new driverMapFragment();
            btnShowLocation.setText("Map Display Off");
            layoutIs = "blank";
        } else {
            fragment = new Fragment();
            btnShowLocation.setText("Map Display On");
            layoutIs = "map";
        }
        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.MapFragment, fragment);
        ft.commit();
        Log.i("completed", "executed fragment transcation");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = new Intent(this, DummyLocationService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        DummyLocationService.LocationBinder b = (DummyLocationService.LocationBinder) binder;
        dummyLocationService = b.getService();
        Toast.makeText(DriverActivity.this, "Location service is online", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        dummyLocationService = null;
    }


    public void onClick(View view){
        if (Build.VERSION.SDK_INT < 23) {
            Intent LocationService = new Intent(getApplicationContext(), dummyLocationService.getClass());
            if(!isSeriviceStarted) {
                this.registerReceiver(myReceiver, filter);
                dummyLocationService.startService();
                getApplicationContext().startService(LocationService);
                btnGetLocation.setText("End Service");
                isSeriviceStarted  = true;
            }else{
                dummyLocationService.stopService();
                dummyLocationService.onDestroy();
                this.unregisterReceiver(myReceiver);
                Log.i("INFO", "service destroy");
                btnGetLocation.setText("Start Service");
                isSeriviceStarted = false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                // we have permission!
                myReceiver = new MyReceiver();
                Intent LocationService = new Intent(getApplicationContext(), dummyLocationService.getClass());
                if(!isSeriviceStarted) {
                    dummyLocationService.startService();
                    getApplicationContext().startService(LocationService);
                    btnGetLocation.setText("End Service");
                    isSeriviceStarted  = true;
                }else{
                    dummyLocationService.stopService();
                    dummyLocationService.onDestroy();
                    Log.i("INFO", "service destroy");
                    btnGetLocation.setText("Start Service");
                    isSeriviceStarted = false;
                }
            }
        }
    }

    public void updateListView(){
        incompletedListAdapter.updateOrders(incompletedList);
        completedListAdapter.updateOrders(completedList);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.i("Address", strAddress);

        Geocoder coder = new Geocoder(context);
        Log.i("Geo",Boolean.toString(coder.isPresent()));
        List<Address> address;
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

    /* Sending locations to server using Volley*/
    public void sendLocationsToServer(String POST_URL, String message){
        final String locationJson = message;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,POST_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String Response){
                        Log.e(DriverActivityTag,"Post completed");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e(DriverActivityTag, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Location", locationJson);
                return params;
            }
        };

        /*
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
            30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        */

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public class MyReceiver extends BroadcastReceiver{

        public MyReceiver(){/*Empty constructor*/}
        @Override
        public void onReceive(Context context, Intent intent){
            currentLoc = intent.getParcelableExtra("LatLng");
            latLngTextView.setText(currentLoc.toString());
            //Get estimate time of arrival from server as well.
            double lat = currentLoc.latitude;
            double lng = currentLoc.longitude;

            Log.e(DriverActivityTag, JSON_POST_REQUEST_URL);
            String locationJSONObject = "{"+"\"delivery_ID\":" + DeliveryId +","
                    + "\"Lat\":" + lat + ","
                    + "\"Lng\":" + lng + "}";
            Log.e(DriverActivityTag,locationJSONObject);
            sendLocationsToServer(JSON_POST_REQUEST_URL,locationJSONObject);
        }
    }

    public void volleyPutRequest(String url){
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(getApplicationContext(), "Status update successful", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(DriverActivityTag, "In Put Request: " + error.toString());
                        Toast.makeText(getApplicationContext(), "Fail to update status, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String> ();
                String jsonStatus = statusJsonBuilder();
                Log.e("status update", jsonStatus);
                params.put("delivery_update" + DeliveryId, jsonStatus);//Incompleted, Completed, Delivery in progress
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(putRequest);
    }

    public String statusJsonBuilder(){
        String jsonStatus = "[";
        /*Build up the status update for completed list*/
        jsonStatus += "{" + "\"completed\":" + "[";
        for(int i = 0; i < completedList.size(); i++){
            jsonStatus += "{" +
                    "\"order_ID\":" + completedList.get(i).getOrderIdView() + "," +
                    "\"status\":" + completedList.get(i).getStatusCode()  +
                    "}";
            if(i != completedList.size() - 1 ) jsonStatus += ",";
        }
        jsonStatus += "]" + "}" + ",";
        jsonStatus += "{" + "\"incompleted\":" + "[";
        /*Builder up the status update for incompleted list*/
        for(int i = 0; i < incompletedList.size(); i++){
            jsonStatus += "{" +
                    "\"order_ID\":" + incompletedList.get(i).getOrderIdView() + "," +
                    "\"status\":" + incompletedList.get(i).getStatusCode() +
                    "}";
            if(i != incompletedList.size() - 1 ) jsonStatus += ",";
        }
        jsonStatus += "]" + "}";
        return jsonStatus;
    }
}
