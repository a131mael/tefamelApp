package aaf.com.br.favodemelapp.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;

import org.aaf.escolar.LocationDTO;

import java.net.InetAddress;
import java.util.UUID;

public class LocationBroadcastService extends Service {

    private LocationDTO locationDTO = null;
    final LocationService locationService = new LocationService();

    public static final String
            ACTION_LOCATION_BROADCAST = LocationBroadcastService.class.getName() + "LocationBroadcast",
            EXTRA_LATITUDE = "extra_latitude",
            EXTRA_LONGITUDE = "extra_longitude";

    private static final int
            MIN_TIME = 60,
            MIN_DISTANCE = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        String uiAndroid = getUID();
        if(isInternetAvailable()){
            locationDTO = locationService.getLocationIDAndroid(uiAndroid);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        canAccessLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("");
        }
        checkLocationPermission();
        sendBroadcastMessage(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        checkLocationPermission();
                        sendBroadcastMessage(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    System.out.print("changed");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        System.out.print("PROVIDER");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        System.out.print("DIS");
                    }
                }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForeground(12345678, getNotification());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private String getUID() {
        try{
            final String tmDevice, tmSerial, androidId;
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);


            canAccessMemory();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                canAccessMemory();
            }
            canAccessMemory();
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String deviceId = deviceUuid.toString();
            return deviceId;

        }catch (Exception e){
            System.out.println(e);
        }
        return "";
    }

    private void sendBroadcastMessage(Location location) {
        if (location != null) {
            if(locationDTO != null){
                if(isInternetAvailable()){
                    locationDTO.setLatitude(location.getLatitude());
                    locationDTO.setLongitude(location.getLongitude());
                    locationService.save(locationDTO);
                }
            }

           /* Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);*/
        }


    }


    private boolean canAccessMemory() {
        return (hasPermission(Manifest.permission.READ_PHONE_STATE));
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasPermission(String perm) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //  requestPermissions(LOCATION_PERMS, 1);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private Notification getNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_01").setAutoCancel(true);

            return builder.build();
        }
        return null;
    }


}