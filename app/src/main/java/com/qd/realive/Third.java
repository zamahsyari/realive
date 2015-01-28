package com.qd.realive;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by zmachmobile on 13/12/14.
 */
public class Third extends Activity {
    private GoogleMap mMap;
    private Double latitude,longitude,latitude2,longitude2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
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

        final Button btnOpenPopup=(Button)findViewById(R.id.activ);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater=(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup,null);
                final PopupWindow popupWindow=new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                final Button btnDismiss=(Button)popupView.findViewById(R.id.btn_dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
            }
        });
    }
}
