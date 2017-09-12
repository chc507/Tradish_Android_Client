package com.example.vanne.tradish_alpha.Applications;

import android.app.Application;

import com.example.vanne.tradish_alpha.Models.Cart;
import com.example.vanne.tradish_alpha.Models.Menus;
import com.parse.Parse;
import com.parse.ParseACL;

import java.util.ArrayList;

/**
 * Created by vanne on 8/22/2017.
 */

public class StarterApplication extends Application {
    private static ArrayList<Menus> shoppingcartmenus;
    private static Cart m_cart = new Cart();
    private int selectedNumberOfOrder;
    @Override
    public void onCreate() {
        super.onCreate();

        /*Enable Local Datastore.*/
        Parse.enableLocalDatastore(this);

        /*Add your initialization code here*/
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("d9316831d20d595c18c381925fe237b6494d747e")
                .clientKey("594024d891ca6568791701d141320e63aef79ae5")
                .server("http://ec2-54-148-159-202.us-west-2.compute.amazonaws.com:80/parse/")
                .build()
        );

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        //Shoppingcart Stuff
        shoppingcartmenus = new ArrayList<Menus>();
        selectedNumberOfOrder = 0;
    }

    public void setShoppingCartMenu(ArrayList<Menus> shoppingcartmenus) {
        this.shoppingcartmenus = shoppingcartmenus;
    }

    public static ArrayList<Menus> getShoppingCartMenu() {
        return shoppingcartmenus;
    }

    public static void addMenu(Menus newMenu) {
        shoppingcartmenus.add(newMenu);
    }

    public static void resetCart() {
        shoppingcartmenus.clear();
    }

    public static void setShoppingCart(Cart cart){
        m_cart = cart;
    }

    public static Cart getShoppingCart(){
        return m_cart;
    }
}

