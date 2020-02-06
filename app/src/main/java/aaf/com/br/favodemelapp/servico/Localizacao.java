package aaf.com.br.favodemelapp.servico;

import  com.google.android.gms.maps.model.LatLng;

import java.time.LocalDate;
import java.util.Date;

public class Localizacao {

    private LatLng latLang ;
    private Date lastUpdate;
    private float accuracy = 1000;
    private boolean atualizado;

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LatLng getLatLang() {
        return latLang;
    }

    public void setLatLang(LatLng latLang) {
        this.latLang = latLang;
    }

    public boolean isAtualizado() {
        return atualizado;
    }

    public void setAtualizado(boolean atualizado) {
        this.atualizado = atualizado;
    }
}
