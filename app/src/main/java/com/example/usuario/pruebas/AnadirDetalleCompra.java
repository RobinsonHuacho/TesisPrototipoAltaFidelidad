package com.example.usuario.pruebas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
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
import com.github.snowdream.android.widget.SmartImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.content.Intent.EXTRA_REFERRER;
import static android.content.Intent.EXTRA_TITLE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AnadirDetalleCompra extends AppCompatActivity {

    private TextView InformacionPantalla;
    private TextView textViewPrecio, LabelPrecio,LabelCantidad;
    private TextView textViewNombreProducto,LabelNombreProducto;
    private EditText editTextCantidad;
    private TextView textViewSubtotal,LabelSubtotal;
    private Button incrementarCantidad,disminuirCantidad,butonAnadir,butonVolver;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar,ImageButtonDesactivar;
    private int contadorCantidad=1;
    private String messageIdCategoriaSeleecionada;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer,
            mySpeechRecognizerResultadoFinal;

    DatabaseHandlerDetalleCompra db = new DatabaseHandlerDetalleCompra(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_detalle_compra);

        db.deleteProductos();

        for (int i=0;i<2;i++){
         ExecTasks t = new ExecTasks(AnadirDetalleCompra.this);
            t.execute();
        }

        Intent intent = getIntent();
        messageIdCategoriaSeleecionada = intent.getStringExtra(EXTRA_REFERRER);
        String messageNombre = intent.getStringExtra(EXTRA_MESSAGE);
        String messageImagen = intent.getStringExtra(EXTRA_TITLE);
        String messagePrecio=intent.getStringExtra(Intent.EXTRA_TEXT);

        //Toast.makeText(getApplicationContext(),messageIdCategoriaSeleecionada, Toast.LENGTH_LONG).show();



        LabelNombreProducto = (TextView) findViewById(R.id.textView9);
        textViewNombreProducto = (TextView) findViewById(R.id.TextView_NombreProducto);
        textViewNombreProducto.setText(messageNombre);

        LabelPrecio = (TextView) findViewById(R.id.textView11);
        textViewPrecio = (TextView) findViewById(R.id.TextView_PrecioProducto);
        textViewPrecio.setText(messagePrecio);

        LabelSubtotal = (TextView) findViewById(R.id.textView12);
        textViewSubtotal=(TextView) findViewById(R.id.TextView_Subtotal);
        textViewSubtotal.setText(messagePrecio);

        SmartImageView imageView= (SmartImageView) findViewById(R.id.ImageView_Foto);
        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
        imageView.setImageUrl("http://192.168.0.10:8080/ProyectoIntegrador/Images/Productos/"+messageImagen, rect);


        LabelCantidad = (TextView) findViewById(R.id.textView10);
        editTextCantidad = (EditText) findViewById(R.id.Label_SaldoCompra);
        editTextCantidad.setText(String.valueOf(contadorCantidad));


        incrementarCantidad = (Button) findViewById(R.id.buttonIncrementar);
        disminuirCantidad = (Button) findViewById(R.id.buttonDisminuir);



        butonAnadir = (Button) findViewById(R.id.buttonAnadir);
        butonVolver = (Button) findViewById(R.id.buttonCancelar);


        incrementarCantidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(contadorCantidad<2) {
                    contadorCantidad++;
                    editTextCantidad.setText(String.valueOf(contadorCantidad));
                    textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString()) * Float.parseFloat(textViewPrecio.getText().toString())));
                }
            }
        });

        disminuirCantidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(contadorCantidad>1) {
                    contadorCantidad--;
                    editTextCantidad.setText(String.valueOf(contadorCantidad));
                    textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString()) * Float.parseFloat(textViewPrecio.getText().toString())));
                }
            }
        });

        InformacionPantalla=(TextView) findViewById(R.id.textView8);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);



        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()<90) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() + 12.0f);
                    LabelNombreProducto.setTextSize(0, textViewNombreProducto.getTextSize() + 12.0f);
                    textViewNombreProducto.setTextSize(0, textViewNombreProducto.getTextSize() + 12.0f);
                    LabelPrecio.setTextSize(0, LabelPrecio.getTextSize() + 12.0f);
                    textViewPrecio.setTextSize(0, textViewPrecio.getTextSize() + 12.0f);
                    LabelSubtotal.setTextSize(0, LabelSubtotal.getTextSize() + 12.0f);
                    textViewSubtotal.setTextSize(0, textViewSubtotal.getTextSize() + 12.0f);
                    LabelCantidad.setTextSize(0, LabelCantidad.getTextSize() + 12.0f);
                    editTextCantidad.setTextSize(0, editTextCantidad.getTextSize() + 12.0f);
                    butonAnadir.setTextSize(0, butonAnadir.getTextSize() + 12.0f);
                    butonVolver.setTextSize(0, butonVolver.getTextSize() + 12.0f);

                }




            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()>66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);
                    LabelNombreProducto.setTextSize(0, textViewNombreProducto.getTextSize() - 12.0f);
                    textViewNombreProducto.setTextSize(0, textViewNombreProducto.getTextSize() - 12.0f);
                    LabelPrecio.setTextSize(0, LabelPrecio.getTextSize() - 12.0f);
                    textViewPrecio.setTextSize(0, textViewPrecio.getTextSize() - 12.0f);
                    LabelSubtotal.setTextSize(0, LabelSubtotal.getTextSize() - 12.0f);
                    textViewSubtotal.setTextSize(0, textViewSubtotal.getTextSize() - 12.0f);
                    LabelCantidad.setTextSize(0, LabelCantidad.getTextSize() - 12.0f);
                    editTextCantidad.setTextSize(0, editTextCantidad.getTextSize() - 12.0f);
                    butonAnadir.setTextSize(0, butonAnadir.getTextSize() - 12.0f);
                    butonVolver.setTextSize(0, butonVolver.getTextSize() - 12.0f);

                }



            }
        });

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;
        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();
                iniciarSpeechToText();
                iniciarSpeechToTextResultadoFinal();

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

                ImageButtonActivar.setVisibility(View.GONE);
                ImageButtonDesactivar.setVisibility(View.VISIBLE);

            }
        });

        ImageButtonDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myTTS.stop();

                pantalla.setOnClickListener(null);

                ImageButtonActivar.setVisibility(View.VISIBLE);
                ImageButtonDesactivar.setVisibility(View.GONE);
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
                    if(Integer.parseInt(editTextCantidad.getText().toString())>=1 && Integer.parseInt(editTextCantidad.getText().toString())<=6) {
                        textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString()) * Float.parseFloat(textViewPrecio.getText().toString())));
                        speak("El valor total por el producto sería de:" + textViewSubtotal.getText().toString() + " dólares. Si está de acuerdo, " +
                                "pronuncie la palabra añadir, caso contrario pronuncie la palabra repetir, para ingresar la cantidad de producto" +
                                "nuevamente. O volver para seleccionar otro producto");
                    }else{
                        speak("Cantidad no válida! Usted puede adquirir hasta un máximo de 6 unidades por producto! Por favor, toque la pantalla " +
                                " y pronuncie la palabra repetir, posteriomente la cantidad de producto nuevamente!");
                    }

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
        final Intent intent1 = getIntent();
        final String messageId1 = intent1.getStringExtra(EXTRA_INDEX);


            if (escuchado.indexOf("añadir") != -1) {
              // if(db.conteoRegistros(IngresoAplicacion.getActivityInstance().getIdUsuario(),messageId1)==0 && Integer.parseInt(textViewSubtotal.getText().toString())<30) {



                final Intent intent = getIntent();
                final String messageId = intent.getStringExtra(EXTRA_INDEX);
                //Toast.makeText(getApplicationContext(),"ID_PRODUCTO: "+messageId,Toast.LENGTH_LONG).show();

                final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();
                //Toast.makeText(getApplicationContext(), "ID_USUARIO: "+messageIdUsuario, Toast.LENGTH_LONG).show();

                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://192.168.0.10:8080/ProyectoIntegrador/nuevoDetalle.php";
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
                                        Intent intent = new Intent(getApplicationContext(), ErrorAnadirProducto.class);
                                        startActivity(intent);

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
            /*else {
                    Toast.makeText(getApplicationContext(), "Algo salio mal. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ErrorAnadirProducto.class);
                    startActivity(intent);

                }*/

             }else {
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
                    intent.putExtra(EXTRA_INDEX,messageIdCategoriaSeleecionada);
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
                            "Toque la pantalla y pronuncie la cantidad . La cantidad puede ir desde 1 a 2 unidades" +
                            " por producto!");

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


        final Intent intent1 = getIntent();
        final String messageId1 = intent1.getStringExtra(EXTRA_INDEX);


       // if(db.conteoRegistros(IngresoAplicacion.getActivityInstance().getIdUsuario(),messageId1)==0 && Integer.parseInt(textViewSubtotal.getText().toString())<30) {


        final Intent intent = getIntent();
        final String messageId = intent.getStringExtra(EXTRA_INDEX);


        final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();
        //Toast.makeText(getApplicationContext(), "ID_USUARIO: "+messageIdUsuario, Toast.LENGTH_LONG).show();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.0.10:8080/ProyectoIntegrador/nuevoDetalle.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new
                    Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.matches("1")) {
                                //Toast.makeText(getApplicationContext(), "Producto Ingresado Exitosamente", Toast.LENGTH_LONG).show();

                                Intent intent1 = new Intent(AnadirDetalleCompra.this, ConfirmacionAnadirProducto.class);
                                startActivity(intent1);

                            } else {
                                Toast.makeText(getApplicationContext(), "Algo salio mal. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ErrorAnadirProducto.class);
                                startActivity(intent);
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

        /*}else {
            final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();

            Toast.makeText(getApplicationContext(), "Algo salió mal. No se pudo realizar la compra. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ErrorAnadirProducto.class);
            startActivity(intent);
        }*/



    }
    public void volverCatalogo(View view){
        Intent intent = new Intent(this, ListaProductos.class);
        intent.putExtra(EXTRA_INDEX,messageIdCategoriaSeleecionada);
        startActivity(intent);
    }





    public class ExecTasks extends AsyncTask<Void, Void, Void> {

        Context context;
        ProgressDialog pDialog;

        public ExecTasks(Context context){
            this.context = context;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            db.deleteProductos();


            RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
            String url2 = "http://192.168.0.10:8080/ProyectoIntegrador/DetalleCompra.php";
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj2 = new JSONObject(response.toString());
                        JSONArray contacts2 = jsonObj2.getJSONArray("compras");
                        for (int i = 0; i < contacts2.length(); i++) {
                            JSONObject c2 = contacts2.getJSONObject(i);

                            String _idDetalle = c2.getString("ID_DETALLE_COMPRA");
                            String _idCompra = c2.getString("ID_COMPRA");
                            String _idProducto = c2.getString("ID_PRODUCTO");
                            String _idUsuario = c2.getString("ID_USUARIO");
                            String _precioVenta = c2.getString("PRECIO_DETALLE");
                            String _cantidadDetalle = c2.getString("CANTIDAD_DETALLE");
                            String _totalDetalle = c2.getString("TOTAL_DETALLE");
                            String _estado = c2.getString("TOTAL_DETALLE");

                            //Toast.makeText(getApplicationContext(),numeroProductoCliente,Toast.LENGTH_LONG).show();
                            db = new DatabaseHandlerDetalleCompra(getApplicationContext());
                            db.addCompras(new ElementoDetalleCompra(_idDetalle,_idCompra,_idProducto,_idUsuario,_precioVenta,_cantidadDetalle,_totalDetalle,_estado));
                            //Toast.makeText(getApplicationContext(),String.valueOf(db.conteoRegistros()),Toast.LENGTH_LONG).show();
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
                    });
            queue2.add(stringRequest2);

        }


        @Override
        protected Void doInBackground(Void... params) {

            try{
                Thread.sleep(2000);

            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


}

