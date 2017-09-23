package com.example.vanne.tradish_alpha.CustomerActivity.Tracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vanne.tradish_alpha.Applications.StarterApplication;
import com.example.vanne.tradish_alpha.LoginAndSignup.ProfileActivity;
import com.example.vanne.tradish_alpha.MainActivity;
import com.example.vanne.tradish_alpha.Menus.CustomerMenu;
import com.example.vanne.tradish_alpha.Models.Cart;
import com.example.vanne.tradish_alpha.Models.Menus;
import com.example.vanne.tradish_alpha.Models.Product;
import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.VolleyNetwork.AppSingleton;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class TrackingActivity extends AppCompatActivity {
    String OrderID;
    ProgressDialog progressDialog;
    TextView etaTextView;
    String GET_URL="http://34.208.189.14:8080/orders/gettime?key=1";
    String TrackingTag = "In Tracking Activity";
    Cart cart;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.track_delivery_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.mainMenu:
                Intent custIntent = new Intent(getApplicationContext(), CustomerMenu.class);
                startActivity(custIntent);
                return true;
            case R.id.logoff:
                ParseUser.getCurrentUser().logOut();
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
        setContentView(R.layout.activity_tracking);
        setTitle("Tracking Activity");

        OrderID = getIntent().getStringExtra("OrderID");
        progressDialog = new ProgressDialog(this);
        etaTextView = (TextView)findViewById(R.id.etaTextView);

        cart = StarterApplication.getShoppingCart();
        Log.e("Trackign cart size", Integer.toString(cart.getSize()));


        /*Getting the cart view*/
        LinearLayout cartLayout = (LinearLayout) findViewById(R.id.cart);
        TextView totalPriceView = (TextView) findViewById(R.id.totalPriceView);

        Set<Product> products = cart.getProducts();

        Iterator iterator = products.iterator();
        while(iterator.hasNext()) {
            Product product = (Product) iterator.next();

            // logic
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView name = new TextView(this);
            TextView quantity = new TextView(this);
            TextView price = new TextView(this);

            name.setText(product.getName());
            quantity.setText(Integer.toString(cart.getQuantity(product)));
            price.setText(Double.toString(product.getValue()));

            linearLayout.addView(name);
            linearLayout.addView(price);
            linearLayout.addView(quantity);


            // display
            name.setTextSize(16);
            quantity.setTextSize(16);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    50, Gravity.CENTER);
            layoutParams.setMargins(20, 50, 20, 20);
            linearLayout.setLayoutParams(layoutParams);

            name.setLayoutParams(new TableLayout.LayoutParams(0,
                    ActionBar.LayoutParams.WRAP_CONTENT, 1));

            price.setLayoutParams(new TableLayout.LayoutParams(0,
                    ActionBar.LayoutParams.WRAP_CONTENT, 1));

            quantity.setLayoutParams(new TableLayout.LayoutParams(0,
                    ActionBar.LayoutParams.WRAP_CONTENT, 1));


            name.setGravity(Gravity.CENTER);
            price.setGravity(Gravity.CENTER);
            quantity.setGravity(Gravity.CENTER);

            cartLayout.addView(linearLayout);
        }
        totalPriceView.setText("Total: $" + Double.toString(cart.getValue()));

    }

    public void updateTime(View view){
        if (cart.getSize() >0 ) {
            volleyStringRequest(GET_URL);
        } else {
            Toast.makeText(getApplicationContext(), "You haven't order anything yet", Toast.LENGTH_SHORT).show();
        }
    }
    public void volleyStringRequest(String url) {
        final String REQUEST_TAG = "In Cust Activity, Getting Array";
        progressDialog.setMessage("Refreshing Orders...");
        progressDialog.show();

        StringRequest stringReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        etaTextView.setText(response);
                        Toast.makeText(TrackingActivity.this, "Time Updated", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(REQUEST_TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringReq, REQUEST_TAG);
    }

}
