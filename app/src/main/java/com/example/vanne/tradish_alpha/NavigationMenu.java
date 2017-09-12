package com.example.vanne.tradish_alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.vanne.tradish_alpha.Menus.CustomerMenu;
import com.example.vanne.tradish_alpha.Menus.DriverMenu;
import com.example.vanne.tradish_alpha.Menus.RestaurantMenu;

public class NavigationMenu extends AppCompatActivity {

    String identity = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_navigation);
    }

    public void navigateToCustomer(View view){
        Log.i("Menu", "custom");
        Intent customIntent = new Intent(getApplicationContext(), CustomerMenu.class);
        startActivity(customIntent);
    }

    public void navigateToResturant(View view){
        identity  = "Resturant";
        Intent resturantIntent = new Intent(getApplicationContext(), RestaurantMenu.class);
        resturantIntent.putExtra("Identity", identity);
        startActivity(resturantIntent);
    }

    public void navigateToDriver(View view){
        identity  = "Driver";
        Intent driverIntent = new Intent(getApplicationContext(), DriverMenu.class);
        driverIntent.putExtra("Identity", identity);
        startActivity(driverIntent);
    }
}
