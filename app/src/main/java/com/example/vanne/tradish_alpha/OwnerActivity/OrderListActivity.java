package com.example.vanne.tradish_alpha.OwnerActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vanne.tradish_alpha.Adapters.OwnerAdapters.OrderListViewAdapter;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.VolleyNetwork.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class OrderListActivity extends AppCompatActivity {


    ListView myListView;
    Button updateButton;
    String RestId;
    Boolean ShowUpdateButton;





    /*Send a standard get request to the server with Volley
      requesting JSON array from the server
     */
    //private static final String JSON_ARRAY_REQUEST_URL = "http://10.0.2.2:3000/Orders";
    private static String JSON_ARRAY_REQUEST_URL = "http://34.208.189.14:8080/orders/getorders?key=";
    private static String Status_String_Request_URL = "http://34.208.189.14:8080/orders/delivered?key=1";

    private static final String TAG = "OrderListActivity";
    private ProgressDialog progressDialog;
    RestOrderModel dummyRestOrder = new RestOrderModel(-1, 0.0, "No Order Yet", 0, 0, false);
    ArrayList<RestOrderModel> RestOrderList = new ArrayList<RestOrderModel>(asList(dummyRestOrder));
    ArrayList<Integer>SelectedPos = new ArrayList<Integer>();
    OrderListViewAdapter orderListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_orderlist);
            setTitle("Order List Activity");

            /*Refreshing the orders */
            progressDialog = new ProgressDialog(this);
            /*Obtain parameters via intents*/
            Intent intent = getIntent();
            RestId = intent.getStringExtra("RestId");

            /*Set up visibility of the update button*/
            ShowUpdateButton = intent.getBooleanExtra("ShowUpdateButton", false);
            updateButton = (Button) findViewById(R.id.updateButton);
            if(ShowUpdateButton){
                updateButton.setVisibility(View.VISIBLE);
            }else{
                updateButton.setVisibility(View.INVISIBLE);
            }
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*get request from server to determine if the status needs to change*/
                    volleyJsonStringRequest(Status_String_Request_URL);
                }
            });


            Log.i("New Url", JSON_ARRAY_REQUEST_URL + RestId);
            final TextView resturantId = (TextView) findViewById(R.id.resturantId);
            resturantId.setText("Log in as resturant: " + RestId);


            myListView = (ListView) findViewById(R.id.deliveryListEdit);
            orderListViewAdapter = new OrderListViewAdapter(this, RestOrderList);
            myListView.setAdapter(orderListViewAdapter);
            //myListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


            /* Handlers for
            */
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    RestOrderModel orderModel = RestOrderList.get(position);
                    if(orderModel.isSelected()){
                        orderModel.setSelected(false);
                    }else{
                        orderModel.setSelected(true);
                    }
                    updateListView();
                }

            });
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
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderListActivity.this);
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
            /*Fetch data from server*/
            volleyJsonArrayRequest(JSON_ARRAY_REQUEST_URL + RestId);
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
        orderListViewAdapter.updateOrders(RestOrderList);
    }


    public void clearListView(){
        RestOrderList.clear();
    }

    public void parseOrderList(JSONArray orderList){
        for (int i = 0; i < orderList.length(); i++) {
            try {
                JSONObject jsonPart = orderList.getJSONObject(i);
                int Order_ID = jsonPart.getInt("Order_ID");
                double Total = jsonPart.getDouble("Total_Price");
                String Address = jsonPart.getString("Address");
                int Driver_ID = jsonPart.getInt("Driver_ID");
                int Customer_ID = jsonPart.getInt("Customer_ID");
                int Status = jsonPart.getInt("Status");
                int OrderType = jsonPart.getInt("OrderType");
                RestOrderModel newRestOrder = new RestOrderModel(Order_ID, Total, Address, OrderType, Status, false);
                RestOrderList.add(newRestOrder);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*All network stuff using Volley Library */
    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "VolleyJsonArrayRequest";
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }

    public void volleyJsonStringRequest(String url){
        String  REQUEST_TAG = "VolleyJsonArrayRequest";
        progressDialog.setMessage("Refreshing Orders...");
        progressDialog.show();

        StringRequest StringReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.i("Order list req", response);
                        for(int i = 0; i < RestOrderList.size(); i++){
                            RestOrderList.get(i).setStatusCode(Integer.parseInt(response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(StringReq, REQUEST_TAG);
    }
}
