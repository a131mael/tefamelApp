package aaf.com.br.favodemelapp.Util;

public class ClassesStaticas {

    public static String USER_SAVED = "userSaved";

    public static UsuarioDTO user;
    public static Long idUsuario = 0L;
    public static String idUserSaved = "idDoUserSaved";
    public static String nomeDoUsuario = "nomeDoUsuarioSaved";

    static {

        user = new UsuarioDTO();
    }
}

