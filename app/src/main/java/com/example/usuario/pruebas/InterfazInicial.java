package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InterfazInicial extends AppCompatActivity {

    private final int duracionPantalla   = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_inicial);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(InterfazInicial.this, SeleccionRol.class);
                startActivity(intent);
                finish();
            };
        }, duracionPantalla);
    }
}
