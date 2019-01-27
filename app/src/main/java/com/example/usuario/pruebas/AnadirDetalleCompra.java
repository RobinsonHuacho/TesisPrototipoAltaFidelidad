package com.example.usuario.pruebas;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AnadirDetalleCompra extends AppCompatActivity {

    private TextView textViewPrecio;
    private TextView textViewNombreProducto;
    private EditText editTextCantidad;
    private TextView textViewSubtotal;
    private Button incrementarCantidad;
    private Button disminuirCantidad;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar;
    private int contadorCantidad=1;
    private int numeroProductoCliente;
    private String contadorCompra;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView InformacionPantalla;
    private SpeechRecognizer mySpeechRecognizer,
            mySpeechRecognizerResultadoFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_detalle_compra);

        Intent intent = getIntent();
        String messageNombre = intent.getStringExtra(EXTRA_MESSAGE);
        String messageDescripcion=intent.getStringExtra(Intent.EXTRA_TITLE);
        String messagePrecio=intent.getStringExtra(Intent.EXTRA_TEXT);

        //Toast.makeText(getApplicationContext(),messageNombre+" "+messageDescripcion+" "+messagePrecio, Toast.LENGTH_LONG).show();

        textViewNombreProducto = (TextView) findViewById(R.id.TextView_NombreProducto);
        textViewNombreProducto.setText(messageNombre);

        textViewPrecio = (TextView) findViewById(R.id.TextView_PrecioProducto);
        textViewPrecio.setText(messagePrecio);


        textViewSubtotal=(TextView) findViewById(R.id.TextView_Subtotal);
        textViewSubtotal.setText(messagePrecio);

        editTextCantidad = (EditText) findViewById(R.id.EditText_Cantidad);
        editTextCantidad.setText(String.valueOf(contadorCantidad));


        incrementarCantidad = (Button) findViewById(R.id.buttonIncrementar);
        disminuirCantidad = (Button) findViewById(R.id.buttonDisminuir);

        incrementarCantidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                contadorCantidad++;
                editTextCantidad.setText(String.valueOf(contadorCantidad));
                textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString())*Float.parseFloat(textViewPrecio.getText().toString())));
            }
        });

        disminuirCantidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                contadorCantidad--;
                editTextCantidad.setText(String.valueOf(contadorCantidad));
                textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString())*Float.parseFloat(textViewPrecio.getText().toString())));
            }
        });

        InformacionPantalla=(TextView) findViewById(R.id.textView8);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la cantidad de producto que desea comprar!");
                mySpeechRecognizer.startListening(speechIntent);



            }
        });

        iniciarTextToSpeech();
        iniciarSpeechToText();
        iniciarSpeechToTextResultadoFinal();

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

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myTTS.stop();

                iniciarTextToSpeech();

            }
        });
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


    private void iniciarSpeechToText() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String escuchado = result.get(0);
                    editTextCantidad.setText(escuchado);
                    textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString())*Float.parseFloat(textViewPrecio.getText().toString())));
                    speak("El valor total por el producto sería de:"+textViewSubtotal.getText().toString()+" dólares. Si está de acuerdo, " +
                            "pronuncie la palabra añadir, caso contrario pronuncie la palabra repetir, para ingresar la cantidad de producto" +
                            "nuevamente. O volver para seleccionar otro producto");

                    pantalla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                            speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Deletree sus credenciales!");
                            mySpeechRecognizerResultadoFinal.startListening(speechIntent);


                        }
                    });


                }


                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }


    private void iniciarSpeechToTextResultadoFinal() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizerResultadoFinal = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizerResultadoFinal.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String escuchado = result.get(0);
                    prepararRespuesta(escuchado);


                }



                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }


    private void prepararRespuesta(String escuchado) {

        escuchado = escuchado.toLowerCase();


            if (escuchado.indexOf("añadir") != -1) {

                final Intent intent = getIntent();
                final String messageId = intent.getStringExtra(EXTRA_INDEX);
                //Toast.makeText(getApplicationContext(),"ID_PRODUCTO: "+messageId,Toast.LENGTH_LONG).show();

                final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();
                //Toast.makeText(getApplicationContext(), "ID_USUARIO: "+messageIdUsuario, Toast.LENGTH_LONG).show();
                if(conteoProductoAniadido()<2) {
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://192.168.0.4:8080/ProyectoIntegrador/nuevoDetalle.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new
                            Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if (response.matches("1")) {
                                        Toast.makeText(getApplicationContext(), "Producto Ingresado Exitosamente", Toast.LENGTH_LONG).show();

                                        Intent intent1 = new Intent(AnadirDetalleCompra.this, ConfirmacionAnadirProducto.class);
                                        startActivity(intent1);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Algo salio mal. Inténtalo de nuevo", Toast.LENGTH_LONG).show();

                                        //   Intent intent1 = new Intent(AnadirDetalleCompra.this, ErrorAnadirProducto.class);
                                        //   startActivity(intent1);


                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id_producto", messageId);
                            params.put("id_usuario", messageIdUsuario);
                            params.put("cantidad", editTextCantidad.getText().toString());
                            params.put("precio", textViewPrecio.getText().toString());
                            params.put("subtotal", textViewSubtotal.getText().toString());


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }

            } else {
                if (escuchado.indexOf("repetir") != -1) {
                    speak("Pronuncie la cantidad nuevamente");

                    pantalla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                            speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Deletree sus credenciales!");
                            mySpeechRecognizer.startListening(speechIntent);


                        }
                    });

                }else
                {if (escuchado.indexOf("volver") != -1) {
                    Intent intent = new Intent(this, ListaProductos.class);
                    startActivity(intent);
                }
            }
        }
    }


    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(AnadirDetalleCompra.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla, por favor "+InformacionPantalla.getText().toString()+"." +
                            "Toque la pantalla y pronuncie la cantidad después del tono.");

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

    public void makeRequestInsertarProducto(View view){

        final Intent intent = getIntent();
        final String messageId = intent.getStringExtra(EXTRA_INDEX);
        //Toast.makeText(getApplicationContext(),"ID_PRODUCTO: "+messageId,Toast.LENGTH_LONG).show();

        final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();
        //Toast.makeText(getApplicationContext(), "ID_USUARIO: "+messageIdUsuario, Toast.LENGTH_LONG).show();
        if(conteoProductoAniadido()<2) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.0.4:8080/ProyectoIntegrador/nuevoDetalle.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new
                    Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.matches("1")) {
                                Toast.makeText(getApplicationContext(), "Producto Ingresado Exitosamente", Toast.LENGTH_LONG).show();

                                Intent intent1 = new Intent(AnadirDetalleCompra.this, ConfirmacionAnadirProducto.class);
                                startActivity(intent1);

                            } else {
                                Toast.makeText(getApplicationContext(), "Algo salio mal. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_producto", messageId);
                    params.put("id_usuario", messageIdUsuario);
                    params.put("cantidad", editTextCantidad.getText().toString());
                    params.put("precio", textViewPrecio.getText().toString());
                    params.put("subtotal", textViewSubtotal.getText().toString());


                    return params;
                }
            };
            queue.add(stringRequest);

        }


    }

    public void volverCatalogo(View view){
        Intent intent = new Intent(this, ListaProductos.class);
        startActivity(intent);
    }

    public int conteoProductoAniadido(){

        final Intent intent = getIntent();
        final String messageId = intent.getStringExtra(EXTRA_INDEX);

        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        String url2 ="http://192.168.0.4:8080/ProyectoIntegrador/nuevoDetalle_numeroProductoPorCliente.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObj2 = new JSONObject(response.toString());
                    JSONArray contacts2 = jsonObj2.getJSONArray("numeroProductos");
                    for (int i = 0; i < contacts2.length(); i++) {
                        JSONObject c2 = contacts2.getJSONObject(i);

                        numeroProductoCliente= Integer.parseInt(c2.getString("CONTEO"));
                    }

                }
                catch (final JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_usuario", IngresoAplicacion.getActivityInstance().getIdUsuario());
                params.put("id_producto", messageId);
                return params;
            }
        };
        queue2.add(stringRequest2);

        return numeroProductoCliente;
    }



}

