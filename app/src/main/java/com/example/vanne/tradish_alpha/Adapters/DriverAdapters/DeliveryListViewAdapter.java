package com.example.vanne.tradish_alpha.Adapters.DriverAdapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
import com.example.vanne.tradish_alpha.R;

import java.util.ArrayList;

import static android.R.drawable.checkbox_off_background;
import static android.R.drawable.checkbox_on_background;

/**
 * Created by vanne on 8/1/2017.
 */

public class DeliveryListViewAdapter extends ArrayAdapter {
    private ArrayList<DeliveryOrderModel>DeliveryOrderList;
    private Activity context;

    /*This handles the orders in restaurant menu */
    public DeliveryListViewAdapter(Activity context, ArrayList<DeliveryOrderModel> DeliveryOrderList){
        super(context, R.layout.detail_delivery_order, DeliveryOrderList);
        this.DeliveryOrderList = DeliveryOrderList;
        this.context = context;
    }

    public void updateOrders(ArrayList<DeliveryOrderModel> DeliveryOrderList ){
        this.DeliveryOrderList = DeliveryOrderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.detail_delivery_order, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.tvw1.setText("Order ID: " + DeliveryOrderList.get(position).getOrderIdView());
        viewHolder.tvw2.setText(DeliveryOrderList.get(position).getOrderStatusView());
        viewHolder.tvw3.setText("Total: $" + DeliveryOrderList.get(position).getTotalPriceView());
        viewHolder.ivw.setImageResource(parseSequence(DeliveryOrderList.get(position).getSequence()));

        return r;
    }

    public int parseSequence(int position){
        int resId;
        switch (position){
            case 1:
                resId = R.drawable.number_1;
                break;
            case 2:
                resId = R.drawable.number_2;
                break;
            case 3:
                resId = R.drawable.number_3;
                break;
            case 4:
                resId = R.drawable.number_4;
                break;
            case 5:
                resId = R.drawable.number_5;
                break;
            case 6:
                resId = R.drawable.number_6;
                break;
            case 7:
                resId = R.drawable.number_7;
                break;
            case 8:
                resId = R.drawable.number_8;
                break;
            case 9:
                resId = R.drawable.number_9;
                break;
            case 10:
                resId = R.drawable.number_10;
                break;
            default:
                resId = R.drawable.number_0;
        }
        return resId;
    }


    class ViewHolder {
        TextView tvw1; //options
        TextView tvw2; //descriptions
        ImageView ivw;//icons;
        TextView tvw3; //prices
        CheckedTextView ctv1; //check mark
        ViewHolder(View view){
            tvw1 = (TextView)view.findViewById(R.id.options);
            tvw2 = (TextView)view.findViewById(R.id.descriptions);
            tvw3 = (TextView)view.findViewById(R.id.priceView); //prices
            ivw = (ImageView)view.findViewById(R.id.icons);
            //ctv1 = (CheckedTextView) view.findViewById(R.id.checkedTextView);
        }
    }
}
