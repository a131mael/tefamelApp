package aaf.com.br.favodemelapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.aaf.escolar.LocationDTO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import aaf.com.br.favodemelapp.service.LocationService;

public class EnviadorLocal extends AppCompatActivity {

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private LocationManager locationManager;
    private TextView latitude;
    private TextView longitude;
    private LocationDTO locationDTO = null;
    final LocationService locationService = new LocationService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviador_local);
        latitude = (TextView) findViewById(R.id.textViewLatitude);
        longitude = (TextView) findViewById(R.id.textViewLongitude);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String uiAndroid = getUID();
        locationDTO = locationService.getLocationIDAndroid(uiAndroid);
    }

    public void enviarCoordenadas(View v) {
        Intent it = new Intent("SERVICO_TEST");
        it.putExtra("aas", "sdvsd");
        it.setPackage(this.getPackageName());
        startService(it);
      /*  adicionaListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName componentName = new ComponentName(getApplicationContext(), tefamel.acf.com.br.tefamel.LocationService.class);
            *//*JobInfo jobInfo = new JobInfo.Builder(1, componentName).setPeriodic(1000,5).build();*//*
            JobInfo jobInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(10000).build();
            } else {
                jobInfo = new JobInfo.Builder(1, componentName).setPeriodic(10000).build();
            }

            jobScheduler.schedule(jobInfo);
        }else{
            updates();
        }*/
    }

    private void updates() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void run() {
                        try {
                            if (locationDTO != null) {
                                LatLng latLng = getCoordenadas();
                                if (latLng != null) {
                                    locationDTO.setLatitude(latLng.latitude);
                                    locationDTO.setLongitude(latLng.longitude);
                                    locationService.save(locationDTO);
                                    latitude.setText(String.valueOf(latLng.latitude));
                                    longitude.setText(String.valueOf(latLng.longitude));
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }, 100, 10000);
    }

    private String getUID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
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
    }

    private LatLng getCoordenadas() {
        LatLng localizacao = null;

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {
            canAccessLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.print("");
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                if (l.getLatitude() != locationDTO.getLatitude() || l.getLatitude() != locationDTO.getLongitude()) {
                    bestLocation = l;
                }
            } else {
                if (l.getLatitude() != locationDTO.getLatitude() || l.getLatitude() != locationDTO.getLongitude()) {
                    bestLocation = l;
                }
            }
        }

        if (bestLocation != null) {
            localizacao = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
        }

        return localizacao;
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.READ_PHONE_STATE));
    }

    private boolean canAccessMemory() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return true;
    }

    public void adicionaListener() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
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
                        System.out.println("UDOU A LOCALICACAO ");
                    }
                });

    }
}
