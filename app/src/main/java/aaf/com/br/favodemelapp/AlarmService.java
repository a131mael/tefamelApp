package aaf.com.br.favodemelapp;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.aaf.escolar.LocationDTO;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.service.LocationService;
import aaf.com.br.favodemelapp.servico.Localizacao;

public class AlarmService extends IntentService{

    public Context context=null;
    private LocationDTO locationDTO = null;
    final LocationService locationService = new LocationService();
    private LocationManager locationManager;

    // Must create a default constructor
    public AlarmService() {

        // Used to name the worker thread, important only for debugging.
        super("test-service");

    }

    @Override

    public void onCreate() {

        super.onCreate(); // if you override onCreate(), make sure to call super().
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        String uiAndroid = getUID();
        if (isInternetAvailable()) {
            if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3) {
                locationDTO = locationService.getLocationIDAndroid(uiAndroid);
            }
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        context=this;
        try {

            if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3) {
                if(locationDTO != null){
                    LatLng latLng = getCoordenadas();
                    if (latLng != null) {

                        locationDTO.setLatitude(latLng.latitude);
                        locationDTO.setLongitude(latLng.longitude);
                        locationService.save(locationDTO);
                    }
                }

            }

            Thread.sleep(50);

        }

        catch (InterruptedException e){
            e.printStackTrace();
        }



        String val = intent.getStringExtra("foo");

        // Do the task here
        Log.i("MyTestService", val);

    }

    private String getUID() {
        try {
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

        } catch (Exception e) {
            return "";
        }
    }

    public boolean isInternetAvailable(){
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public LatLng getCoordenadas() {
        try {
            Localizacao gps = getCoordenadasGPS();
            Localizacao internet = getCoordenadasInternet();
            Localizacao passive = getCoordenadasPassive();

            Localizacao bestLocation = new Localizacao();
            if (passive.isAtualizado()) {
                bestLocation = passive;
            }
            if (internet.isAtualizado()) {
                bestLocation = internet;
            }
            if (gps.isAtualizado()) {
                bestLocation = gps;
            }

            if (bestLocation.isAtualizado() && internet.isAtualizado()) {
                if (bestLocation.getAccuracy() > internet.getAccuracy() || somaMinutos(bestLocation.getLastUpdate(), 5).before(internet.getLastUpdate())) {
                    bestLocation = internet;
                }
            }
            if (bestLocation.isAtualizado() && passive.isAtualizado()) {
                if (bestLocation.getAccuracy() > passive.getAccuracy() || somaMinutos(bestLocation.getLastUpdate(), 5).before(passive.getLastUpdate())) {
                    bestLocation = passive;
                }
            }

            return bestLocation.getLatLang();
        } catch (Exception e) {
            return null;
        }
    }

    public Localizacao getCoordenadasInternet() {
        //removeCachePosition();
        Localizacao localizacao = new Localizacao();
        if (isInternetAvailable()) {
            try {
                canAccessLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    System.out.print("");
                }
                Location locationNetWork = locationManager.getLastKnownLocation("network");

                if (locationNetWork != null) {
                    LatLng ltLng = new LatLng(locationNetWork.getLatitude(), locationNetWork.getLongitude());
                    localizacao.setLatLang(ltLng);
                    localizacao.setLastUpdate(new Date());
                    localizacao.setAccuracy(locationNetWork.getAccuracy());
                    localizacao.setAtualizado(true);
                }
            } catch (Exception e) {

            }
        }

        return localizacao;
    }

    public Localizacao getCoordenadasGPS() {
        //removeCachePosition();
        Localizacao localizacao = new Localizacao();
        try {
            canAccessLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.print("");
            }
            Location locationNetWork = locationManager.getLastKnownLocation("gps");

            if (locationNetWork != null) {
                LatLng ltLng = new LatLng(locationNetWork.getLatitude(), locationNetWork.getLongitude());
                localizacao.setLatLang(ltLng);
                localizacao.setLastUpdate(new Date());
                localizacao.setAccuracy(locationNetWork.getAccuracy());
                localizacao.setAtualizado(true);
            }
        } catch (Exception e) {

        }

        return localizacao;
    }

    public Localizacao getCoordenadasPassive() {
        //removeCachePosition();
        Localizacao localizacao = new Localizacao();
        try {
            canAccessLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.print("");
            }
            Location locationNetWork = locationManager.getLastKnownLocation("passive");

            if (locationNetWork != null) {
                LatLng ltLng = new LatLng(locationNetWork.getLatitude(), locationNetWork.getLongitude());
                localizacao.setLatLang(ltLng);
                localizacao.setLastUpdate(new Date());
                localizacao.setAccuracy(locationNetWork.getAccuracy());
                localizacao.setAtualizado(true);
            }
        } catch (Exception e) {

        }

        return localizacao;
    }

    private boolean canAccessLocation() {
        try {
            return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        } catch (Exception e) {
            return true;
        }

    }

    private boolean canAccessMemory() {
        try {
            return (hasPermission(Manifest.permission.READ_PHONE_STATE));
        } catch (Exception e) {
            return true;
        }
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

    private Date somaMinutos(Date dataHora, int minutos) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataHora);

        gc.add(Calendar.MINUTE, minutos);

        return gc.getTime();
    }


}