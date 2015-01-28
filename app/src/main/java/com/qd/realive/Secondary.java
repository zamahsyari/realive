package com.qd.realive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import retrofit.RestAdapter;

/**
 * Created by zmachmobile on 13/12/14.
 */
public class Secondary extends Activity {
    private GoogleMap mMap;
    private Double latitude,longitude,latitude2,longitude2;
    private static final String API_URL="http://thisisrealive.mybluemix.net/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        //Map
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);
        latitude=37.443058;
        longitude=-122.158230;
        LatLng california=new LatLng(latitude,longitude);

        latitude2=37.442205;
        longitude2=-122.159375;
        LatLng fire=new LatLng(latitude2,longitude2);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fire, 20));
        mMap.addMarker(new MarkerOptions()
                .position(california)
                .title("Palo Alto")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("California"));
        mMap.addMarker(new MarkerOptions()
                .position(fire)
                .title("Fire accident was happened")
                .snippet("774 Emerson Street, Palo Alto, California"));
    }
    public void func_yes(View view){
        latitude=37.443058;
        longitude=-122.158230;
        LatLng NewLoc=new LatLng(latitude,longitude);
        //update button
//        final Button activ=(Button)findViewById(R.id.activ);
//        activ.setText("ACTIVE");
//        activ.setBackgroundColor(Color.parseColor("#008000"));
//        activ.setTextColor(Color.parseColor("#ffffff"));
        //update map
        mMap.clear();
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NewLoc, 19));

//        RestAdapter restAdapter=new RestAdapter.Builder()
//                .setEndpoint(API_URL)
//                .build();
//
//        ResponderService service=restAdapter.create(ResponderService.class);
//
//        new Responder("680c212d-5cf7-40ce-86be-b65a740f24fe","firefighter");
//        new Responder("79417860-0801-4eaf-8e4a-735bf1cf0250","firefighter");
//        new Responder("281d7d54-7088-4afd-b2ff-89cec012d7cc","firefighter");

        Intent in=new Intent(getApplicationContext(),Third.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

    public void func_no(View view){

    }
}
