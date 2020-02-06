package aaf.com.br.favodemelapp.Util;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.Normalizer;

/**
 * Created by Abimael Fidencio on 25/11/2016.
 */
public class Utils {

    public static void showMessage(Context context, String texto) {
        /*Context contexto = getApplicationContext();*/

        int duracao = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, texto, duracao);
        toast.show();
    }

    public static void save(String data, FileOutputStream fOut) {

        try {
            //    FileOutputStream fOut = openFileOutput(file, MODE_WORLD_READABLE);
            fOut.write(URLEncoder.encode(data, "UTF-8").getBytes());
            fOut.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static String read(FileInputStream fin) {
        try {
            // FileInputStream fin = openFileInput(file);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }

            return URLEncoder.encode(temp, "UTF-8");

        } catch (Exception e) {
            return null;
        }

    }


    public static String removeSpecialCharacter(String value){
        CharSequence cs = new StringBuilder(value);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace(" ","");

    }

    public static boolean internetDisponivel() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}