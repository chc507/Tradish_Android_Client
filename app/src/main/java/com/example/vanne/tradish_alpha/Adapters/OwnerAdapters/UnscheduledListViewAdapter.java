package com.example.vanne.tradish_alpha.Adapters.OwnerAdapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;

import java.util.ArrayList;

/**
 * Created by vanne on 8/10/2017.
 */

public class UnscheduledListViewAdapter extends ArrayAdapter {

    private ArrayList<RestOrderModel>unscheduledList;
    private Activity context;

    /*This handles the orders in restaurant menu */

    public UnscheduledListViewAdapter(Activity context, ArrayList<RestOrderModel> unscheduledList){
        super(context, R.layout.unscheduledlist, unscheduledList);
        this.unscheduledList = unscheduledList;
        this.context = context;
    }

    public void updateOrders(ArrayList<RestOrderModel> unscheduledList ){
        this.unscheduledList = unscheduledList;
        //Log.i("updating orders", Integer.toString(RestOrderList.size()));
        notifyDataSetChanged();
        //Log.i("After orders", Integer.toString(RestOrderList.size()));
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        String street;
        String city;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.unscheduledlist, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        String address = unscheduledList.get(position).getAddress();
        String[] separated = address.split(",");
        if(address.matches("") || address == null || separated.length < 2){
            street = "Invalid Street Name"; // this will contain street
            city = "Invalid City Name"; // this will contain " they taste good"
        }else {
            street = separated[0]; // this will contain street
            city = separated[1]; // this will contain " they taste good"
        }
        viewHolder.tvw1.setText(unscheduledList.get(position).getOrderIdView());
        viewHolder.tvw2.setText(street + "," + city);

        return r;
    }

    class ViewHolder {
        TextView tvw1; //order Id
        TextView tvw2; //address
        ViewHolder(View view){
            tvw1 = (TextView)view.findViewById(R.id.orderId);
            tvw2 = (TextView)view.findViewById(R.id.address);
        }
    }
}

