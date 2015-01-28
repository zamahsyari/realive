package com.qd.realive;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleMap mMap;
    private Double latitude,longitude;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String currentaddress;
    protected String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Map
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);
        latitude=37.443058;
        longitude=-122.158248;
        LatLng california=new LatLng(latitude,longitude);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(california, 10));
        mMap.addMarker(new MarkerOptions()
                .position(california)
                .title("Palo Alto")
                .snippet("California"));
        buildGoogleApiClient();
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void func_yes(View view){
        latitude=37.443058;
        longitude=-122.158230;
        LatLng NewLoc=new LatLng(latitude,longitude);
        //update button
        final Button activ=(Button)findViewById(R.id.activ);
            activ.setText("ACTIVE");
            activ.setBackgroundColor(Color.parseColor("#008000"));
            activ.setTextColor(Color.parseColor("#ffffff"));
        //update map
        mMap.clear();
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NewLoc, 19));
        //wearable
        //if events occured, activate wearable notification
//        Intent mapIntent = getPackageManager().getLaunchIntentForPackage("com.qd.realive");
        Intent mapIntent=new Intent(Intent.ACTION_MAIN);
        mapIntent.setClassName("com.qd.realive","com.qd.realive.Secondary");
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(false).setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Fire accident was happened")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText("774 Emerson Street, Palo Alto, California")
                        .addAction(R.drawable.common_full_open_on_phone,"Are you ready?",mapPendingIntent)
                        .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        int notificationId=001;
        notificationManager.notify(notificationId, notification);
        Intent in=new Intent(getApplicationContext(),Secondary.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }
    public void func_no(View view){
        final Button activ=(Button)findViewById(R.id.activ);
        activ.setText("NOT ACTIVE");
        activ.setBackgroundColor(Color.parseColor("#800000"));
        activ.setTextColor(Color.parseColor("#ffffff"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
            LatLng california=new LatLng(latitude,longitude);
            startIntentService();

            Geocoder gCoder=new Geocoder(getApplicationContext(), Locale.getDefault());
            try{
                List<Address> addressList=gCoder.getFromLocation(latitude,longitude,1);
                if(addressList!=null && addressList.size()>0){
                    Address address=addressList.get(0);
                    StringBuilder sb=new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    city=address.getLocality();
                    currentaddress=sb.toString();
                }
            }catch (IOException e){
                Log.e("Oops","Unable to connect geocoder", e);
            }

            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(california, 20));
            mMap.addMarker(new MarkerOptions()
                    .position(california)
                    .title(city)
                    .snippet(currentaddress));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void displayAddressOutput() {
        currentaddress= mAddressOutput;
    }

    protected void startIntentService(){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }
    class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();
        }
    }
}
