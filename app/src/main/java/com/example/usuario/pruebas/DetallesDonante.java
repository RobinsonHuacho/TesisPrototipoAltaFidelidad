package com.example.usuario.pruebas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.content.Intent.EXTRA_REFERRER;
import static android.content.Intent.EXTRA_TITLE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class DetallesDonante extends AppCompatActivity {

    private ListView listView_Beneficiarios;
    private ArrayList<String> listViewItemsNombre = new ArrayList<String>();
    private ArrayList<String> listViewItemsCantidad = new ArrayList<String>();
    private ArrayList<String> listViewItemsPrecio = new ArrayList<String>();
    private ArrayList<String> listViewItemsImagen = new ArrayList<String>();

    DatabaseHandlerProductosComprados db = new DatabaseHandlerProductosComprados(this);

    private String selectedIdCompra,selectedIdUsuario,primer_nombre_usuario,primer_apellido_usuario;

    ListView listViewdetallesBeneficiario;
    ElementosProductosCompradosAdaptador adapter;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView InformacionPantalla;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;
    private TextView textView_NombresBeneficiario,
            textView_ApellidosBeneficiario,
            textView_TotalCompra,
            textView_SaldoCompra,
            textView_CondicionBeneficiario,
            Label_TotalCompra,
            Label_SaldoCompra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_donante);

        Intent intent = getIntent();
        selectedIdCompra=intent.getStringExtra(EXTRA_INDEX);
        selectedIdUsuario=intent.getStringExtra(EXTRA_MESSAGE);

        db.deleteProductos();

        for (int i=0;i<2;i++) {
            ExecTasks t = new ExecTasks(DetallesDonante.this);
            t.execute();
        }

        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        String url1 ="http://192.168.0.10:8080/ProyectoIntegrador/detalleBeneficiario_totales_donador.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObj1 = new JSONObject(response.toString());
                    JSONArray contacts1 = jsonObj1.getJSONArray("detalleTotal");
                    for (int i = 0; i < contacts1.length(); i++) {
                        JSONObject c1 = contacts1.getJSONObject(i);

                        String total_compra = c1.getString("TOTAL_COMPRA");
                        String saldo_compra = c1.getString("SALDO_COMPRA");
                        //Toast.makeText(getApplicationContext(),total_compra+" "+saldo_compra,Toast.LENGTH_LONG).show();

                        TextView textView_TotalCompra = (TextView) findViewById(R.id.textView_CostoTotal);
                        textView_TotalCompra.setText(total_compra);

                        TextView textViewxt_SaldoCompra = (TextView) findViewById(R.id.textView_SaldoCompra);
                        textViewxt_SaldoCompra.setText(saldo_compra);
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
                params.put("id_compra", selectedIdCompra);
                params.put("id_usuario", selectedIdUsuario);
                return params;
            }
        };
        queue1.add(stringRequest1);

        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        String url2 ="http://192.168.0.10:8080/ProyectoIntegrador/detalleBeneficiario_usuario.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObj2 = new JSONObject(response.toString());
                    JSONArray contacts2 = jsonObj2.getJSONArray("usuarioCompra");
                    for (int i = 0; i < contacts2.length(); i++) {
                        JSONObject c2 = contacts2.getJSONObject(i);

                        primer_nombre_usuario = c2.getString("PRIMER_NOMBRE_USUARIO");
                        primer_apellido_usuario = c2.getString("PRIMER_APELLIDO_USUARIO");
                        String condicion_usuario = c2.getString("CONDICION_USUARIO");
                        String imagen_usuario = c2.getString("FOTO_USUARIO");
                        //Toast.makeText(getApplicationContext(),primer_nombre_usuario+" "+primer_apellido_usuario,Toast.LENGTH_LONG).show();

                        TextView textView_Nombres = (TextView) findViewById(R.id.TextView_NombreBeneficiario);
                        textView_Nombres.setText("NOMBRES:\n"+primer_nombre_usuario);

                        TextView textViewxt_Apellidos = (TextView) findViewById(R.id.TextView_ApellidoBeneficiario);
                        textViewxt_Apellidos.setText("APELLIDOS:\n"+primer_apellido_usuario);

                        TextView condicion = (TextView) findViewById(R.id.TextView_CondicionBeneficiario);
                        condicion.setText("CONDICIÓN:\n"+condicion_usuario);

                        SmartImageView imageView= (SmartImageView) findViewById(R.id.ImageView_Foto);
                        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
                        imageView.setImageUrl("http://192.168.0.10:8080/ProyectoIntegrador/Images/Beneficiarios/"+imagen_usuario,rect);
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
                params.put("id_usuario", selectedIdUsuario);
                return params;
            }
        };
        queue2.add(stringRequest2);

        InformacionPantalla=(TextView) findViewById(R.id.textView25);

        textView_NombresBeneficiario= (TextView) findViewById(R.id.TextView_NombreBeneficiario);
        textView_ApellidosBeneficiario= (TextView) findViewById(R.id.TextView_ApellidoBeneficiario);
        textView_CondicionBeneficiario= (TextView) findViewById(R.id.TextView_CondicionBeneficiario);
        textView_TotalCompra= (TextView) findViewById(R.id.textView_CostoTotal);
        textView_SaldoCompra= (TextView) findViewById(R.id.textView_SaldoCompra);
        Label_TotalCompra= (TextView) findViewById(R.id.textView26);
        Label_SaldoCompra= (TextView) findViewById(R.id.textView34);

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
                if(InformacionPantalla.getTextSize()<75) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() + 12.0f);
                    textView_NombresBeneficiario.setTextSize(0, textView_NombresBeneficiario.getTextSize() + 12.0f);
                    textView_ApellidosBeneficiario.setTextSize(0, textView_ApellidosBeneficiario.getTextSize() + 12.0f);
                    textView_CondicionBeneficiario.setTextSize(0, textView_CondicionBeneficiario.getTextSize() + 12.0f);
                    textView_TotalCompra.setTextSize(0, textView_TotalCompra.getTextSize() + 12.0f);
                    textView_SaldoCompra.setTextSize(0, textView_SaldoCompra.getTextSize() + 12.0f);
                    Label_TotalCompra.setTextSize(0, Label_TotalCompra.getTextSize() + 12.0f);
                    Label_SaldoCompra.setTextSize(0, Label_SaldoCompra.getTextSize() + 12.0f);

                    String[] arregloNombres = listViewItemsNombre.toArray(new String[0]);
                    String[] arregloCantidades = listViewItemsCantidad.toArray(new String[0]);
                    String[] arregloTotales = listViewItemsPrecio.toArray(new String[0]);
                    String[] arregloImagenes = listViewItemsImagen.toArray(new String[0]);

                    //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
                    adapter = new ElementosProductosCompradosAdaptador(DetallesDonante.this, arregloNombres, arregloCantidades, arregloTotales, "Productos", arregloImagenes,22);
                    listViewdetallesBeneficiario.setAdapter(adapter);
                }



            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()>66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);
                    textView_NombresBeneficiario.setTextSize(0, textView_NombresBeneficiario.getTextSize() - 12.0f);
                    textView_ApellidosBeneficiario.setTextSize(0, textView_ApellidosBeneficiario.getTextSize() - 12.0f);
                    textView_CondicionBeneficiario.setTextSize(0, textView_CondicionBeneficiario.getTextSize() - 12.0f);
                    textView_TotalCompra.setTextSize(0, textView_TotalCompra.getTextSize() - 12.0f);
                    textView_SaldoCompra.setTextSize(0, textView_SaldoCompra.getTextSize() - 12.0f);
                    Label_TotalCompra.setTextSize(0, Label_TotalCompra.getTextSize() - 12.0f);
                    Label_SaldoCompra.setTextSize(0, Label_SaldoCompra.getTextSize() - 12.0f);

                    String[] arregloNombres = listViewItemsNombre.toArray(new String[0]);
                    String[] arregloCantidades = listViewItemsCantidad.toArray(new String[0]);
                    String[] arregloTotales = listViewItemsPrecio.toArray(new String[0]);
                    String[] arregloImagenes = listViewItemsImagen.toArray(new String[0]);

                    //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
                    adapter = new ElementosProductosCompradosAdaptador(DetallesDonante.this, arregloNombres, arregloCantidades, arregloTotales, "Productos", arregloImagenes,18);
                    listViewdetallesBeneficiario.setAdapter(adapter);


                }


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

        if(escuchado.indexOf("donar")!=-1){
            Intent intent = new Intent(this,Donacion.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("regresar")!=-1){
                Intent intent = new Intent(this,ListaBeneficiarioDonacion.class);
                startActivity(intent);

            }
        }


    }



    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(DetallesDonante.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak(InformacionPantalla.getText().toString()+ "por el beneficiario: "+
                            textView_NombresBeneficiario.getText().toString().substring(9, textView_NombresBeneficiario.getText().toString().length())+
                            " "+textView_ApellidosBeneficiario.getText().toString().substring(11,textView_ApellidosBeneficiario.getText().toString().length())+". El costo total de los productos sería de: "
                            +textView_TotalCompra.getText().toString()+" dólares. El saldo de la compra es de: "
                                    +textView_TotalCompra.getText().toString()+" dólares. Si está de acuerdo, toque la pantalla" +
                            " y pronuncie la palabra, donar para proceder a ingresar los datos de la tarjeta de crédito, caso contrario" +
                            ", la palabra regresar para bisualizar la lista de beneficiarios.");
                    //deberá ingresar su usuario y contraseña. " +
                    //      "En el caso de no tener, deberá registrarse pronunciando la palabra, registrar, ." +
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



    public class ExecTasks extends AsyncTask<Void, Void, ArrayAdapter<String>> {

        Context context;
        ProgressDialog pDialog;

        public ExecTasks(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando Lista");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected ArrayAdapter<String> doInBackground(Void... params) {

            try {
                Thread.sleep(2000);


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="http://192.168.0.10:8080/ProyectoIntegrador/detalleBeneficiario_donador.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new JSONObject(response.toString());
                        JSONArray contacts = jsonObj.getJSONArray("detalleProducto");
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String id_detalle_compra = c.getString("ID_DETALLE_COMPRA");
                            String id_producto = c.getString("ID_PRODUCTO");
                            String id_usuario = c.getString("ID_USUARIO");
                            String precio_detalle = c.getString("PRECIO_DETALLE");
                            String cantidad_detalle = c.getString("CANTIDAD_DETALLE");
                            String total_detalle = c.getString("TOTAL_DETALLE");
                            String estado = c.getString("ESTADO");
                            String imagen_producto = c.getString("IMAGEN_PRODUCTO");
                            String nombre_producto = c.getString("NOMBRE_PRODUCTO");

                            db.addProductosDetalle(new ElementoProductosComprados(id_detalle_compra,id_producto,id_usuario,precio_detalle,cantidad_detalle,total_detalle,estado,imagen_producto,nombre_producto));

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
                    params.put("id_compra", selectedIdCompra);
                    params.put("id_usuario", selectedIdUsuario);
                    return params;
                }
            };
            queue.add(stringRequest);

            listViewItemsNombre=db.getAllProductosComprados(8);
            listViewItemsCantidad=db.getAllProductosComprados(4);
            listViewItemsPrecio=db.getAllProductosComprados(5);
            listViewItemsImagen=db.getAllProductosComprados(7 );

            String []arregloNombres=listViewItemsNombre.toArray(new String[0]);
            String []arregloCantidades=listViewItemsCantidad.toArray(new String[0]);
            String []arregloTotales=listViewItemsPrecio.toArray(new String[0]);
            String []arregloImagenes=listViewItemsImagen.toArray(new String[0]);

            //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
            adapter = new ElementosProductosCompradosAdaptador(DetallesDonante.this, arregloNombres,arregloCantidades,arregloTotales, "Productos", arregloImagenes,18);
            listViewdetallesBeneficiario = (ListView) findViewById(R.id.ListView_ListaProductosBeneficiario);

            return adapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
            super.onPostExecute(stringArrayAdapter);

            listViewdetallesBeneficiario.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }
    }

    public void irADonacion (View view)
    {

        Intent intent1 = new Intent(getApplicationContext(), Donacion.class);

        final Intent intent = getIntent();
        final String messageIdCompra = intent.getStringExtra(EXTRA_INDEX);
        final String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);

        intent1.putExtra(Intent.EXTRA_INDEX, messageIdCompra);
        intent1.putExtra(EXTRA_MESSAGE, messageIdUsuario);
        intent1.putExtra(EXTRA_TITLE, primer_nombre_usuario);
        intent1.putExtra(EXTRA_REFERRER, primer_apellido_usuario);

        //Toast.makeText(getApplicationContext(),messageIdCompra+" "+messageIdUsuario, Toast.LENGTH_LONG).show();

        startActivity(intent1);
    }

    public void regresarListaBeneficiarios (View view)
    {
        Intent intent1 = new Intent(getApplicationContext(), ListaBeneficiarioDonacion.class);

        startActivity(intent1);
    }











}
