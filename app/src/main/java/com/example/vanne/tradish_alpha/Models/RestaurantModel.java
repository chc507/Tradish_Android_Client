package com.example.vanne.tradish_alpha.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vanne.tradish_alpha.Adapters.CustomerAdapter.RestListAdapter;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vanne on 9/6/2017.
 */

public class RestaurantModel implements Parcelable,Comparable<RestaurantModel> {
    private String address;
    private String name;
    private LatLng latLng = new LatLng(0.0,0.0);
    private int restId = 1; //Associate with owner id ?
    private String businessHour = "8:00 A.M. - 11:00 P.M."; //dummy business hour
    private String priceLevel = "$$";
    private byte[] ImageStream;
    private int flag = 0; //Indicate the source of image.  "0" indicates the image comes from local machine,
    // "1" indicated the image comes from internet.

    public RestaurantModel(String name, String address, int restId,byte[] ImageStream){
        this.address = address;
        this.name = name;
        this.restId = restId;
        this.ImageStream = ImageStream;
    }

    public RestaurantModel(String name, String address, int restId){
        this.address = address;
        this.name = name;
        this.restId = restId;
    }


    @Override
    public int compareTo(RestaurantModel compareModel){
        int compareSequence = ((RestaurantModel)compareModel).getRestId();
        /*For Ascending order*/
        return this.restId - compareSequence;
    }

    //Parcel part
    public RestaurantModel(Parcel in) {
        String[] data  = new String[3];
        in.readStringArray(data);
        this.address = data[0];
        this.name = data[1];
        this.address = data[2];
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
                this.name,
                this.address,
                Integer.toString(this.restId)
        });
    }

    public static final Parcelable.Creator<RestaurantModel> CREATOR= new Parcelable.Creator<RestaurantModel>() {
        @Override
        public RestaurantModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new RestaurantModel(source);  //using parcelable constructor
        }

        @Override
        public RestaurantModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new RestaurantModel[size];
        }

    };

    public String getName(){
         return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public int getRestId(){
        return this.restId;
    }

    public String getRestIdRTextView(){
        return Integer.toString(this.restId);
    }

    public String getBusinessHour(){ return this.businessHour;}

    public Bitmap getBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(ImageStream, 0, ImageStream.length);
        return bitmap;
        //return this.ImageStreamBitMap;
    }

    public int getFlag(){ return flag;}

    public void setFlag(int flag){this.flag = flag;}

    public String getPriceLevel(){return priceLevel;}
}

