package com.example.vanne.tradish_alpha.CustomerActivity.ResturantList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vanne.tradish_alpha.Adapters.CustomerAdapter.RestListAdapter;
import com.example.vanne.tradish_alpha.CustomerActivity.CustomerActivity;
import com.example.vanne.tradish_alpha.Models.RestaurantModel;
import com.example.vanne.tradish_alpha.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private ListView restListView;
    private ArrayList<RestaurantModel> restaurantModels;
    private RestListAdapter restListAdapter;
    private ProgressDialog progressDialog;
    private String RestListTag = "InRestListActivity";
    private RestaurantModel restaurantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        setTitle("Restaurant List Activity");

        progressDialog = new ProgressDialog(this);

        restaurantModels = new ArrayList<>();
        restaurantModels.add(new RestaurantModel("0", "Dummy Restaurant", 0));
        restListView = (ListView) findViewById(R.id.restListView);
        restListAdapter = new RestListAdapter(this, restaurantModels);
        restListView.setAdapter(restListAdapter);
        restListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RestaurantModel restaurantModel = restaurantModels.get(i);
                //Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
                Intent intent = new Intent(RestaurantListActivity.this, CustomerActivity.class);
                intent.putExtra("restaurantModel", restaurantModel);
                startActivity(intent);
            }
        });


        /*Assign user id by making query of identities and putting the largest integer for latest user*/
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Identity", "Restaurant");
        query.orderByDescending("UserID" );
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects != null){
                    Log.i("In gettting list", Integer.toString(objects.size()));
                    clearListView();
                    if(objects.size() > 0){
                        for(int i = 0; i < objects.size(); i++){
                            final String name = objects.get(i).get("Nickname").toString();
                            final String address = objects.get(i).get("Identity").toString();
                            final int restId = (int)objects.get(i).get("UserID");
                            ParseFile file = (ParseFile)objects.get(i).get("Image");
                            if(file != null) {
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null) {
                                            restaurantModel = new RestaurantModel(name,address, restId,data);
                                            restaurantModel.setFlag(1);
                                            restaurantModels.add(restaurantModel);
                                            updateListView();
                                        } else {
                                            restaurantModel = new RestaurantModel(name,address, restId);
                                            restaurantModels.add(restaurantModel);
                                            updateListView();
                                        }
                                    }
                                });
                            }
                        }
                        updateListView();
                        Log.i("In gettting list", Integer.toString(restaurantModels.size()));
                    }else if(objects.size() == 0){
                        Toast.makeText(getApplicationContext(), "No Restaurants in the system!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    public void updateListView(){
        restListAdapter.updateList(restaurantModels);
    }


    public void clearListView(){
        restaurantModels.clear();
    }}
