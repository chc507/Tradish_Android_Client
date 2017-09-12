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

import com.example.vanne.tradish_alpha.CustomerActivity.CustomerActivity;
import com.example.vanne.tradish_alpha.CustomerActivity.ResturantList.RestaurantListActivity;
import com.example.vanne.tradish_alpha.CustomerActivity.Tracking.TrackingActivity;
import com.example.vanne.tradish_alpha.LoginAndSignup.LoginSignupActivity;
import com.example.vanne.tradish_alpha.LoginAndSignup.ProfileActivity;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.OwnerActivity.OrderListActivity;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.Adapters.UserDefinedListViewAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class CustomerMenu extends AppCompatActivity {


    ListView myListView;
    TextView userIdentity;
    String identity;
    /*Generate options of array for customers

    */
    ArrayList<String> custMenu = new ArrayList<String>(asList("Make Orders", "Track Delivery", "Order History"));
    ArrayList<String> description = new ArrayList<String>(asList("Order Your Food", "Find where you food is", "Check your history"));
    ArrayList<Integer> iconid = new ArrayList<Integer>(asList(R.drawable.food_icon,R.drawable.route_icon,R.drawable.history_icon));
    String CustMenuTag = "InCustMenu";

    /*Cust Id should be fetched using Parse user */

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
                Log.i(CustMenuTag, "profile");
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.logoff:
                Log.i(CustMenuTag, "logoff");
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
        setContentView(R.layout.activity_customer_menu);
        setTitle("Customer Menu");


        final Intent intent = getIntent();
        identity = intent.getStringExtra("Identity");
        userIdentity = (TextView)findViewById(R.id.userIdentity);
        userIdentity.setText("Log in as: " + identity);


        myListView = (ListView)findViewById(R.id.custMenuEdit);
        UserDefinedListViewAdapter userDefinedListView = new UserDefinedListViewAdapter(this, custMenu,description, iconid);
        myListView.setAdapter(userDefinedListView);
        /*
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restMenu);
        */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                if(position == 0){
                    int custId = 1;
                    /*Naviggate to an activity that contains a list of restaurants*/
                    Intent restListIntent = new Intent(getApplicationContext(), RestaurantListActivity.class);
                    restListIntent.putExtra("CustId", Integer.toString(custId));
                    startActivity(restListIntent);
                }else if(position == 1){
                    int orderId = 123;
                    Intent trackingIntent = new Intent(getApplicationContext(), TrackingActivity.class);
                    trackingIntent.putExtra("OrderId", Integer.toString(orderId));
                    startActivity(trackingIntent);
                }else{
                    /*This should navigate to order history*/
                    Toast.makeText(getApplicationContext(), "The selected option is " + custMenu.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}