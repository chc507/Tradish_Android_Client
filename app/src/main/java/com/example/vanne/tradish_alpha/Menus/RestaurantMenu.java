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

import com.example.vanne.tradish_alpha.LoginAndSignup.ProfileActivity;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.OwnerActivity.OrderListActivity;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.Adapters.UserDefinedListViewAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class RestaurantMenu extends AppCompatActivity {

    ListView myListView;
    String identity;
    /*Generate options of array for resturant owners
        View Orders
        EditMenu
        Check Delivery
        Finance Options
    */
    ArrayList<String> restMenu = new ArrayList<String>(asList("View Orders", "Edit Menu", "Checkbook"));
    ArrayList<String> description = new ArrayList<String>(asList("Check and modify your order", "Change your menu", "Check money flow"));
    ArrayList<Integer> iconid = new ArrayList<Integer>(asList(R.drawable.order_icon, R.drawable.menu_icon,R.drawable.finance_icon));
    String RestMenuTag = "InRestMenu";

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
                Log.i(RestMenuTag, "profile");
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.logoff:
                Log.i(RestMenuTag, "logoff");
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
        setContentView(R.layout.activity_resturant_menu);
        setTitle("Owner Menu");

        final Intent intent = getIntent();
        identity = intent.getStringExtra("Identity");
        TextView userIdentity = (TextView)findViewById(R.id.userIdentity);
        userIdentity.setText("Log in as: " + identity);


        myListView = (ListView)findViewById(R.id.restMenuEdit);
        UserDefinedListViewAdapter userDefinedListView = new UserDefinedListViewAdapter(this, restMenu,description, iconid);
        myListView.setAdapter(userDefinedListView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                if(position == 0){
                    int restId = 1;
                    Intent orderListIntent = new Intent(getApplicationContext(), OrderListActivity.class);
                    orderListIntent.putExtra("RestId", Integer.toString(restId));
                    orderListIntent.putExtra("ShowUpdateButton", false);
                    startActivity(orderListIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "The selected option is " + restMenu.get(position), Toast.LENGTH_SHORT).show();
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
