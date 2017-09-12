package com.example.vanne.tradish_alpha.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Yuchen on 8/13/2017.
 */

public class Menus implements Serializable {

    private String id;
    private String name;
    private String category;
    private String note;
    private int photo; //Might change to byteArray for Bitmap transfer.
    private double price;
    //private Bitmap ImageStreamBitMap;
    private byte[] ImageStream;
    private int flag = 0; //Indicate the source of image.  "0" indicates the image comes from local machine,
                                                            // "1" indicated the image comes from internet.
    private String MenusTag = "In Menus Tag";

    public Menus(String id, String name, String category,  String note, double price, int photo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.note = note;
        this.photo = photo;
        this.price = price;
    }

    public Menus(String id, String name, String category,  String note, double price,
                 /*Bitmap ImageStreamBitMap*/
                 byte[] ImageStream
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.note = note;
        //this.ImageStreamBitMap = ImageStreamBitMap;
        this.ImageStream = ImageStream;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }


    public Bitmap getBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(ImageStream, 0, ImageStream.length);
        return bitmap;
        //return this.ImageStreamBitMap;
    }


    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



//    public Menus(String order_ID, String order_Name, String order_Category, double price, String order_Note, int order_Photo) {
//    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getFlag(){ return flag;}
    public void setFlag(int flag){this.flag = flag;}

}
