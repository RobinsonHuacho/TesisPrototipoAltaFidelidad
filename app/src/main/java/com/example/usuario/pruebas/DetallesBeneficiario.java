package com.example.usuario.pruebas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetallesBeneficiario extends AppCompatActivity {


    private ListView listViewdetallesBeneficiario;
    private ArrayList<String> listViewItemsNombre = new ArrayList<String>();
    private ArrayList<String> listViewItemsCantidad = new ArrayList<String>();
    private ArrayList<String> listViewItemsPrecio = new ArrayList<String>();
    private ArrayList<String> listViewItemsImagen = new ArrayList<String>();
    private int numeroComprasCliente;

    TextView textView_TotalCompra,textView_SaldoCompra;
    ElementosProductosCompradosAdaptador adapter;

    DatabaseHandlerProductosComprados db = new DatabaseHandlerProductosComprados(this);


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView InformacionPantalla;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_beneficiario);


        db.deleteProductos();

        for (int i=0;i<2;i++) {
            ExecTasks t = new ExecTasks(DetallesBeneficiario.this);
            t.execute();
            conteoRegistros();
        }

        InformacionPantalla=(TextView) findViewById(R.id.textView25);

        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        String url1 ="http://192.168.0.4:8080/ProyectoIntegrador/detalleBeneficiario_totales.php";
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
                        String saldo_compra = c1.getString("SALDO");
                        //Toast.makeText(getApplicationContext(),total_compra+" "+saldo_compra,Toast.LENGTH_LONG).show();
                        Float total_compra_float,saldo_compra_float;

                        total_compra_float=Float.parseFloat(total_compra);
                        saldo_compra_float=Float.parseFloat(saldo_compra);


                        textView_TotalCompra= (TextView) findViewById(R.id.textView_CostoTotal);
                        textView_TotalCompra.setText(String.format("%.02f", total_compra_float));
                        //Toast.makeText(getApplcationContext(),textView_TotalCompra.getText(),Toast.LENGTH_LONG).show();

                        textView_SaldoCompra= (TextView) findViewById(R.id.textView_SaldoCompra);
                        textView_SaldoCompra.setText(String.format("%.02f", saldo_compra_float));
                        //Toast.makeText(getApplicationContext(),textView_SaldoCompra.getText(),Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
        queue1.add(stringRequest1);



        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Finalizar o Continuar!");
                //mySpeechRecognizer.startListening(speechIntent);
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);
            }
        });

        iniciarTextToSpeech();

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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myTTS!=null){
            myTTS.stop();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myTTS!=null){
            myTTS.stop();
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

        escuchado = escuchado.toLowerCase();

        if(escuchado.indexOf("finalizar")!=-1){
            if(conteoRegistros()<2) {
                //Toast.makeText(getApplicationContext(), String.valueOf(conteoRegistros())+IngresoAplicacion.getActivityInstance().getIdUsuario(), Toast.LENGTH_LONG).show();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url3 = "http://192.168.0.4:8080/ProyectoIntegrador/nuevaCompra.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new
                        Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                if (response.matches("1")) {
                                    Toast.makeText(getApplicationContext(), "Compra Realizada Exitosamente", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), ConfirmacionCompra.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Algo salió mal. No se pudo realizar la compra. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        textView_TotalCompra = (TextView) findViewById(R.id.textView_CostoTotal);
                        textView_SaldoCompra = (TextView) findViewById(R.id.textView_SaldoCompra);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_usuario", IngresoAplicacion.getActivityInstance().getIdUsuario());
                        params.put("total_compra", textView_TotalCompra.getText().toString());
                        params.put("saldo_compra", textView_SaldoCompra.getText().toString());

                        return params;
                    }
                };
                queue.add(stringRequest);

            }else {
                Toast.makeText(getApplicationContext(), "Algo salió mal. No se pudo realizar la compra. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
            }

        }else {
            if (escuchado.indexOf("continuar") != -1) {
                Intent intent = new Intent(this, ListaCategoriaProducto.class);
                startActivity(intent);


            } else{
                if (escuchado.indexOf("añadir") != -1) {
                    Intent intent = new Intent(this, ListaCategoriaProducto.class);
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
                    Toast.makeText(DetallesBeneficiario.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak(InformacionPantalla.getText().toString()+". El costo total de los productos sería de: "
                            +textView_TotalCompra.getText().toString()+" dólares. Si está de acuerdo, toque la pantalla" +
                            " y pronuncie la palabra, finalizar para finalizar la solicitud; caso contario" +
                            ", la palabra continuar para añadir más productos.");

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


    public void realizarCompra(View view){
        if(conteoRegistros()<2) {
            Toast.makeText(getApplicationContext(), String.valueOf(conteoRegistros())+IngresoAplicacion.getActivityInstance().getIdUsuario(), Toast.LENGTH_LONG).show();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url3 = "http://192.168.0.4:8080/ProyectoIntegrador/nuevaCompra.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new
                    Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if (response.matches("1")) {
                                Toast.makeText(getApplicationContext(), "Compra Realizada Exitosamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ConfirmacionCompra.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "Algo salió mal. No se pudo realizar la compra. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ErrorConfirmacionCompra.class);
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
                    textView_TotalCompra = (TextView) findViewById(R.id.textView_CostoTotal);
                    textView_SaldoCompra = (TextView) findViewById(R.id.textView_SaldoCompra);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_usuario", IngresoAplicacion.getActivityInstance().getIdUsuario());
                    params.put("total_compra", textView_TotalCompra.getText().toString());
                    params.put("saldo_compra", textView_SaldoCompra.getText().toString());

                    return params;
                }
            };
            queue.add(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(), "Algo salió mal. No se pudo realizar la compra. Inténtalo de nuevo", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ErrorConfirmacionCompra.class);
            startActivity(intent);
        }

    }


    public int conteoRegistros(){

        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        String url2 ="http://192.168.0.4:8080/ProyectoIntegrador/nuevaCompra_numeroComprasPorCliente.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
        //        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObj2 = new JSONObject(response.toString());
                    JSONArray contacts2 = jsonObj2.getJSONArray("numeroCompras");
                    for (int i = 0; i < contacts2.length(); i++) {
                        JSONObject c2 = contacts2.getJSONObject(i);

                        numeroComprasCliente= Integer.parseInt(c2.getString("CONTEO"));
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
                return params;
            }
        };
        queue2.add(stringRequest2);

        return numeroComprasCliente;
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
            String url = "http://192.168.0.4:8080/ProyectoIntegrador/detalleBeneficiario.php";
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


                            db.addProductosDetalle(new ProductosComprados(id_detalle_compra, id_producto, id_usuario, precio_detalle, cantidad_detalle, total_detalle, estado, imagen_producto, nombre_producto));

                        }

                    } catch (final JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //mTextView.setText("That didn't work!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_usuario", IngresoAplicacion.getActivityInstance().getIdUsuario());
                    return params;
                }
            };
            queue.add(stringRequest);

            listViewItemsNombre = db.getAllProductosComprados(8);
            listViewItemsCantidad = db.getAllProductosComprados(4);
            listViewItemsPrecio = db.getAllProductosComprados(5);
            listViewItemsImagen = db.getAllProductosComprados(7);

            String[] arregloNombres = listViewItemsNombre.toArray(new String[0]);
            String[] arregloCantidades = listViewItemsCantidad.toArray(new String[0]);
            String[] arregloTotales = listViewItemsPrecio.toArray(new String[0]);
            String[] arregloImagenes = listViewItemsImagen.toArray(new String[0]);

            //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
           adapter = new ElementosProductosCompradosAdaptador(DetallesBeneficiario.this, arregloNombres, arregloCantidades, arregloTotales, "Productos", arregloImagenes);
           listViewdetallesBeneficiario = (ListView) findViewById(R.id.ListView_ProductosCompradosBeneficiario);
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


    public void volverCatalogo(View view){
        Intent intent = new Intent(this, ListaCategoriaProducto.class);
        startActivity(intent);
    }
}
