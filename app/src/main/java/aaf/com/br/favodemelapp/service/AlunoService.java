package aaf.com.br.favodemelapp.service;

import org.aaf.escolar.AlunoDTO;

import aaf.com.br.favodemelapp.Util.EndPoints;

public class AlunoService extends Service{


    public AlunoDTO getStudentById(String idStudent) {
        String endPoint =  EndPoints.STUDENT_BY_ID.replace("*1", idStudent);
        Object retorno = getObject(endPoint);

        return (AlunoDTO)retorno;
    }

    public void enviarBoleto(String idStudent,String mes,String email) {
        String endPoint =  EndPoints.ENVIAR_BOLETO_RESPONSAVEL.replace("*1", idStudent).replace("*2", mes).replace("*3", email);
        Object retorno = getObject(endPoint);

    }

}
