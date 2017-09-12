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

public class CompletedListViewAdapter extends ArrayAdapter {

    private ArrayList<DeliveryOrderModel>completedList;
    private Activity context;

    /*This handles the orders in restaurant menu */

    public CompletedListViewAdapter(Activity context, ArrayList<DeliveryOrderModel> completedList){
        super(context, R.layout.completed_delivery_list, completedList);
        this.completedList = completedList;
        this.context = context;
    }

    public void updateOrders(ArrayList<DeliveryOrderModel> completedList ){
        this.completedList = completedList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.completed_delivery_list, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }
        viewHolder.tvw1.setText("| " + completedList.get(position).getOrderIdView());
        viewHolder.tvw2.setText("s:" + Integer.toString(position + 1));//start with 1
        return r;
    }

    class ViewHolder {
        TextView tvw1; //order Id
        TextView tvw2; //sequence
        ViewHolder(View view){
            tvw1 = (TextView)view.findViewById(R.id.orderId);
            tvw2 = (TextView)view.findViewById(R.id.sequence);
        }
    }
}

