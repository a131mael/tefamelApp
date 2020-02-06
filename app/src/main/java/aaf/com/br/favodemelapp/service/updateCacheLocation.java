package aaf.com.br.favodemelapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class updateCacheLocation extends AppCompatActivity implements Runnable {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_update_cache_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        removeCachePosition();
    }

    private void removeCachePosition() {
        try {
            new Thread() {

                @SuppressLint("MissingPermission")
                public void run() {
                    canAccessLocation();

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                }

                                @Override
                                public void onProviderDisabled(String provider) {
                                }

                                @Override
                                public void onLocationChanged(final Location location) {
                                }
                            });
                }
            }.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.READ_PHONE_STATE));
    }

    private boolean canAccessMemory() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return true;
    }

    @Override
    public void run() {
        removeCachePosition();
    }
}
