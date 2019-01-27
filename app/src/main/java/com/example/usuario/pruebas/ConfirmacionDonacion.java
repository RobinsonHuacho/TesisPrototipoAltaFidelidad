package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ConfirmacionDonacion extends AppCompatActivity {

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    private TextView TextViewTitulo,TextViewContenido,TextViewTransaccion,TextViewEstado,TextViewMonto;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_donacion);

        getSupportActionBar().hide();

        TextViewContenido = (TextView) findViewById(R.id.TextViewInformacion);
        TextViewTransaccion = (TextView) findViewById(R.id.TextViewTransaccion);
        TextViewEstado = (TextView) findViewById(R.id.TextViewEstado);
        TextViewMonto = (TextView) findViewById(R.id.TextViewMonto);

        Intent intent = getIntent();
        try{
            JSONObject jo = new JSONObject(intent.getStringExtra("PaymentDetails"));

            final String messageIdCompra = intent.getStringExtra(EXTRA_INDEX);
            final String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);
            final String messageMonto = intent.getStringExtra("PaymentAmount");
            //Toast.makeText(getApplicationContext(),messageMonto,Toast.LENGTH_LONG).show();

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
                    params.put("monto", messageMonto);



                    return params;
                }
            };
            queue.add(stringRequest);


            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            String url1 ="http://192.168.0.4:8080/ProyectoIntegrador/ActualizarDetalleBeneficiario.php";
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), messageId+" "+textViewPrecio.getText().toString()+" "+editTextCantidad.getText().toString()+" "+textViewSubtotal.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue1.add(stringRequest);

            verDetalles(jo.getJSONObject("response"), messageMonto);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;

        ImageButtonActivar.setVisibility(View.VISIBLE);
        ImageButtonDesactivar.setVisibility(View.GONE);

        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();

                pantalla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Donar o regresar!");
                        //mySpeechRecognizer.startListening(speechIntent);
                        startActivityForResult(speechIntent,RECONOCEDOR_VOZ);
                    }
                });

                ImageButtonActivar.setVisibility(View.GONE);
                ImageButtonDesactivar.setVisibility(View.VISIBLE);
            }
        });

        ImageButtonDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myTTS.stop();
                myTTS.shutdown();

                pantalla.setOnClickListener(null);

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

    private void verDetalles(JSONObject response, String pyymentAmount) {

        try{


            TextViewTransaccion.setText(response.getString("id"));
            TextViewEstado.setText(response.getString("state").toUpperCase());
            TextViewMonto.setText("$: " + response.getString("PaymentAmount"));

            Toast.makeText(ConfirmacionDonacion.this,response.toString(),Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(myTTS!=null){
            myTTS.stop();
            myTTS.shutdown();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myTTS!=null){
            myTTS.stop();
            myTTS.shutdown();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myTTS!=null){
            myTTS.stop();
            myTTS.shutdown();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            prepararRespuesta(escuchado);



        }

    }

    private void prepararRespuesta(String escuchado) {

        if(escuchado.indexOf("continuat")!=-1){
            Intent intent = new Intent(this,ListaBeneficiarioDonacion.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("salir")!=-1){
                finish();
                moveTaskToBack(true);

            }
        }


    }



    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ConfirmacionDonacion.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak(TextViewContenido.getText().toString()+". Pronuncie la palabra, continuar para regresar a la lista de beneficiarios" +
                            " y continuar donando. Caso contrario, pronuncie la palabra, salir. Posterior a esto saldrá de la aplicación automáticamente.");
                    //deberá ingresar su usuario y contraseña. " +
                    //      "En el caso de no tener, deberá registrarse pronunciando la palabra, registrar, después del tono." +
                    //    "Caso contario, presionar sobre cualquier parte de la pantalla ");
                    //speak(TextViewTitulo.getText().toString());
                    //speak(TextViewContenido.getText().toString());
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

    public void regresarListaBeneficiarios(View view)
    {
        Intent intent = new Intent(this,ListaBeneficiarioDonacion.class);
        startActivity(intent);
    }

    public void salirAplicacion(View view)
    {
        finish();
        moveTaskToBack(true);
    }
}
