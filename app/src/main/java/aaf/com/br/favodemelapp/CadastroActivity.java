package aaf.com.br.favodemelapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;

import org.aaf.escolar.LocationDTO;

import java.util.UUID;

import aaf.com.br.favodemelapp.service.LocationService;

public class CadastroActivity extends AppCompatActivity {

    private EditText editText;
    private EditText nomeMap;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private LocationService locationService;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        editText = (EditText) findViewById(R.id.tagCarro);
        nomeMap = (EditText) findViewById(R.id.nomeMapa);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        locationService = new LocationService();
        setNome(editText);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setNome(EditText editText){
        try{
            LocationDTO loc =  locationService.getLocationIDAndroid(getUID());
            editText.setText(loc.getNome());
            nomeMap.setText(loc.getNomeMapa());
        }catch (Exception e){
        System.out.println("Nao achou localizaçao com o nome que ja tinha");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void enviarNome(View v) {
        LocationService locationService = new LocationService();
        LocationDTO locationDTO = locationService.getLocationIDAndroid(getUID());
        if(locationDTO  == null){
            locationDTO = new LocationDTO();
        }
        locationDTO.setNome(editText.getText().toString());
        locationDTO.setNomeMapa(nomeMap.getText().toString());
        locationDTO.setAndroidID(getUID());
        locationService.save(locationDTO);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getUID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        canAccessMemory();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            canAccessMemory();
        }
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return  deviceId;
    }

    private boolean canAccessMemory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        }else{
            return true;
        }
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

}
