package com.batuhanertas.batue.denemebirki;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    TextView dataTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        dataTextView = (TextView) findViewById(R.id.dataTextView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }

        // get the last known location of the user so there won't be an empty textview
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);



    }

    // turn off the location when the app is not used so user's device battery can be saved
    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //return;
        }
        locationManager.removeUpdates(this);
    }

    // turn the location feature on when the app is used
    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //return;
        }

        //get the location every 400 milliseconds or in every meter
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    // get various information of user's location to display
    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        Float bearing = location.getBearing();
        Float speed =  location.getSpeed();
        Float accuracy = location.getAccuracy(); //accuracy of the location

        //get user's current location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "There is a problem with the address";
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);

            if(addressList != null && addressList.size() > 0)
            {
                Log.i("Address", addressList.get(0).toString());
                address = addressList.get(0).toString();
                address = "";
                for(int i = 0; i <= addressList.get(0).getMaxAddressLineIndex(); i++){
                    address += addressList.get(0).getAddressLine(i) + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dataInfo = "Latitude " + String.valueOf(lat) + "\n" +
                "Longitude " + String.valueOf(lng) + "\n" +
                "Altitude " + String.valueOf(alt) + "\n" +
                "Bearing " + String.valueOf(bearing) + "\n" +
                "Speed " + String.valueOf(speed) + "\n" +
                "Accuracy " + String.valueOf(accuracy) + "\n" +
                "Address\n " + String.valueOf(address) + "\n";

        dataTextView.setText(dataInfo);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
