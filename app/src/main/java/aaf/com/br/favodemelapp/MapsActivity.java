package aaf.com.br.favodemelapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.aaf.escolar.LocationDTO;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.service.LocationService;
import aaf.com.br.favodemelapp.service.ServicoTest;
import aaf.com.br.favodemelapp.servico.Localizacao;


public class   MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    String uiAndroid = "";
    boolean centralizar = false;
    String seguindoCarro = "";
    private GoogleMap mMap;
    private MarkerOptions tefamel1;
    private MarkerOptions tefamel2;
    private MarkerOptions tefamel3;
    private MarkerOptions tefamel4;
    private MarkerOptions tefamel5;
    private MarkerOptions tefamel6;
    private MarkerOptions tefamel7;
    private MarkerOptions tefamel8;
    private MarkerOptions tefamel9;
    private MarkerOptions tefamel10;
    private List<LocationDTO> localizadoes;

    private TextView tvCarro1;
    private TextView tvCarro1Value;
    private TextView tvCarro2;
    private TextView tvCarro2Value;
    private TextView tvCarro3;
    private TextView tvCarro3Value;
    private TextView tvCarro4;
    private TextView tvCarro4Value;
    private TextView tvCarro5;
    private TextView tvCarro5Value;
    private TextView tvCarro6;
    private TextView tvCarro6Value;
    private TextView tvCarro7;
    private TextView tvCarro7Value;
    private Button toolbar ;
    private TextView tvSeguindo;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final LocationService locationService = new LocationService();
    final ServicoTest servico = new ServicoTest();
    private LocationManager locationManager;
    private LatLng minhaCoordenada;
    SimpleDateFormat formata = new SimpleDateFormat("HH:mm - dd/MM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            uiAndroid = getUID();

            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

            setContentView(R.layout.activity_maps);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            tvCarro1 = (TextView) findViewById(R.id.cordCarro1);
            tvCarro2 = (TextView) findViewById(R.id.cordCarro2);
            tvCarro3 = (TextView) findViewById(R.id.cordCarro3);
            tvCarro4 = (TextView) findViewById(R.id.cordCarro4);
            tvCarro5 = (TextView) findViewById(R.id.cordCarro5);
            tvCarro6 = (TextView) findViewById(R.id.cordCarro6);
            tvCarro7 = (TextView) findViewById(R.id.cordCarro7);
            tvSeguindo =  (TextView) findViewById(R.id.textoSeguindo);

            tvCarro1Value = (TextView) findViewById(R.id.cordCarro1C);
            tvCarro2Value = (TextView) findViewById(R.id.cordCarro2C);
            tvCarro3Value = (TextView) findViewById(R.id.cordCarro3c);
            tvCarro4Value = (TextView) findViewById(R.id.cordCarro4c);
            tvCarro5Value = (TextView) findViewById(R.id.cordCarro5c);
            tvCarro6Value = (TextView) findViewById(R.id.cordCarro6c);
            tvCarro7Value = (TextView) findViewById(R.id.cordCarro7c);

            localizadoes = getCarrosAtivos();
            if (localizadoes == null) {
                localizadoes = new ArrayList<>();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        toolbar = (Button) findViewById(R.id.atualizar);
        if(VariaveisGlobais.loggedUser != null && VariaveisGlobais.loggedUser.getTipoMembro() == 3){
            if(toolbar != null){
                toolbar.setVisibility(View.GONE);
            }
        }

        while (!isInternetAvailable()) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mMap = googleMap;

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        LatLng def = new LatLng(-27.65671294, -48.68970577);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(def));
        for (LocationDTO locs : localizadoes) {
            createMarker(locs.getNome());
        }
        updates();
        atualizacaoCarros();
        if(VariaveisGlobais.loggedUser != null && VariaveisGlobais.loggedUser.getTipoMembro() == 3){
            minhaCoordenada = getCoordenadas();
          /*  createMarkerMe();
            alertaProximidade();*/
        }
        mMap.setOnMarkerClickListener(this);

    }

    private void createMarkerMe() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(minhaCoordenada);
        markerOptions.title(VariaveisGlobais.loggedUser.getName());
        mMap.addMarker(markerOptions);
    }

    private void createMarker(String nomeCarro) {
        try {
            switch (nomeCarro) {
                case "carro1":
                    tefamel1 = new MarkerOptions();
                    tefamel1.title("Tefamel 1");
                    addIconOnMarker(tefamel1, nomeCarro);
                    break;

                case "carro2":
                    tefamel2 = new MarkerOptions();
                    tefamel2.title("Tefamel 2");
                    addIconOnMarker(tefamel2, nomeCarro);
                    break;

                case "carro3":
                    tefamel3 = new MarkerOptions();
                    tefamel3.title("Tefamel 3");
                    addIconOnMarker(tefamel3, nomeCarro);
                    break;

                case "carro4":
                    tefamel4 = new MarkerOptions();
                    tefamel4.title("Tefamel 4");
                    addIconOnMarker(tefamel4, nomeCarro);
                    break;

                case "carro5":
                    tefamel5 = new MarkerOptions();
                    tefamel5.title("Tefamel 5");
                    addIconOnMarker(tefamel5, nomeCarro);
                    break;

                case "carro6":
                    tefamel6 = new MarkerOptions();
                    tefamel6.title("Tefamel 6");
                    addIconOnMarker(tefamel6, nomeCarro);
                    break;

                case "carro7":
                    tefamel7 = new MarkerOptions();
                    tefamel7.title("Tefamel 7");
                    addIconOnMarker(tefamel7, nomeCarro);
                    break;

                case "carro8":
                    tefamel8 = new MarkerOptions();
                    tefamel8.title("Tefamel 8");
                    addIconOnMarker(tefamel8, nomeCarro);
                    break;

                case "carro9":
                    tefamel9 = new MarkerOptions();
                    tefamel9.title("Tefamel 9");
                    addIconOnMarker(tefamel9, nomeCarro);
                    break;

                case "carro10":
                    tefamel10 = new MarkerOptions();
                    tefamel10.title("Tefamel 10");
                    addIconOnMarker(tefamel10, nomeCarro);
                    break;

                default:
                    tefamel1 = new MarkerOptions();
                    tefamel1.title("Tefamel 1");
                    addIconOnMarker(tefamel1, nomeCarro);
                    break;
            }

        } catch (Exception e) {

        }

    }

    private void move(MarkerOptions carro, double latitude, double longitude) {
        try {
            LatLng carro1 = new LatLng(latitude, longitude);
            carro.position(carro1);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(carro1));
           mMap.addMarker(carro);
        } catch (Exception e) {

        }
    }

    private List<LocationDTO> getCarrosAtivos() {
        try {
            if (isInternetAvailable()) {
                List<LocationDTO> localizacoes = locationService.geAlltLocation ();
                return localizacoes;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void addIconOnMarker(MarkerOptions markerOptions, String nomeCarro) {
        BitmapDrawable bitmapdraw = null;
        try {
            switch (nomeCarro) {
                case "carro1":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                case "carro2":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus2);
                    break;

                case "carro3":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus3);
                    break;

                case "carro4":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus4);
                    break;

                case "carro5":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus5);
                    break;

                case "carro6":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                case "carro7":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                case "carro8":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                case "carro9":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                case "carro10":
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;

                default:
                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.onibus1);
                    break;
            }

            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 125, 52, false);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        } catch (Exception e) {

        }
    }

    private void updates() {
        try {
            final LocationService locationService = new LocationService();

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (isInternetAvailable()) {
                                    mMap.clear();
                                    for (LocationDTO loc : localizadoes) {
                                        adicionarMarcador(locationService, loc.getNome());
                                    }
                                    if(VariaveisGlobais.loggedUser != null && VariaveisGlobais.loggedUser.getTipoMembro() == 3){
                                        createMarkerMe();
                                    }
                                }

                            } catch (Exception e) {
                                System.out.println("Nao conseguiu localizar");
                            }
                        }
                    });
                }
            }, 1000, 13000);// 13 segundos
        } catch (Exception e) {

        }

    }

    private void removeMarker(LocationDTO locationDTO){
        MarkerOptions mo = getMarcadorCarro(locationDTO.getNome());
        mo.visible(false);

    }

    private void atualizacaoCarros() {
        try {
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (isInternetAvailable()) {
                                    localizadoes = locationService.geAlltLocationFresh();
                                }
                                List<LocationDTO> carrosDesatualizados = locationService.geAlltLocationOutOfDate();
                                
                                for(LocationDTO locR : carrosDesatualizados){
                                    removeMarker(locR);
                                }
                            } catch (Exception e) {
                                System.out.println("Nao conseguiu encontrar os carros");
                            }
                        }
                    });
                }
            }, 1000, 600000);// 10 minutos
        } catch (Exception e) {

        }

    }


    public void centralizarMapa(View v) {
        centralizar = !centralizar;
        if(toolbar == null){
            toolbar = (Button) findViewById(R.id.atualizar);
        }
        if(seguindoCarro.equalsIgnoreCase("")){
            seguindoCarro = "Ariel";
        }

        if (centralizar) {
            toolbar.setText("Não seguir");
            tvSeguindo.setText("Você está seguindo : " + seguindoCarro);
        } else {
            toolbar.setText("Seguir");
            tvSeguindo.setText("Você não está seguindo");
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        /*String channelId = getString(R.string.default_notification_channel_id);*/
        String channelId = "1";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*  .setSmallIcon(R.drawable.ic_stat_ic_notification)*/
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE) ;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void adicionarMarcador(LocationService service, String nomeCarro) {
        try {
            LocationDTO locationDTO = service.getLocationByName(nomeCarro);
            MarkerOptions marcador = getMarcadorCarro(nomeCarro);
            if (locationDTO != null) {
                mostrarCordenadaTextView(locationDTO);
                marcador.title(locationDTO.getNomeMapa());


                move(marcador, locationDTO.getLatitude(), locationDTO.getLongitude());
                if(VariaveisGlobais.loggedUser != null && VariaveisGlobais.loggedUser.getTipoMembro() != 3){
                    if(seguindoCarro.equalsIgnoreCase("") && locationDTO.getAndroidID().equalsIgnoreCase(uiAndroid)){

                        seguindoCarro =locationDTO.getNomeMapa();
                    }

                }else{
                    if(marcador.getTitle().equalsIgnoreCase(seguindoCarro) && centralizar) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador.getPosition()));

                        tvSeguindo.setText("Você seguindo  " + marcador.getTitle());
                    }
                }




            }
        } catch (Exception e) {
        }
    }

    private void mostrarCordenadaTextView(LocationDTO locationDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append(locationDTO.getNomeMapa());
        sb.append(normalizaTamanho(sb,20));

        StringBuilder sb2 = new StringBuilder();
        sb2.append(formata.format(locationDTO.getDataUltimaAtualizacao()));
        sb2.append(normalizaTamanho(sb2,20));



        switch (locationDTO.getNome()) {

            case "carro1":
                tvCarro1.setVisibility(View.VISIBLE);
                tvCarro1.setText(sb.toString());
                tvCarro1Value.setVisibility(View.VISIBLE);
                tvCarro1Value.setText(sb2.toString());
                break;
            case "carro2":
                tvCarro2.setVisibility(View.VISIBLE);
                tvCarro2.setText(sb.toString());

                tvCarro2Value.setVisibility(View.VISIBLE);
                tvCarro2Value.setText(sb2.toString());
                break;

            case "carro3" :
                tvCarro3.setVisibility(View.VISIBLE);
                tvCarro3.setText(sb.toString());
                tvCarro3Value.setVisibility(View.VISIBLE);
                tvCarro3Value.setText(sb2.toString());
                break;
            case "carro4" :
                tvCarro4.setVisibility(View.VISIBLE);
                tvCarro4.setText(sb.toString());

                tvCarro4Value.setVisibility(View.VISIBLE);
                tvCarro4Value.setText(sb2.toString());
                break;
            case "carro5":
                tvCarro5.setVisibility(View.VISIBLE);
                tvCarro5.setText(sb.toString());

                tvCarro5Value.setVisibility(View.VISIBLE);
                tvCarro5Value.setText(sb2.toString());
                break;
            case "carro6" :
                tvCarro6.setVisibility(View.VISIBLE);
                tvCarro6.setText(sb.toString());

                tvCarro6Value.setVisibility(View.VISIBLE);
                tvCarro6Value.setText(sb2.toString());
                break;
            case "carro7":
                tvCarro7.setVisibility(View.VISIBLE);
                tvCarro7.setText(sb.toString());

                tvCarro7Value.setVisibility(View.VISIBLE);
                tvCarro7Value.setText(sb2.toString());
                break;
            default:
                break;
        }
    }

    private String normalizaTamanho(StringBuilder nomeMapa, int tamanho) {

        if(nomeMapa.lastIndexOf("M") >- 1){
            tamanho -=5;
        }
        if(nomeMapa.lastIndexOf("m") >- 1){
            tamanho -=5;
        }
        if(nomeMapa.lastIndexOf("I") >- 1){
            tamanho +=2;
        }
        if(nomeMapa.lastIndexOf("i") >- 1){
            tamanho +=0;
        }
        if(nomeMapa.lastIndexOf("l") >- 1){
            tamanho +=2;
        }

        while (nomeMapa.length()<tamanho){
            nomeMapa.insert(nomeMapa.length()," ");
        }
        return "";
    }

    private MarkerOptions getMarcadorCarro(String nomeCarro) {
        try {
            switch (nomeCarro) {
                case "carro1":
                    return tefamel1;
                case "carro2":
                    return tefamel2;

                case "carro3":
                    return tefamel3;

                case "carro4":
                    return tefamel4;

                case "carro5":
                    return tefamel5;

                case "carro6":
                    return tefamel6;

                case "carro7":
                    return tefamel7;

                case "carro8":
                    return tefamel8;

                case "carro9":
                    return tefamel9;

                case "carro10":
                    return tefamel10;

                default:
                    return tefamel1;
            }
        } catch (Exception e) {
            return tefamel1;
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

    private boolean canAccessMemory() {
        try {
            return (hasPermission(Manifest.permission.READ_PHONE_STATE));
        } catch (Exception e) {
            return false;
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

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

            if(bestLocation.isAtualizado() && internet.isAtualizado()){
                if (bestLocation.getAccuracy() > internet.getAccuracy() || somaMinutos(bestLocation.getLastUpdate(), 5).before(internet.getLastUpdate())) {
                    bestLocation = internet;
                }
            }
            if(bestLocation.isAtualizado() && passive.isAtualizado()){
                if (bestLocation.getAccuracy() > passive.getAccuracy() || somaMinutos(bestLocation.getLastUpdate(), 5).before(passive.getLastUpdate())) {
                    bestLocation = passive;
                }
            }

            return bestLocation.getLatLang();
        }catch (Exception e){
            return  null;
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
        if(isInternetAvailable()){
            try{
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
            }catch (Exception e){

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
        } catch (Exception e){

        }

        return localizacao;
    }

    public Localizacao getCoordenadasPassive() {
        //removeCachePosition();
        Localizacao localizacao = new Localizacao();
        try{
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
        }catch (Exception e){

        }

        return localizacao;
    }

    private boolean canAccessLocation() {
        try {
            return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        }catch (Exception e){
            return true;
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        seguindoCarro = marker.getTitle();
        tvSeguindo.setText("Você seguindo  " + marker.getTitle());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return false;
    }
}
