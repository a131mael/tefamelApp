package aaf.com.br.favodemelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class BoletoSucessoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto_sucesso);
    }

    public void enviarOutroBoleto(View v) {
        Intent it = new Intent(BoletoSucessoActivity.this, BoletoActivity.class);
        startActivity(it);
    }

}
