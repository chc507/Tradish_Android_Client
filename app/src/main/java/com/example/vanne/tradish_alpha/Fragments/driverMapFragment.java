package com.example.vanne.tradish_alpha.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanne.tradish_alpha.Models.DeliveryOrderModel;
import com.example.vanne.tradish_alpha.Models.RestOrderModel;
import com.example.vanne.tradish_alpha.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class driverMapFragment extends Fragment implements OnMapReadyCallback {

    //All item type
    private GoogleMap mMap;
    private MapView mMapView;
    private View rootView;
    private ArrayList<DeliveryOrderModel> incompletedList;
    //private ArrayList<LatLng>latLngs;

    public driverMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            incompletedList = getArguments().getParcelableArrayList("incompletedList");
            for(int i = 0; i < incompletedList.size(); i++) {
                Log.i("Id in frag", incompletedList.get(i).getOrderIdView());
                Log.i("price in frag", incompletedList.get(i). getTotalPriceView());
                Log.i("address in frag", incompletedList.get(i). getAddress());
                Log.i("latlng in frag", incompletedList.get(i).getLatLng().toString());
            }
        }

        //TODO: learn how to pick up arguments and etc.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView)rootView.findViewById(R.id.mapViewEdit);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < incompletedList.size(); i++){
            LatLng custLoc = incompletedList.get(i).getLatLng();
            builder.include(custLoc);
            if(incompletedList.get(i).getSequence() == -1){
                mMap.addMarker(
                        new MarkerOptions()
                                .position(custLoc)
                                .title(incompletedList.get(i).getOrderIdView())
                                .snippet(incompletedList.get(i).getAddress()));}
            else{
                int markerRes = findResId(i);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(custLoc)
                                .title(incompletedList.get(i).getOrderIdView())
                                .icon(BitmapDescriptorFactory.fromResource(markerRes))
                                .snippet(incompletedList.get(i).getAddress()));
            }
            Log.i("Completed", incompletedList.get(i). getAddress());
        }

        LatLng bluefly = new LatLng(41.253461, -96.022720);
        builder.include(bluefly);
        MarkerOptions rest = new MarkerOptions().position(bluefly).title("Blue Fly").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).snippet("721 S 72nd St, Omaha, NE 68114");
        mMap.addMarker(rest);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        //mMap.addPolyline(new PolylineOptions().add(userLoc,restLoc).width(10).color(Color.RED));
    }



    public int findResId(int i){
        int resId;
        switch (i){
            case 0:
                resId = R.drawable.number_1;
                break;
            case 1:
                resId = R.drawable.number_2;
                break;
            case 2:
                resId = R.drawable.number_3;
                break;
            case 3:
                resId = R.drawable.number_4;
                break;
            case 4:
                resId = R.drawable.number_5;
                break;
            case 5:
                resId = R.drawable.number_6;
                break;
            case 6:
                resId = R.drawable.number_7;
                break;
            case 7:
                resId = R.drawable.number_8;
                break;
            case 8:
                resId = R.drawable.number_9;
                break;
            case 9:
                resId = R.drawable.number_10;
                break;
            default:
                resId = R.drawable.number_0;
        }
        return resId;
    }
}
