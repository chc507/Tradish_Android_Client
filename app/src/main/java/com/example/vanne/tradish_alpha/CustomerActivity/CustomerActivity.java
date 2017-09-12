package com.example.vanne.tradish_alpha.CustomerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vanne.tradish_alpha.Adapters.CustomerAdapter.MenuListAdapter;
import com.example.vanne.tradish_alpha.CustomerActivity.MenuActivities.MenusDetailActivity;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.Menus.CustomerMenu;
import com.example.vanne.tradish_alpha.Models.Menus;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.VolleyNetwork.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {


    private ListView listViewMenus;
    private List<Menus> menus;
    private MenuListAdapter menuListAdapter;
    private ProgressDialog progressDialog;
    String CustTag = "In Cust Activity";
    String GET_URL = "http://34.208.189.14:8080/menus/getmenus?key=1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menus);
        setTitle("Customer Activity");

        progressDialog = new ProgressDialog(this);
        menus = new ArrayList<Menus>();
        menus.add(new Menus("m01", "No Dish Available", "No Catagory Avaialble",  "No Taste Available",  0.01, R.drawable.question_sign_icon));

        menuListAdapter = new MenuListAdapter(this, menus);
        listViewMenus = (ListView) findViewById(R.id.listViewMenus);
        listViewMenus.setAdapter(menuListAdapter);
        listViewMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Menus menu = menus.get(i);
                Intent intent = new Intent(CustomerActivity.this, MenusDetailActivity.class);
                intent.putExtra("Menus", menu);
                startActivity(intent);
            }
        });
        volleyJsonArrayRequest(GET_URL);
    }


    public void parseOrderList(JSONArray orderList) {
        for (int i = 0; i < orderList.length(); i++) {
            try {
                Menus newDish = null;
                JSONObject jsonPart = orderList.getJSONObject(i);
                String Order_ID = jsonPart.getString("Dish_ID");
                String Order_Name = jsonPart.getString("dishname");
                String Order_Note = jsonPart.getString("note");
                String Order_Category = jsonPart.getString("category");
                double Price = jsonPart.getDouble("price");

                //JSONObject picObject = jsonPart.getJSONObject("pic");
                if(!jsonPart.get("pic").equals(null)) {
                    JSONObject picObject = jsonPart.getJSONObject("pic");
                    if (picObject != null) {
                        try {
                            JSONArray dataJsonArray = picObject.getJSONArray("data");
                            Log.e(CustTag, " size is " + Integer.toString(dataJsonArray.length()));
                            byte[] ImageStream = new byte[dataJsonArray.length()];

                            for(int j = 0; j < dataJsonArray.length(); j++){
                                ImageStream[j] = (byte)Integer.parseInt(dataJsonArray.get(j).toString());
                            }

                            newDish = new Menus(Order_ID, Order_Name, Order_Category, Order_Note, Price, ImageStream);
                            newDish.setFlag(1);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } else {
                    newDish = new Menus(Order_ID, Order_Name, Order_Category, Order_Note, Price, R.drawable.question_sign_icon);
                }

                menus.add(newDish);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*All network stuff using Volley Library */
    public void volleyJsonArrayRequest(String url) {
        final String REQUEST_TAG = "In Cust Activity, Getting Array";
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
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }

    public void updateListView() {
        menuListAdapter.updateMenu(menus);
        //Collections.sort(DeliveryOrderList);
    }

    public void clearListView() {
        menus.clear();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this,CustomerMenu.class);
        startActivity(intent);

    }
}

