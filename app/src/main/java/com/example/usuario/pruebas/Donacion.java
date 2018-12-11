package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Donacion extends AppCompatActivity {

    private ValidadorTarjetaCredito validadorTarjetaCredito;

    private EditText EditTextNumeroTarjeta;
    private EditText EditTextCodigoSeguridad;
    private EditText EditTextMesExpiracion;
    private EditText EditTextAnioExpiracion;
    private EditText EditTextMonto;

    private DatabaseHandlerCompras db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);

        EditTextNumeroTarjeta=(EditText) findViewById(R.id.EditText_NumeroTarjeta);
        EditTextNumeroTarjeta.setBackgroundResource(R.drawable.border_color);

        EditTextCodigoSeguridad=(EditText) findViewById(R.id.EditText_CodigoSeguridad);
        EditTextCodigoSeguridad.setBackgroundResource(R.drawable.border_color);

        EditTextMesExpiracion=(EditText) findViewById(R.id.EditText_MesExpiracion);
        EditTextMesExpiracion.setBackgroundResource(R.drawable.border_color);

        EditTextAnioExpiracion=(EditText) findViewById(R.id.EditText_AnioExpiracion);
        EditTextAnioExpiracion.setBackgroundResource(R.drawable.border_color);

        EditTextMonto=(EditText) findViewById(R.id.EditText_Monto);
        EditTextMonto.setBackgroundResource(R.drawable.border_color);




    }

    public void makeRequestInsertarDonacion(View view){
        if(TextUtils.isEmpty(EditTextNumeroTarjeta.getText().toString())){
            EditTextNumeroTarjeta.setError("Este campo no puede estar vacío. Por favor ingrese un número de tarjeta");
            return;
        }

        if(!validadorTarjetaCredito.validateCardNumber(Long.parseLong(EditTextNumeroTarjeta.getText().toString()))){
            EditTextNumeroTarjeta.setError("Número de tarjeta no válido. Por favor ingrese nuevamente");
            return;
        }

        if(TextUtils.isEmpty(EditTextCodigoSeguridad.getText().toString())){
            EditTextCodigoSeguridad.setError("Este campo no puede estar vacío. Por favor ingrese el código de seguridad de la tarjeta");
            return;
        }

        if(!validadorTarjetaCredito.validateCVV(EditTextCodigoSeguridad.getText().toString(),validadorTarjetaCredito.getCardType(EditTextNumeroTarjeta.getText().toString()))){
            EditTextCodigoSeguridad.setError("Código de seguridad no válido. Por favor ingrese nuevamente");
            return;
        }

        if(TextUtils.isEmpty(EditTextMesExpiracion.getText().toString())){
            EditTextMesExpiracion.setError("Este campo no puede estar vacío. Por favor ingrese el mes de expiración de la tarjeta");
            return;
        }

        if(TextUtils.isEmpty(EditTextAnioExpiracion.getText().toString())){
            EditTextAnioExpiracion.setError("Este campo no puede estar vacío. Por favor ingrese el año de expiración de la tarjeta");
            return;
        }

        if(!validadorTarjetaCredito.validateExpiryDate(EditTextMesExpiracion.getText().toString(),EditTextAnioExpiracion.getText().toString())){
            EditTextAnioExpiracion.setError("Datos incorrectos. Por favor ingrese nuevamente");
            return;
        }

        if(TextUtils.isEmpty(EditTextMonto.getText().toString())){
            EditTextMonto.setError("Este campo no puede estar vacío. Por favor ingrese un monto desde 1 dólar");
            return;
        }

        final Intent intent = getIntent();
        final String messageIdCompra = intent.getStringExtra(EXTRA_INDEX);
        final String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);
        //Toast.makeText(getApplicationContext(),messageIdCompra,Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.14:8080/ProyectoIntegrador/nuevaDonacion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.matches("1")){
                            Toast.makeText(getApplicationContext(),"Donación Realizada Exitosamente",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Algo salio mal. Inténtalo de nuevo",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();
                params.put("id_compra", messageIdCompra);
                params.put("id_usuario", messageIdUsuario);
                params.put("monto", EditTextMonto.getText().toString());



                return params;
            }
        };
        queue.add(stringRequest);


        Intent intent1 = new Intent(this, ConfirmacionDonacion.class);
        startActivity(intent1);


        db1 = new DatabaseHandlerCompras(getApplicationContext());
        db1.deleteProductos();

    }

    public void volverCatalogo(View view){
        Intent intent = new Intent(this, DetallesDonante.class);
        startActivity(intent);
    }
}


