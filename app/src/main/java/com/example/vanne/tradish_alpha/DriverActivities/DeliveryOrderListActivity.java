package com.example.vanne.tradish_alpha.DriverActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vanne.tradish_alpha.Adapters.DriverAdapters.DeliveryListViewAdapter;
import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.VolleyNetwork.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;

public class DeliveryOrderListActivity extends AppCompatActivity {

    ListView myListView;
    String DriverId;
    JSONArray jsonArray;
    String deliveryId;

    boolean reqStatus = false ; // Success: true, Failure:  false

    /*Send a standard get request to the server with Volley
      requesting JSON array from the server
     */

    private static String JSON_ARRAY_REQUEST_URL = "http://34.208.189.14:8080/orders/getorderswithseq?key=";
    //private static final String JSON_ARRAY_REQUEST_URL = "http://10.0.2.2:5000/orders";

    private static final String TAG = "OrderListActivity";
    private ProgressDialog progressDialog;
    DeliveryOrderModel dummyDeliveryOrder = new DeliveryOrderModel(-1, 0.0, "1308 North 112th Plaza, Omaha", 1, -1, false);
    int SelectedPos = -1;
    ArrayList<DeliveryOrderModel> DeliveryOrderList= new ArrayList<DeliveryOrderModel>(asList(dummyDeliveryOrder));
    DeliveryListViewAdapter deliveryListViewAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.driver_delivery_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.refresh:
                Log.i("Refresh", "CHANGED it later");
                return true;
            case R.id.start:
                Log.i("Start", "Schedule sended to the driver");
                /*send out the rest id to get drivers*/
                Intent driverActivity = new Intent(getApplicationContext(), DriverActivity.class);
                driverActivity.putParcelableArrayListExtra("DeliveryOrderList", DeliveryOrderList);
                driverActivity.putExtra("DriverId", DriverId);
                startActivity(driverActivity);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_list);
        setTitle("DeliverY Order List");

            /*Refreshing the orders */

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        DriverId = intent.getStringExtra("DriverId");
        //Log.i("New Url", JSON_ARRAY_REQUEST_URL + RestId);
        final TextView resturantId = (TextView) findViewById(R.id.deliveryId);
        resturantId.setText("Log in as driver: " + DriverId);


        myListView = (ListView) findViewById(R.id.deliveryListEdit);
        deliveryListViewAdapter = new DeliveryListViewAdapter(this, DeliveryOrderList);
        myListView.setAdapter(deliveryListViewAdapter);
        myListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /*No handlers for this*/
        /*
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                RestaurantModel orderModel = DeliveryOrderList.get(position);
                if(orderModel.isSelected()){
                    orderModel.setSelected(false);
                }else{
                    orderModel.setSelected(true);
                }
                updateListView();
            }

        });
        */


        /*
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                int selected = 0;
                String message = "";
                SelectedPos.clear();

                for(int i = 0; i < RestOrderList.size(); i++){
                    if(RestOrderList.get(i).isSelected()){
                        selected ++;
                        SelectedPos.add(i);
                        message += "* Order: " + RestOrderList.get(i).getOrderIdView() + " Address: " + RestOrderList.get(i).getAddress() + "\n";
                    }
                }
                Log.i("mess", message);
                //Generate an alert
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryOrderListActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
                alertDialog.setTitle("Schedule Delivery");
                alertDialog.setMessage(message);
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("in yes button", Integer.toString(i));
                        //This might not be needed as we will use fragment instead of activity
                        ArrayList<RestOrderModel> selectedList = new ArrayList<RestOrderModel>();
                        for(int j = 0; j < SelectedPos.size(); j++){
                            //Log.i("SelectedPos", Integer.toString(SelectedPos.get(j)));
                            selectedList.add(RestOrderList.get(SelectedPos.get(j)));
                            selectedList.get(selectedList.size()-1).setRestId(Integer.parseInt(RestId));
                        }
                        Intent deliverIntent = new Intent(getApplicationContext(), DeliveryScheduleActivity.class);
                        deliverIntent.putParcelableArrayListExtra("orderList", selectedList);
                        deliverIntent.putExtra("RestId", RestId);
                        startActivity(deliverIntent);
                    }
                });
                alertDialog.setNeutralButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("in no button", Integer.toString(i));
                    }
                });

                alertDialog.show();

                return true;
            }
        });
        */

        /*Fetch data from server*/
        volleyJsonArrayRequest(JSON_ARRAY_REQUEST_URL + DriverId);
    }
    /*
        Overload the  onBackPress() so that it will
        dimiss the dialog when back button is pressed.
    */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        progressDialog.dismiss();
        finish();
    }


    public void updateListView(){
        deliveryListViewAdapter.updateOrders(DeliveryOrderList);
        Collections.sort(DeliveryOrderList);
    }

    public void clearListView(){
        DeliveryOrderList.clear();
    }

    public void parseOrderList(JSONArray orderList){
        for (int i = 0; i < orderList.length(); i++) {
            try {
                JSONObject jsonPart = orderList.getJSONObject(i);
                int Order_ID = jsonPart.getInt("Order_ID");
                double Total = jsonPart.getDouble("Total_Price");
                String Address = jsonPart.getString("Address");
                int Driver_ID = jsonPart.getInt("Driver_ID");
                //String Customer_Name = jsonPart.getString("Customer_Name");
                //int deliveryId = jsonPart.getInt("Delivery_ID");
                int Sequence = jsonPart.getInt("Sequence");
                int Status= jsonPart.getInt("Status");
                DeliveryOrderModel newDeliveryOrder = new DeliveryOrderModel(Order_ID, 100,Total, Address, Status,Sequence, false);
                DeliveryOrderList.add(newDeliveryOrder);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*All network stuff using Volley Library */
    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "VolleyJsonArrayRequest";
        Log.e(REQUEST_TAG, "my url is " + url);
        progressDialog.setMessage("Refreshing Orders...");
        progressDialog.show();


        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hide();
                        clearListView();
                        parseOrderList(response);
                        updateListView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, TAG + error.getMessage());
                Log.e(TAG, "Download failed, dont know why");
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }
}
