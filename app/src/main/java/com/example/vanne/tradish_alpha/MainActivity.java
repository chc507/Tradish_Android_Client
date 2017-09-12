package com.example.vanne.tradish_alpha;

import android.content.DialogInterface;
import android.content.Intent;

import com.example.vanne.tradish_alpha.CustomerActivity.CustomerActivity;
import com.example.vanne.tradish_alpha.LoginAndSignup.LoginSignupActivity;
import com.example.vanne.tradish_alpha.OwnerActivity.DeliveryScheduleActivity;
import com.example.vanne.tradish_alpha.OwnerActivity.OrderListActivity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.util.Arrays.asList;


public class MainActivity extends AppCompatActivity {


    String identity = "";
    ArrayList<String>options;
    ArrayAdapter optionsListAdapter;
    ListView optionsListView = null;
    int selectedPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Set up dialog for user to select*/
        options = new ArrayList<String>(asList("Driver", "Customer", "Restaurant"));
        optionsListAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice, options);
        optionsListView = new ListView(this);
        optionsListView.setAdapter(optionsListAdapter);
        optionsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (position){
                    case 2:
                        selectedPos = 2;
                        break;
                    case 1:
                        selectedPos = 1;
                        break;
                    default:
                        selectedPos = 0;
                }
            }
        });

        final AlertDialog.Builder optionsDialog = new AlertDialog.Builder(MainActivity.this);

        optionsDialog.setIcon(android.R.drawable.ic_lock_idle_alarm);
        optionsDialog.setTitle("Tell Us Who You Are?");

        optionsDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (selectedPos){
                    case 0:
                        navigateToDriver();
                        break;
                    case 1:
                        navigateToCustomer();
                        break;
                    case 2:
                        navigateToRestaurant();
                }
            }
        });

        optionsDialog.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent mainIntent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
        }
        });
        optionsDialog.setView(optionsListView);
        AlertDialog dialog = optionsDialog.create();
        dialog.show();
    }

    public void navigateToCustomer() {
        Log.i("Menu", "custom");
        identity = "Customer";
        Intent customIntent = new Intent(getApplicationContext(), LoginSignupActivity.class);
        customIntent.putExtra("Identity", identity);
        startActivity(customIntent);
    }

    public void navigateToRestaurant() {
        Log.i("Menu", "Resturant");
        identity = "Restaurant";
        Intent resturantIntent = new Intent(getApplicationContext(), LoginSignupActivity.class);
        resturantIntent.putExtra("Identity", identity);
        startActivity(resturantIntent);
    }

    public void navigateToDriver() {
        identity = "Driver";
        Intent driverIntent = new Intent(getApplicationContext(), LoginSignupActivity.class);
        driverIntent.putExtra("Identity", identity);
        startActivity(driverIntent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
