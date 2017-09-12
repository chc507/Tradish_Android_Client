package com.example.vanne.tradish_alpha.Menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanne.tradish_alpha.Adapters.UserDefinedListViewAdapter;
import com.example.vanne.tradish_alpha.DriverActivities.DeliveryOrderListActivity;
import com.example.vanne.tradish_alpha.LoginAndSignup.ProfileActivity;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.R;
import com.parse.ParseUser;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class DriverMenu extends AppCompatActivity {

    ListView myListView;
    String identity;
    TextView userIdentity;
    /*Generate options of array for resturant owners
        View Orders
        EditMenu
        Check Delivery
        Finance Options
    */
    ArrayList<String> deliveryMenu = new ArrayList<String>(asList("View Orders", "History"));
    ArrayList<String> description = new ArrayList<String>(asList("Check your order", "Track Delivery History"));
    ArrayList<Integer> iconid = new ArrayList<Integer>(asList(R.drawable.order_icon, R.drawable.delivery_icon));
    String DriverMenuTag = "InDriverMenu";
    ParseUser currentUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.driver_cust_rest_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.profile:
                Log.i(DriverMenuTag, "profile");
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.logoff:
                Log.i(DriverMenuTag, "logoff");
                ParseUser.getCurrentUser().logOut();
                onBackPressed();//Navigate back to main menu
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_menu);
        setTitle("Driver Menu");

        final Intent intent = getIntent();
        identity = intent.getStringExtra("Identity");
        userIdentity = (TextView)findViewById(R.id.userIdentity);
        userIdentity.setText("Log in as: " + identity);

        myListView = (ListView)findViewById(R.id.driverMenuEdit);
        UserDefinedListViewAdapter userDefinedListView = new UserDefinedListViewAdapter(this, deliveryMenu,description, iconid);
        myListView.setAdapter(userDefinedListView);
        /*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restMenu);
        */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                if(position == 0){
                    int driverId = 1;
                    Intent orderIntent = new Intent(getApplicationContext(), DeliveryOrderListActivity.class);
                    orderIntent.putExtra("DriverId", Integer.toString(driverId));
                    startActivity(orderIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "The selected option is " + deliveryMenu.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*Check if current user is available, is so, diplay the information */
        currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            Log.e(DriverMenuTag, "Getting current user " + currentUser.getUsername() + " " + currentUser.getSessionToken() + " " + currentUser.get("Identity"));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
