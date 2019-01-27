package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Donacion_1 extends AppCompatActivity {

    private ValidadorTarjetaCredito validadorTarjetaCredito;

    private EditText EditTextNumeroTarjeta;
    private EditText EditTextCodigoSeguridad;
    private EditText EditTextMesExpiracion;
    private EditText EditTextAnioExpiracion;
    private EditText EditTextMonto;

    private DatabaseHandlerCompras db1;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView InformacionPantalla;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

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

        InformacionPantalla=(TextView) findViewById(R.id.textView14);

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;


        ImageButtonActivar.setVisibility(View.VISIBLE);
        ImageButtonDesactivar.setVisibility(View.GONE);

        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();



            }
        });


        ImageButtonDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myTTS.stop();
                myTTS.shutdown();

                ImageButtonActivar.setVisibility(View.VISIBLE);
                ImageButtonDesactivar.setVisibility(View.GONE);
            }
        });

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                pantalla.setScaleX((float) (x + 1));
                pantalla.setScaleY((float) (y + 1));




            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                pantalla.setScaleX((float) (x - 1));
                pantalla.setScaleY((float) (y - 1));



            }
        });


    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(Donacion_1.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla "+InformacionPantalla.getText().toString()+". Posteriormente, de clic en el botón Confirmar donación," +
                            "para validar los datos y realizar la donación. Caso contrario, clic en el botón regresar para volver" +
                            "a los detalles!.");
                    EditTextNumeroTarjeta.requestFocus();
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null,null);
        }else{
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
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
        String url ="http://192.168.0.4:8080/ProyectoIntegrador/nuevaDonacion.php";
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

    @Override
    protected void onPause() {
        super.onPause();

        myTTS.stop();
        myTTS.shutdown();
    }

    @Override
    protected void onStop() {
        super.onStop();

        myTTS.stop();
        myTTS.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        myTTS.stop();
        myTTS.shutdown();
    }
}


