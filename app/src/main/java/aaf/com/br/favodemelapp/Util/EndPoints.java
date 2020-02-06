package aaf.com.br.favodemelapp.Util;

public class EndPoints {


	/*
	private static StringBuilder urlBase = new StringBuilder("http://192.168.25.36/");
	private static String context = "ProvaUFSCWebService";
*/
	/*private static StringBuilder urlBase = new StringBuilder("http://192.168.15.19:8080");*/
	private static StringBuilder urlBase = new StringBuilder("http://servidoradonai.ddns.net:8080");
	private static String context = "/EscolarWebService";

	public static final String SAVE_LOCATION = new StringBuilder(urlBase).append(context).append("/rest/locations/").toString();

	public static final String GET_LOCATION_BY_NAME = new StringBuilder(urlBase).append(context).append("/rest/locations/name/*1").toString();
	public static final String GET_LOCATION_BY_ANDROID_ID = new StringBuilder(urlBase).append(context).append("/rest/locations/androidID/*1").toString();
	public static final String GET_ALL_LOCATIONS = new StringBuilder(urlBase).append(context).append("/rest/locations/all").toString();

	public static final String LOGIN_BY_LOGIN_PASSWORD = new StringBuilder(urlBase).append(context).append("/rest/login/*1/*2").toString();


	public static final String MEMBERS = new StringBuilder(urlBase).append(context).append("/rest/members").toString();
	public static final String SCRAPS = new StringBuilder(urlBase).append(context).append("/rest/recados").toString();
	public static final String COUNT_SCRAPS = new StringBuilder(urlBase).append(context).append("/rest/recados/count/*1").toString();
	public static final String SCRAPS_RECEIVER_BY_MEMBER_AND_SCRAP = new StringBuilder(urlBase).append(context).append("/rest/recados/receiver/*1/*2").toString();

	public static final String SAVE_SCRAPS_RECEIVER = new StringBuilder(urlBase).append(context).append("/rest/recados/answer").toString();

	public static final String SCRAPS_BY_MEMBER = new StringBuilder(urlBase).append(context).append("/rest/recados/*1").toString();
	public static final String SCRAP_BY_MEMBER_ORDINAL = new StringBuilder(urlBase).append(context).append("/rest/recados/*1/*2").toString();
	public static final String GRADES_BY_STUDENT_DISCIPLINE_TURN_REC = new StringBuilder(urlBase).append(context).append("/rest/grades/gradeStudent/*1/*2/*3/*4").toString();
	public static final String LOGIN_BY_PHONE = new StringBuilder(urlBase).append(context).append("/rest/login/automatic/*1").toString();
	public static final String MEMBER_BY_ID = new StringBuilder(urlBase).append(context).append("/rest/login/*1").toString();

	public static final String STUDENT_BY_ID = new StringBuilder(urlBase).append(context).append("/rest/students/*1").toString();
    public static final String ENVIAR_BOLETO_RESPONSAVEL = new StringBuilder(urlBase).append(context).append("/rest/students/boletos/*1/*2/*3").toString();

	public static final String SAVE_USER = new StringBuilder(urlBase).append(context).append("/rest/members/").toString();
	/*==========================================================================================================*/


	public static final String QUESTIONS_BY_DISCIPLINE = new StringBuilder(urlBase).append(context).append("/rest/questions/discipline/*1").toString();

	public static final String QUESTIONS_BY_YEAR = new StringBuilder(urlBase).append(context).append("/rest/questions/discipline/*1").toString();

	public static final String QUESTIONS_BY_YEAR_AND_DISCIPLINE = new StringBuilder(urlBase).append(context).append("/rest/questions/disciplineYear?discipline=*1&year=*2").toString();

	/*public static final String SAVE_USER = new StringBuilder(urlBase).append(context).append("/rest/users/").toString();*/

	public static final String GET_USER_BY_ID = new StringBuilder(urlBase).append(context).append("/rest/users/id/*1").toString();

	public static final String SAVE_GROUP = new StringBuilder(urlBase).append(context).append("/rest/groups/").toString();

	public static final String GET_GROUP_BY_ID = new StringBuilder(urlBase).append(context).append("/rest/groups/id/*1").toString();


	public static final String USER_MAIOR_PONTUADOR = new StringBuilder(urlBase).append(context).append("/rest/users/maiorPontuadorGeral/").toString();
	public static final String USER_MAIOR_PONTUADOR_MES = new StringBuilder(urlBase).append(context).append("/rest/users/maiorPontuadorMes/").toString();
	public static final String USER_MAIOR_PONTUADOR_SEMANA = new StringBuilder(urlBase).append(context).append("/rest/users/maiorPontuadorSemana/").toString();

	public static final String GROUP_MAIOR_PONTUADOR = new StringBuilder(urlBase).append(context).append("/rest/groups/maiorPontuadorGeral/*1").toString();
	public static final String GROUP_MAIOR_PONTUADOR_MES = new StringBuilder(urlBase).append(context).append("/rest/groups/maiorPontuadorMes/*1").toString();
	public static final String GROUP_MAIOR_PONTUADOR_SEMANA = new StringBuilder(urlBase).append(context).append("/rest/groups/maiorPontuadorSemana/*1").toString();
}

