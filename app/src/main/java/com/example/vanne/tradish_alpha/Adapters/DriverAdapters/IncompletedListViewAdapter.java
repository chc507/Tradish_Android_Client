package com.example.vanne.tradish_alpha.Adapters.DriverAdapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
import com.example.vanne.tradish_alpha.R;

import java.util.ArrayList;

/**
 * Created by vanne on 8/10/2017.
 */

public class IncompletedListViewAdapter extends ArrayAdapter {

    private ArrayList<DeliveryOrderModel>incompletedList;
    private Activity context;

    /*This handles the orders in restaurant menu */

    public IncompletedListViewAdapter(Activity context, ArrayList<DeliveryOrderModel> incompletedList){
        super(context, R.layout.unscheduledlist, incompletedList);
        this.incompletedList = incompletedList;
        this.context = context;
    }

    public void updateOrders(ArrayList<DeliveryOrderModel> incompletedList ){
        this.incompletedList = incompletedList;
        //Log.i("updating orders", Integer.toString(RestOrderList.size()));
        notifyDataSetChanged();
        //Log.i("After orders", Integer.toString(RestOrderList.size()));
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.unscheduledlist, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        String address = incompletedList.get(position).getAddress();
        String[] separated = address.split(",");
        String street = separated[0]; // this will contain street
        String city = separated[1]; // this will contain " they taste good"
        viewHolder.tvw1.setText("ID: " + incompletedList.get(position).getOrderIdView());
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

