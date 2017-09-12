package com.example.vanne.tradish_alpha.CustomerActivity.MenuActivities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.*;
import android.view.View;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.vanne.tradish_alpha.CustomerActivity.CartActivities.ShoppingCartActivity;
import com.example.vanne.tradish_alpha.CustomerActivity.CustomerActivity;
import com.example.vanne.tradish_alpha.Models.Menus;
import com.example.vanne.tradish_alpha.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MenusDetailActivity extends AppCompatActivity {

    private Button buttonBACK;
    private Button buttonorder;
    private TextView textViewName;
    private TextView textViewid;
    private TextView textViewPrice;
    private TextView textViewCategory;
    private TextView textViewNote;
    private ImageView imageViewPhoto;
    private Menus menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus_detail);
        setTitle("Dish Detail Activity");
        Intent intent = getIntent();
        menus = (Menus) intent.getSerializableExtra("Menus");


        this.buttonBACK = (Button) findViewById(R.id.buttonBACK);
        this.buttonBACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenusDetailActivity.this, CustomerActivity.class);
                startActivity(intent);
            }
        });

        this.buttonorder = (Button) findViewById(R.id.buttonorder);
        this.buttonorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenusDetailActivity.this, ShoppingCartActivity.class);
                intent.putExtra("MENUSDETAIL",menus);
                startActivity(intent);
            }
        });

        /*All the menus here needed to be validated */
        this.textViewid = (TextView) findViewById(R.id.textViewid);
        this.textViewid.setText(menus.getId());
        this.textViewName = (TextView) findViewById(R.id.textViewName);
        this.textViewName.setText(menus.getName());
        this.textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        this.textViewPrice.setText(String.valueOf(menus.getPrice()));

        this.textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        this.textViewCategory.setText(menus.getCategory());

        this.textViewNote = (TextView) findViewById(R.id.textViewNote);
        this.textViewNote.setText(menus.getNote());

        this.imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        if(menus.getFlag() == 0) {
            this.imageViewPhoto.setImageResource(menus.getPhoto());
        }else{
            this.imageViewPhoto.setImageBitmap(menus.getBitmap());
        }


    }
}
