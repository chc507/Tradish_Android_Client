package com.example.vanne.tradish_alpha.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vanne.tradish_alpha.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vanne on 8/1/2017.
 */

public class UserDefinedListViewAdapter extends ArrayAdapter {

    private ArrayList<String> options = new ArrayList<String>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    private ArrayList<Integer> icons = new ArrayList<Integer>();
    private Activity context;
    private int listLayout = 0; //0 for default, 1 for meunu order
    //private boolean isSelected[];


    /*This constructor initiate the random menu
    *
    * */
    public UserDefinedListViewAdapter(Activity context, ArrayList<String> options, ArrayList<String> descriptions, ArrayList<Integer>icons){
        //ArrayList<String> options,ArrayList<String> descriptions,ArrayList<Integer>icons
        super(context, R.layout.userdefinedlist,options);
        this.context = context;
        this.options = options;
        this.descriptions = descriptions;
        this.icons = icons;
    }

    /*
        This construct the customers orders list
     */
    /*
    public UserDefinedListViewAdapter(Activity context, ArrayList<Integer> orderIdList, ArrayList<Double> totalPriceList,
                                      ArrayList<String> addressList, ArrayList<Integer> driverIdList, ArrayList<Integer> customerIdList,
                                      ArrayList<Integer> sequenceList, ArrayList<Integer> orderTypeList){
        super(context, R.layout.userdefinedlist, orderIdList);
        this.context = context;
        this.options = options;
        this.descriptions = descriptions;
        this.icons = icons;
    }
    */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            if(listLayout == 1){
                r = layoutInflater.inflate(R.layout.selectedorderlist, null, true);
            }else {
                r = layoutInflater.inflate(R.layout.userdefinedlist, null, true);
            }
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }
        viewHolder.tvw1.setText(options.get(position));
        viewHolder.tvw2.setText(descriptions.get(position));
        viewHolder.ivw.setImageResource(icons.get(position));
        return r;
    }
    //for optimazation purpose
    class ViewHolder {
        TextView tvw1; //options
        TextView tvw2; //descriptions
        ImageView ivw;//icons;
        ViewHolder(View view){
            tvw1 = (TextView)view.findViewById(R.id.options);
            tvw2 = (TextView)view.findViewById(R.id.descriptions);
            ivw = (ImageView)view.findViewById(R.id.icons);
        }
    }
}
