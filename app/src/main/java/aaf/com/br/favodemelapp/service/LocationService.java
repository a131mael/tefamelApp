package aaf.com.br.favodemelapp.service;

import com.cedarsoftware.util.io.JsonWriter;

import org.aaf.escolar.LocationDTO;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import aaf.com.br.favodemelapp.Util.EndPoints;
import aaf.com.br.favodemelapp.Util.Utils;

public class LocationService extends Service {

    public LocationDTO getLocationIDAndroid(String name) {
        try{
            if(Utils.internetDisponivel()){
                String endPoint = EndPoints.GET_LOCATION_BY_ANDROID_ID;
                String endPointFinal =  endPoint.replace("*1", name);
                Object obj =  getObject(endPointFinal);
                if(obj != null){
                    return  (LocationDTO) obj;
                }
            }
            return null;

        }catch (Exception e){
        return  null;
        }

    }

    public LocationDTO getLocationByName(String nome) {
        try{
            if(Utils.internetDisponivel()){
                String endPoint = EndPoints.GET_LOCATION_BY_NAME;
                String endPointFinal =  endPoint.replace("*1", nome);
                Object obj =  getObject(endPointFinal);
                if(obj != null){
                    return  (LocationDTO) obj;
                }
            }
            return null;

        }catch (Exception e){
            return null;
        }
    }

    public List<LocationDTO> geAlltLocation() {
        try{
            if(Utils.internetDisponivel()){
                String endPoint = EndPoints.GET_ALL_LOCATIONS;
                List<Object> objs = (List<Object>) getObject(endPoint);
                if(objs != null){
                    List<LocationDTO> locations = new ArrayList<>();
                    for(Object obj : objs){
                        locations.add((LocationDTO) obj);
                    }
                    return  locations;
                }
            }

            return null;

        }catch (Exception e){
            return null;
        }
    }

    private Date somaMinutos(Date dataHora, int minutos) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataHora);

        gc.add(Calendar.MINUTE, minutos);

        return gc.getTime();
    }

    public List<LocationDTO> geAlltLocationFresh() {
        Date agora = new Date();

        try{
            if(Utils.internetDisponivel()){
                String endPoint = EndPoints.GET_ALL_LOCATIONS;
                List<Object> objs = (List<Object>) getObject(endPoint);
                if(objs != null){
                    List<LocationDTO> locations = new ArrayList<>();
                    for(Object obj : objs){
                        LocationDTO loc = (LocationDTO) obj;
                        if(somaMinutos(loc.getDataUltimaAtualizacao(),30).after(agora)){
                            locations.add(loc);
                        }
                    }
                    return  locations;
                }
            }

            return null;

        }catch (Exception e){
            return null;
        }
    }

    public List<LocationDTO> geAlltLocationOutOfDate() {
        Date agora = new Date();

        try{
            if(Utils.internetDisponivel()){
                String endPoint = EndPoints.GET_ALL_LOCATIONS;
                List<Object> objs = (List<Object>) getObject(endPoint);
                if(objs != null){
                    List<LocationDTO> locations = new ArrayList<>();
                    for(Object obj : objs){
                        LocationDTO loc = (LocationDTO) obj;
                        if(somaMinutos(loc.getDataUltimaAtualizacao(),30).before(agora)){
                            locations.add(loc);
                        }
                    }
                    return  locations;
                }
            }

            return null;

        }catch (Exception e){
            return null;
        }
    }


    public LocationDTO save(LocationDTO locationDTO) {
        try{
            if(Utils.internetDisponivel()){
                if(locationDTO.getId() != null){
                    locationDTO.setDataUltimaAtualizacao(null);
                }
                JSONObject jo = JSONPPost.postJson(JsonWriter.objectToJson(locationDTO), EndPoints.SAVE_LOCATION);
                 return (LocationDTO) Service.getObject(jo);
            }
            return locationDTO;
        }catch (Exception e){
            return locationDTO;
        }

    }
}
