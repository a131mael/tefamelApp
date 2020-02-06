package aaf.com.br.favodemelapp.service;

import org.aaf.escolar.MemberDTO;

import aaf.com.br.favodemelapp.Util.EndPoints;
import aaf.com.br.favodemelapp.Util.Utils;

public class LoginService extends Service{


    public MemberDTO getMember(String phoneNumber) {

        if(Utils.internetDisponivel()){
            String endPoint =  EndPoints.LOGIN_BY_PHONE.replace("*1", phoneNumber);
            Object obj = getObject(endPoint);
            if(obj != null){
                return (MemberDTO) obj;
            }
        }
        return null;
    }

    public MemberDTO getMemberByID(String id) {
        MemberDTO mdto = null;
        if(Utils.internetDisponivel()){

            String endPoint =  EndPoints.MEMBER_BY_ID.replace("*1", id);
            Object obj = getObject(endPoint);
            if(obj != null){
                mdto= (MemberDTO) obj;
            }
        }
        return mdto;
    }

    public MemberDTO getMember(String login, String password) {
        if(Utils.internetDisponivel()){
            String endPoint =  EndPoints.LOGIN_BY_LOGIN_PASSWORD.replace("*1", login).replace("*2",password);
            Object obj = getObject(endPoint);
            if(obj != null){
                return (MemberDTO) obj;
            }
        }
        return null;
    }

}
