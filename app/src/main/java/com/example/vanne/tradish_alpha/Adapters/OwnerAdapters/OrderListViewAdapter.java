package com.example.vanne.tradish_alpha.Adapters.OwnerAdapters;

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

import com.example.vanne.tradish_alpha.R;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;

import java.util.ArrayList;

import static android.R.drawable.checkbox_off_background;
import static android.R.drawable.checkbox_on_background;

/**
 * Created by vanne on 8/1/2017.
 */

public class OrderListViewAdapter extends ArrayAdapter {
    private ArrayList<RestOrderModel>RestOrderList;
    private Activity context;

    /*This handles the orders in restaurant menu */

    public OrderListViewAdapter(Activity context, ArrayList<RestOrderModel> RestOrderList){
        super(context, R.layout.selectedorderlist, RestOrderList);
        this.RestOrderList = RestOrderList;
        this.context = context;
    }

    public void updateOrders(ArrayList<RestOrderModel> RestOrderList ){
        this.RestOrderList = RestOrderList;
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
            r = layoutInflater.inflate(R.layout.selectedorderlist, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.tvw1.setText("Order ID: " + RestOrderList.get(position).getOrderIdView());
        viewHolder.tvw2.setText(RestOrderList.get(position).getOrderStatusView());
        viewHolder.ivw.setImageResource(RestOrderList.get(position).getIconId());
        viewHolder.tvw3.setText("Total: $" + RestOrderList.get(position).getTotalPriceView());

        if(RestOrderList.get(position).isSelected()){
            viewHolder.ctv1.setCheckMarkDrawable(checkbox_on_background);
            viewHolder.ctv1.setChecked(true);
            viewHolder.ctv1.setText("Selected");
        }else{
            viewHolder.ctv1.setCheckMarkDrawable(checkbox_off_background);
            viewHolder.ctv1.setChecked(false);
            viewHolder.ctv1.setText("Select");
        }

        return r;
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
            ivw = (ImageView)view.findViewById(R.id.icons);
            tvw3 = (TextView)view.findViewById(R.id.priceView); //prices
            ctv1 = (CheckedTextView) view.findViewById(R.id.checkedTextView);
        }
    }
}
