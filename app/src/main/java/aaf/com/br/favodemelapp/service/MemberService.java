package aaf.com.br.favodemelapp.service;

import com.cedarsoftware.util.io.JsonWriter;

import org.aaf.escolar.MemberDTO;
import org.json.JSONObject;

import aaf.com.br.favodemelapp.Util.EndPoints;
import aaf.com.br.favodemelapp.Util.JSONPPost;

public class MemberService extends Service{

    public MemberDTO save(MemberDTO memberDTO) {
        JSONObject jo = JSONPPost.postJson(JsonWriter.objectToJson(memberDTO), EndPoints.MEMBERS);
        return (MemberDTO) Service.getObject(jo);
    }


}
