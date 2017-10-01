package com.example.vanne.tradish_alpha.Adapters.CustomerAdapter;

/**
 * Created by Yuchen on 8/14/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vanne.tradish_alpha.Models.Menus;
import com.example.vanne.tradish_alpha.R;

import java.util.ArrayList;
import java.util.List;


public class MenuListAdapter extends ArrayAdapter<Menus> {

    private Context context;
    private List<Menus> menus;
    public MenuListAdapter(Context context, List<Menus> menus){

        super(context, R.layout.menu_list_layout,menus);
        this.context = context;
        this.menus = menus;
    }
    String MenuAdapterTag = "In Menu Adapter";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.menu_list_layout,parent,false);

        ImageView imageViewPhoto = view.findViewById(R.id.imageView3);
        if(menus.get(position).getFlag() == 0) {
            Log.e(MenuAdapterTag, "Get Photo");
            imageViewPhoto.setImageResource(menus.get(position).getPhoto());
        }else {
            Log.e(MenuAdapterTag, "Get Bitmap");
            imageViewPhoto.setImageBitmap(menus.get(position).getBitmap());
        }

        TextView textViewname = (TextView) view.findViewById(R.id.textViewname);
        textViewname.setText(menus.get(position).getName());
        textViewname.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView textViewPrice = (TextView) view.findViewById(R.id.textViewprice);
        textViewPrice.setText(String.valueOf(menus.get(position).getPrice()));
        textViewPrice.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView textviewCategory = (TextView) view.findViewById(R.id.textViewCategory);
        textViewPrice.setText(String.valueOf(menus.get(position).getPrice()));
        textViewPrice.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView textViewNote = (TextView) view.findViewById(R.id.textViewNote);
        textViewPrice.setText(String.valueOf(menus.get(position).getPrice()));
        textViewPrice.setGravity(Gravity.CENTER_HORIZONTAL);

        return view;
    }


    public void updateMenu(List<Menus> update){
        this.menus = update;
        notifyDataSetChanged();
    }

}
