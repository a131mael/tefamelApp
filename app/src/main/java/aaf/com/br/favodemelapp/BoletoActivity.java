package aaf.com.br.favodemelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import aaf.com.br.favodemelapp.Util.VariaveisGlobais;
import aaf.com.br.favodemelapp.service.AlunoService;

public class BoletoActivity extends AppCompatActivity {

    AlunoService alunoService;
    EditText email;
    Spinner mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alunoService = new AlunoService();
        setContentView(R.layout.activity_boleto);
        email = (EditText) findViewById(R.id.editTextEmail);
        mes = (Spinner) findViewById(R.id.spinner);
    }

    public void enviarBoleto(View v) {
        alunoService.enviarBoleto(VariaveisGlobais.loggedUser.getIdContratoAtivo(),String.valueOf(mes.getSelectedItemId() -1),email.getText().toString());
        Intent it = new Intent(BoletoActivity.this, BoletoSucessoActivity.class);
        startActivity(it);

    }
}
