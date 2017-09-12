package com.example.vanne.tradish_alpha.Adapters.CustomerAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vanne.tradish_alpha.Models.RestaurantModel;
import com.example.vanne.tradish_alpha.R;


import java.util.ArrayList;


/**
 * Created by vanne on 9/6/2017.
 */

public class RestListAdapter extends ArrayAdapter {

    /*This handles the orders in restaurant menu */

    private ArrayList<RestaurantModel> restaurantModels;
    private Activity context;

    public RestListAdapter(Activity context, ArrayList<RestaurantModel> restaurantModels){
        super(context, R.layout.activity_restaurant_listview, restaurantModels);
        this.restaurantModels = restaurantModels;
        this.context = context;
    }


    public void updateList(ArrayList<RestaurantModel> restaurantModels ){
        this.restaurantModels = restaurantModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_restaurant_listview, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.tvw1.setText(restaurantModels.get(position).getName());
        viewHolder.tvw2.setText(restaurantModels.get(position).getAddress());
        viewHolder.tvw3.setText(restaurantModels.get(position).getBusinessHour());
        if(restaurantModels.get(position).getFlag() == 0) {
            viewHolder.ivw.setImageResource(R.drawable.question_sign_icon);
        }else {
            viewHolder.ivw.setImageBitmap(restaurantModels.get(position).getBitmap());
        }
        viewHolder.tvw3.setText(restaurantModels.get(position).getBusinessHour());
        viewHolder.tvw4.setText(restaurantModels.get(position).getPriceLevel());

        return r;
    }

    class ViewHolder {
        TextView tvw1; //options
        TextView tvw2; //descriptions
        ImageView ivw;//icons;
        TextView tvw3; //business hours
        TextView tvw4; //price level
        ViewHolder(View view){
            tvw1 = (TextView)view.findViewById(R.id.options);
            tvw2 = (TextView)view.findViewById(R.id.descriptions);
            ivw = (ImageView)view.findViewById(R.id.icons);
            tvw3 = (TextView)view.findViewById(R.id.businesshour);
            tvw4 = (TextView)view.findViewById(R.id.priceLevel);
        }
    }
}
