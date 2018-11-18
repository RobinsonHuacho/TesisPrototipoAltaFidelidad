package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ConfirmacionDonacion extends AppCompatActivity {

    DatabaseHandlerCompras db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_donacion);

        getSupportActionBar().hide();
    }

    public void regresarListaBeneficiarios(View view)
    {
        Intent intent = new Intent(this,ListaBeneficiarioDonacion.class);
        startActivity(intent);
    }
}
