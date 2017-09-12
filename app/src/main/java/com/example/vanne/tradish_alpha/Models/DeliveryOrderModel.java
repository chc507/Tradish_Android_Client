package com.example.vanne.tradish_alpha.Models;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vanne.tradish_alpha.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by vanne on 8/7/2017.
 */

public class DeliveryOrderModel implements Parcelable,Comparable<DeliveryOrderModel> {
    private boolean isSelected;
    private int statusCode = 0 ; //0 incompleted(default), 1 Delivery in the process 2 completed
    private int orderId;
    private double totalPrice;
    private String address;
    private int sequence = -1;
    private LatLng latLng = new LatLng(0.0,0.0);
    private int driverId = -1;
    private int restId = -1;
    private int deliveryId = 100;


    public DeliveryOrderModel(int orderId, double totalPrice,
                          String address, int statusCode, int sequence, boolean isSelected){
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.statusCode = statusCode;
        this.sequence = sequence;
        this.isSelected = isSelected;
    }

    public DeliveryOrderModel(int orderId, int deliveryId,double totalPrice,
                              String address, int statusCode, int sequence, boolean isSelected){
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.statusCode = statusCode;
        this.sequence = sequence;
        this.isSelected = isSelected;
        this.deliveryId = deliveryId;
    }

    @Override
    public int compareTo(DeliveryOrderModel compareModel){
        int compareSequence = ((DeliveryOrderModel)compareModel).getSequence();
        /*For Ascending order*/
        return this.sequence - compareSequence;
    }

    //Parcel part
    public DeliveryOrderModel(Parcel in) {
        /*
        String[] data= new String[3];
        in.readStringArray(data);
        this.UserName= data[0];
        this.Password= data[1];
        this.Action= Integer.parseInt(data[2]);
        }
        */
        String[] data  = new String[6];
        in.readStringArray(data);
        this.orderId = Integer.parseInt(data[0]);
        this.totalPrice = Double.parseDouble(data[1]);
        this.address = data[2];
        this.statusCode = Integer.parseInt(data[3]);
        this.sequence = Integer.parseInt(data[4]);
        this.isSelected = Boolean.parseBoolean(data[5]);
    }


    //TODO: figure out how to use this
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeStringArray(new String[]{
                Integer.toString(this.orderId),
                Double.toString(this.totalPrice),
                this.address,
                Integer.toString(this.sequence),
                Integer.toString(this.statusCode),
                Boolean.toString(this.isSelected)
        });
    }

    public static final Parcelable.Creator<DeliveryOrderModel> CREATOR= new Parcelable.Creator<DeliveryOrderModel>() {
        @Override
        public DeliveryOrderModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new DeliveryOrderModel(source);  //using parcelable constructor
        }

        @Override
        public DeliveryOrderModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new DeliveryOrderModel[size];
        }
    };




    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public int getOrderStatusCode(){
        return statusCode;
    }

    public String getOrderStatusView(){
        if (statusCode == 2 ) return "Completed";
        else if(statusCode == 1 ) return "Delivery In Progress";
        else return "Incompleted";
    }

    public String getOrderIdView(){
        return Integer.toString(orderId);
    }
    public double getTotalPrice(){
        return totalPrice;
    }

    public String getTotalPriceView(){
        return Double.toString(totalPrice);
    }
    public String getAddress(){return address;}
    public void setLatLng(LatLng latLng){this.latLng = latLng;}
    public LatLng getLatLng(){return this.latLng;}
    public int getSequence(){ return this.sequence;}
    public void setSequence(int sequence){this.sequence = sequence;}
    public void setDriverId(int driverId){this.driverId = driverId;}
    public int getDriverId(){ return this.driverId;}
    public void setRestId(int restId){this.restId = restId;}
    public int getRestId(){return this.restId;}
    public void setStatusCode(int statusCode){this.statusCode = statusCode;}
    public int getStatusCode(){return this.statusCode;}
    public int getDeliveryId(){return this.deliveryId;}
    public void setDeliveryId(int deliveryId){this.deliveryId = deliveryId;}

}