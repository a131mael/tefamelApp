package aaf.com.br.favodemelapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.FileNotFoundException;

import aaf.com.br.favodemelapp.Util.ClassesStaticas;
import aaf.com.br.favodemelapp.Util.UsuarioDTO;
import aaf.com.br.favodemelapp.Util.Utils;
import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.service.LocationService;
import aaf.com.br.favodemelapp.service.MemberService;
import aaf.com.br.favodemelapp.service.UserService;


public class MainActivity extends AppCompatActivity {

    private ImageButton btResultados;
    private ImageButton imageButton2;
    //  private MenuItem menuItemLogout;

    final LocationService locationService = new LocationService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        //TODO analisar no futuro solucao pra mandar localizacao sempre mesmo com ele fechado, mas so funciona em no sdk 26 api

      /*  final Intent intent = new Intent(this.getApplication(), BackgroundService.class);
        this.getApplication().startService(intent);
        this.getApplication().startForegroundService(intent);
        this.getApplication().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
*/



//        setSupportActionBar(toolbar);

        //  menuItemLogout = findViewById(R.id.);

        btResultados = (ImageButton) findViewById(R.id.btResultados);
        imageButton2 = (ImageButton) findViewById(R.id.btBoletos);



        //REMOVER O BOLETIM e o Boleto SE FOR UM PROFESSOR mudar para o tipo 1  = professor
        if(VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3){
            imageButton2.setVisibility(View.GONE);
        }

        try {
            String id = Utils.read(openFileInput(ClassesStaticas.idUserSaved));
            ClassesStaticas.user.setId(id != null ? Long.parseLong(id) :0L) ;
        } catch (FileNotFoundException e) {
            //saveUser();
        }
        //SALVAR O TOKEN FM do usuario
        try {
            //TODO se nao atualizar o token ao reinstalar o app remover essa linha para nao fazer o if e sempre salvar o token
            //   if(VariaveisGlobais.loggedUser.getTokenFCM() == null || VariaveisGlobais.loggedUser.getTokenFCM().equalsIgnoreCase("")){

            String token = Utils.read(openFileInput(VariaveisGlobais.nameToken));
            if(token != null){
                saveMember(token);

            }
            // }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //else {

        //UsuarioDTO u = getUser();
//            if(u != null){
        //              ClassesStaticas.user = u;
        //          }else{
//                saveUser();
        //        }

        //}

    }

    private void saveMember(String token){
        try {
            MemberService memberService = new MemberService();
            token = token.replace("%253A",":");
            VariaveisGlobais.tokenFCM = token;
            if(VariaveisGlobais.loggedUser != null){
                VariaveisGlobais.loggedUser.setTokenFCM(VariaveisGlobais.tokenFCM);
            }

            memberService.save(VariaveisGlobais.loggedUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UsuarioDTO getUser(){
        try{
            UserService userService = new UserService();
            return  userService.getUser(ClassesStaticas.user.getId());
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            try {
                Utils.save(0+"", openFileOutput(VariaveisGlobais.idUserSaved, MODE_PRIVATE));
            } catch (Exception e) {
                try {
                    Utils.save(0+"", openFileOutput(VariaveisGlobais.idUserSaved, MODE_WORLD_READABLE));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }*/


        return super.onOptionsItemSelected(item);
    }

    public void openRecados(View v) {
       /* Intent it = new Intent(MainActivity.this, RecadosList.class);
        startActivity(it);*/
    }

    public void openBoletim(View v) {
        Intent it = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(it);
    }

    public void openBoleto(View v) {
        Intent it = new Intent(MainActivity.this, BoletoActivity.class);
        startActivity(it);
    }

    public void openProfile(View v) {
        /*Intent it = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(it);*/
    }


    public void openRanking(View v) {
        /*Intent it = new Intent(MainActivity.this, RankingActivity.class);
        startActivity(it);*/
    }

    public void enviarNome(View v) {
        if(VariaveisGlobais.loggedUser == null || VariaveisGlobais.loggedUser.getTipoMembro() != 3){
            Intent it = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(it);
        }else{
            Intent it = new Intent(MainActivity.this, PerfilAlunoActivity.class);
            startActivity(it);
        }
    }


}
