package com.example.vanne.tradish_alpha.Applications;

import android.app.Application;
import android.view.View;

import com.example.vanne.tradish_alpha.Models.Menus;
import com.parse.Parse;
import com.parse.ParseACL;
import java.util.ArrayList;


/**
 * Created by Yuchen on 8/23/2017.
 */

public class ShoppingcartApplication extends Application {

    private static ArrayList<Menus> shoppingcartmenus = new ArrayList<Menus>();


    static int selectedNumberOfOrder = 0;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setShoppingCartMenu (ArrayList<Menus> shoppingcartmenus) {
        this.shoppingcartmenus = shoppingcartmenus;

    }

    public static ArrayList<Menus> getShoppingCartMenu(){
        return shoppingcartmenus;
    }

    public static void addMenu(Menus newMenu){
        shoppingcartmenus.add(newMenu);
    }


    public static void resetCart()
    {
//        setText("Total cart value = $ 0 ");
        shoppingcartmenus.clear();
    }



}
