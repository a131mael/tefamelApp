package aaf.com.br.favodemelapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.aaf.escolar.LocationDTO;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import aaf.com.br.favodemelapp.MainActivity;
import aaf.com.br.favodemelapp.R;
import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.servico.Localizacao;

public class ServicoTest extends Service {
    public List<Worker> threads = new ArrayList<Worker>();

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private LocationManager locationManager;
    private LocationDTO locationDTO = null;
    final LocationService locationService = new LocationService();
    private List<LocationDTO> localizadoes;
    private LatLng minhaCoordenada;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Log.i("Script", "onCreate()");
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            String uiAndroid = getUID();
            if (isInternetAvailable()) {
                if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3) {
                    locationDTO = locationService.getLocationIDAndroid(uiAndroid);
                }

                localizadoes = getCarrosAtivos();
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                if (localizadoes == null) {
                    localizadoes = new ArrayList<>();
                }
                if (VariaveisGlobais.loggedUser != null && VariaveisGlobais.loggedUser.getTipoMembro() == 3) {
                    minhaCoordenada = getCoordenadas();
                }


            }
        } catch (Exception e) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(12345678, getNotification());
        }else{

        }

    }

    private List<LocationDTO> getCarrosAtivos() {
        try {
            if (isInternetAvailable()) {
                List<LocationDTO> localizacoes = locationService.geAlltLocation();
                return localizacoes;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Script", "onStartCommand()");

        Worker w = new Worker(startId);
        w.start();
        threads.add(w);

        return (super.onStartCommand(intent, flags, startId));
        // START_NOT_STICKY
        // START_STICKY
        // START_REDELIVER_INTENT
    }

    private void updates(int periodo) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isInternetAvailable()) {
                        if (locationDTO != null) {
                            if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3) {
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
                                    LatLng latLng = getCoordenadas();
                                    if (latLng != null) {
                                        locationDTO.setLatitude(latLng.latitude);
                                        locationDTO.setLongitude(latLng.longitude);
                                        locationService.save(locationDTO);
                                    }
                                }
                            }else{
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
                                    LatLng latLng = getCoordenadas();
                                    if (latLng != null) {
                                        locationDTO.setLatitude(latLng.latitude);
                                        locationDTO.setLongitude(latLng.longitude);
                                        locationService.save(locationDTO);
                                    }
                                }
                            }

                        }

                        if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() == 3) {
                            if(VariaveisGlobais.loggedUser.isAlertaProximidade()){
                                minhaCoordenada = getCoordenadas();
                                localizadoes = getCarrosAtivos();
                                alertaProximidade();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, 1000, periodo);
    }

   /* private void alertasProximidade() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (locationDTO != null) {
                        if (isInternetAvailable()) {
                            if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() == 3) {
                                alertaProximidade();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, 1000, 20000); //a cada 20 segundos
    }*/

    class Worker extends Thread {
        public int count = 0;
        public int startId;
        public boolean ativo = true;

        public Worker(int startId) {
            this.startId = startId;
        }

        public void run() {
            while (ativo && count < 1) {
                try {
                    Thread.sleep(1000);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        /*JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
                        ComponentName componentName = new ComponentName(getApplicationContext(), tefamel.acf.com.br.tefamel.LocationService.class);
                        JobInfo jobInfo;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(10000).build();
                        } else {
                            jobInfo = new JobInfo.Builder(1, componentName).setPeriodic(10000).build();
                        }

                        jobScheduler.schedule(jobInfo);*/
                        //alertaProximidade();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&  Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1 ) {
                            updates(60000);
                        }
                        if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() == 3) {
                            updates(60000);

                        }else{
                         //   updates(20000);

                        }
                    } else {
                        if (VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() == 3) {
                            updates(60000);

                        }else{
                       //     updates(20000);

                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                count++;
                Log.i("Script", "COUNT: " + count);
            }
            stopSelf(startId);
        }
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

    private Date somaMinutos(Date dataHora, int minutos) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataHora);

        gc.add(Calendar.MINUTE, minutos);

        return gc.getTime();
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (int i = 0, tam = threads.size(); i < tam; i++) {
            threads.get(i).ativo = false;
        }
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

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void sendNotification2(String messageTitle, String messageBody) {
        long[] pattern = {500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setVibrate(pattern)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setSound(defaultSoundUri);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private NotificationManager mNotificationManager;
    private void sendNotification3(String titulo, String corpoTexto, String detalheTitulo, String channelId, Uri sound) {

        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this.getApplicationContext(), channelId);
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(corpoTexto);
        bigText.setBigContentTitle(titulo);
        bigText.setSummaryText(detalheTitulo);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.onibus1);
        mBuilder.setContentTitle("TEFAMEL");
        mBuilder.setContentText(corpoTexto);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setSound(sound);
        mNotificationManager =  (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,corpoTexto,NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(Integer.valueOf(channelId), mBuilder.build());
        }

    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello World!");

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }


    private void alertaProximidade() {
        if (isInternetAvailable()) {
            for (LocationDTO loc : localizadoes) {
                if (loc != null) {

                    if(minhaCoordenada != null){

                        long diferencaTempo = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Date agora = new Date();

                            diferencaTempo = agora.getTime() - loc.getDataUltimaAtualizacao().getTime();
                            diferencaTempo/=1000;
                            diferencaTempo/=60;

                        }

                        if(diferencaTempo < 10){
                            LatLng posicao = new LatLng(loc.getLatitude(), loc.getLongitude());
                            double distance = SphericalUtil.computeDistanceBetween(minhaCoordenada, posicao);
                            if (distance < 1000 && distance > 500) {

                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                sendNotification3("Tefamel Chegando.." + String.format("%.1f", distance) + "Metros","Transporte Escolar Favo de Mel já está próximo.",loc.getNomeMapa(), loc.getId()+"54",sound);

                            }else{
                                if (distance < 500) {
                                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                                    sendNotification3("Tefamel Chegando.." + String.format("%.1f", distance) + "Metros" ,"Transporte Escolar Favo de Mel já está próximo.", loc.getNomeMapa(), loc.getId()+"54",sound);

                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
