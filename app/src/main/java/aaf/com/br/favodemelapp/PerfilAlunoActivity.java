package aaf.com.br.favodemelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.service.AlunoService;
import aaf.com.br.favodemelapp.service.UserService;

public class PerfilAlunoActivity extends AppCompatActivity {

    UserService userService = new UserService();
    CheckBox alerta;
    CheckBox email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_aluno);
        alerta = (CheckBox) findViewById(R.id.checkAlertaProximidade);
        email = (CheckBox) findViewById(R.id.ckEnviarEmail);

        if(VariaveisGlobais.loggedUser != null){
            alerta.setChecked(VariaveisGlobais.loggedUser.isAlertaProximidade());
            email.setChecked(VariaveisGlobais.loggedUser.isEnviarBoletosEmail());
        }


    }

    public void salvarPerfil(View v) {
        VariaveisGlobais.loggedUser.setAlertaProximidade(alerta.isChecked());
        VariaveisGlobais.loggedUser.setEnviarBoletosEmail(email.isChecked());

        userService.save(VariaveisGlobais.loggedUser);
        Intent it = new Intent(PerfilAlunoActivity.this, MainActivity.class);
        startActivity(it);

    }
}
