package com.example.vanne.tradish_alpha.CustomerActivity.CartActivities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vanne.tradish_alpha.CustomerActivity.CustomerActivity;
import com.example.vanne.tradish_alpha.Applications.ShoppingcartApplication;
import com.example.vanne.tradish_alpha.Applications.StarterApplication;
import com.example.vanne.tradish_alpha.CustomerActivity.Tracking.TrackingActivity;
import com.example.vanne.tradish_alpha.LoginAndSignup.LoginSignupActivity;
import com.example.vanne.tradish_alpha.Models.Cart;
import com.example.vanne.tradish_alpha.Models.Menus;
import com.example.vanne.tradish_alpha.Models.Product;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;
import com.example.vanne.tradish_alpha.OwnerActivity.DeliveryScheduleActivity;
import com.example.vanne.tradish_alpha.OwnerActivity.OrderListActivity;
import com.example.vanne.tradish_alpha.R;
import com.parse.ParseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ShoppingCartActivity extends AppCompatActivity {

    private Menus menus;

    ArrayList<Menus> selectedmenus;
    ArrayList<Product> products;

    TextView m_response;
    static Cart m_cart;

    PayPalConfiguration m_configuration;
    /*the id is the link to the paypal account, we have to create an app and get its id*/
    //String m_paypalClientId = "AfYHiloGgwg7EBTeyKRcggf8mosbFqj6KF1BmEFQVimFvoqCy3shY9V8UZHUCVML5JgcKN96D_83YXKK";
    String m_paypalClientId = "AeQ_z6Rl4nXehrXQdKLv_UvNctXMC-CwBN3fcZI1Zlv_jNR5tm-DmzbRxN2sXg7d5kzBSC8eRsLxewfS";
    Intent m_service;
    int m_paypalRequestCode = 999; // or any number you want

    String POST_URL = "http://34.208.189.14:8080/orders/placeorder";
    String ShoppingTag = "InShopping";
    LinearLayout list;

    /*Get current user*/
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("Shopping Cart Activity");

        selectedmenus = StarterApplication.getShoppingCartMenu();

        /*Obtain dish details from previous intent*/
        Intent intent = getIntent();
        menus = (Menus) intent.getSerializableExtra("MENUSDETAIL");
        /*Update menus across the app and across this activity*/
        selectedmenus.add(menus);
        //ShoppingcartApplication.addMenu(menus);
        ShoppingcartApplication.addMenu(menus);


        Log.e(ShoppingTag, Integer.toString(selectedmenus.size()));


        list = (LinearLayout) findViewById(R.id.list);

        m_response = (TextView) findViewById(R.id.response);

        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // sandbox for test, production for real
                .clientId(m_paypalClientId);

        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration); // configuration above
        startService(m_service); // paypal service, listening to calls to paypal app


        m_cart = new Cart();

        products = new ArrayList<Product>();

        for(Menus eachItem : selectedmenus){
            Product eachProduct = new Product(eachItem.getName(),eachItem.getPrice() );
            products.add(eachProduct);
        }

        for(int i = 0 ; i < products.size() ; i++)
        {
            Button button = new Button(this);
            button.setText(products.get(i).getName() + " --- " +" $ "+ products.get(i).getValue() );
            button.setTag(products.get(i));

            // display
            button.setTextSize(20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    200, Gravity.CENTER);
            layoutParams.setMargins(10, 40, 10, 40);
            button.setLayoutParams(layoutParams);

            button.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    Button button = (Button) view;
                    Product product = (Product) button.getTag();

                    m_cart.addToCart(product);
                    m_response.setText("Total cart value = $ " + String.format("%.2f",  m_cart.getValue()));
                }
            });

            button.setOnLongClickListener(new View.OnLongClickListener()
            {
                public boolean onLongClick(View view)
                {
                    Button button = (Button) view;
                    final Product product = (Product) button.getTag();
                    /*Set up Alert Dialog for order removal*/
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoppingCartActivity.this);
                    alertDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
                    alertDialog.setTitle("Remove Order");

                    alertDialog.setMessage("Remove this order?");
                    alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            m_cart.removeFromCart(product);
                            m_response.setText("Total cart value = $ " + String.format("%.2f",  m_cart.getValue()));
                        }
                    });
                    alertDialog.setNeutralButton("Back to Cart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e(ShoppingTag, "Not removing anything" + Integer.toString(m_cart.getSize()));
                            dialogInterface.dismiss();
                        }
                    });

                    alertDialog.show();

                    return true;
                }
            });

            list.addView(button);
        }

    }

    void pay(View view)
    {
        PayPalPayment cart = new PayPalPayment(new BigDecimal(m_cart.getValue()), "USD", "Cart",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class); // it's not paypalpayment, it's paymentactivity !
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);
        startActivityForResult(intent, m_paypalRequestCode);
    }

    void reset(View view)
    {
        m_response.setText("Total cart value = $ 0 ");
        products.clear();//remove items from products list
        m_cart.empty(); //remove items from the hashmap
        StarterApplication.resetCart(); //remove all items from the global variable in
        list.removeAllViews(); //remove all the views
    }

    void viewCart(View view)
    {
        Intent intent = new Intent(this, ViewCartActivity.class);
        //m_cart = m_cart;
        startActivity(intent);
    }

    public String JsonArrayBuilder(){
        String dishJsonArrayMessage = "[";

        for(int i = 0; i < selectedmenus.size(); i++){
            dishJsonArrayMessage += selectedmenus.get(i).getId();
            if(i != selectedmenus.size() - 1 ) dishJsonArrayMessage += ",";
        }

        dishJsonArrayMessage += "]";
        return dishJsonArrayMessage;
    }

    public void sendOrdersToServer(String POST_URL) {
        final String custID = currentUser.get("UserID").toString();
        final String dishIDArray = JsonArrayBuilder();
        final String userAddress = currentUser.getString("Address");
        final String RestID = "1";
        final double total = m_cart.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a,z");
        final String dateTime = sdf.format(Calendar.getInstance().getTime());
        final String orderObjectJson =
                "{"
                        + "\"Customer_ID\":" + custID + ","
                        + "\"Address\":" + userAddress + ","
                        + "\"Rest_ID\":" + RestID + ","
                        + "\"Order_Time\":" + dateTime + ","
                        + "\"Dish_ID\":" + dishIDArray + ","
                        + "\"Total\":" + total
                        + "}";

        Log.e(ShoppingTag, "Order json array " + orderObjectJson);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String Respose) {
                        Toast.makeText(ShoppingCartActivity.this, "Order was successfully sent to server", Toast.LENGTH_SHORT).show();
                        /*Go to tracking activity*/
                        Intent intent = new Intent(ShoppingCartActivity.this, TrackingActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShoppingCartActivity.this, "Fail to send orders, please try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Orders", orderObjectJson);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }



    /*Paypal Stuff*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == m_paypalRequestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // we have to confirm that the payment worked to avoid fraud
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation != null)
                {
                    String state = confirmation.getProofOfPayment().getState();

                    if(state.equals("approved")) // if the payment worked, the state equals approved
                    {
                        m_response.setText("payment approved");
                        /*update the m_cart instance in application that ran globally*/;
                        StarterApplication.setShoppingCart(m_cart);
                        Intent intent = new Intent(ShoppingCartActivity.this, TrackingActivity.class);
                        startActivity(intent);

                        /*Construct Json array for client-server communication*/
                        JsonArrayBuilder();

                        /*Send order Array to server*/
                        //sendOrdersToServer("http://34.208.189.14:8080/menus/getmenus?key=1");
                        sendOrdersToServer(POST_URL);
                    }
                    else
                        m_response.setText("error in the payment");
                }
                else
                    m_response.setText("confirmation is null");
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this,CustomerActivity.class);
        startActivity(intent);

    }

}
