package com.example.usuario.pruebas;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AnadirDetalleCompra extends AppCompatActivity {

    private TextView textViewPrecio;
    private TextView textViewNombreProducto;
    private TextView textViewDescripcionProducto;
    private EditText editTextCantidad;
    private TextView textViewSubtotal;
    private Button incrementarCantidad;
    private Button disminuirCantidad;
    private int contadorCantidad=1;
    private String contadorCompra;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;


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

        textViewDescripcionProducto = (TextView) findViewById(R.id.TextView_Descripcion);
        textViewDescripcionProducto.setText(messageDescripcion);

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


        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la cantidad de producto que desea comprar!");
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);


            }
        });






        iniciarTextToSpeech();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            editTextCantidad.setText(escuchado);
            textViewSubtotal.setText(String.valueOf(Float.parseFloat(editTextCantidad.getText().toString())*Float.parseFloat(textViewPrecio.getText().toString())));
            speak("El valor total por el producto sería de:"+textViewSubtotal.getText().toString());

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
                    speak("En esta pantalla");
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

    public void makeRequestInsertarProducto(View view){

        final Intent intent = getIntent();
        final String messageId = intent.getStringExtra(EXTRA_INDEX);
        //Toast.makeText(getApplicationContext(),"ID_PRODUCTO: "+messageId,Toast.LENGTH_LONG).show();

        final String messageIdUsuario=IngresoAplicacion.getActivityInstance().getIdUsuario();
        //Toast.makeText(getApplicationContext(), "ID_USUARIO: "+messageIdUsuario, Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.8:8080/ProyectoIntegrador/nuevoDetalle.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.matches("1")){
                            Toast.makeText(getApplicationContext(),"Producto Ingresado Exitosamente",Toast.LENGTH_LONG).show();

                            Intent intent1 = new Intent(AnadirDetalleCompra.this, ConfirmacionAnadirProducto.class);
                            startActivity(intent1);

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

    public void volverCatalogo(View view){
        Intent intent = new Intent(this, ListaProductos.class);
        startActivity(intent);
    }


}

