package aaf.com.br.favodemelapp.servico;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.aaf.escolar.LocationDTO;

import java.util.List;
import java.util.UUID;

public class LocationService extends JobService {

    JobParameters params;
    DoItTask doIt;

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private LocationManager locationManager;
    private LocationDTO locationDTO = null;
    final aaf.com.br.favodemelapp.service.LocationService locationService = new aaf.com.br.favodemelapp.service.LocationService();


    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        Log.d("TestService", "Work to be called from here");
        doIt = new DoItTask();
        doIt.execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("TestService", "System calling to stop the job here");
        if (doIt != null)
            doIt.cancel(true);
        return false;
    }

    private class DoItTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("DoItTask", "Clean up the task here and call jobFinished...");
            jobFinished(params, false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String uiAndroid = getUID();
            locationDTO = locationService.getLocationIDAndroid(uiAndroid);
            enviar();
            return null;
        }
    }

    private void enviar() {
        try {
            if (locationDTO != null) {
                LatLng latLng = getCoordenadas();
                if (latLng != null) {
                    locationDTO.setLatitude(latLng.latitude);
                    locationDTO.setLongitude(latLng.longitude);
                    locationService.save(locationDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        try{
            final Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } finally {

                    }
                }
            };
            t.start();
            return t;
        }catch (Exception e){

        }
        return null;
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

    public LocationManager getLocationManager() {
        return locationManager;
    }

    private LatLng getCoordenadas() {
        LatLng localizacao = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        canAccessLocation();

        for (String provider : providers) {
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

    private void forceActualization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.getLocationManager().requestLocationUpdates(
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
}
