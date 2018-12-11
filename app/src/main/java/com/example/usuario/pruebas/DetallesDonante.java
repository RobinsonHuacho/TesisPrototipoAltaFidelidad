package com.example.usuario.pruebas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class DetallesDonante extends AppCompatActivity {

    private ListView listView_Beneficiarios;
    private ArrayList<String> listViewItemsNombre = new ArrayList<String>();
    private ArrayList<String> listViewItemsCantidad = new ArrayList<String>();
    private ArrayList<String> listViewItemsPrecio = new ArrayList<String>();
    private ArrayList<String> listViewItemsImagen = new ArrayList<String>();

    DatabaseHandlerProductosComprados db = new DatabaseHandlerProductosComprados(this);

    private String selectedIdCompra,selectedIdUsuario;

    ListView listViewdetallesBeneficiario;
    ElementosProductosCompradosAdaptador adapter;


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
        String url1 ="http://192.168.0.14:8080/ProyectoIntegrador/detalleBeneficiario_totales_donador.php";
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
        String url2 ="http://192.168.0.14:8080/ProyectoIntegrador/detalleBeneficiario_usuario.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObj2 = new JSONObject(response.toString());
                    JSONArray contacts2 = jsonObj2.getJSONArray("usuarioCompra");
                    for (int i = 0; i < contacts2.length(); i++) {
                        JSONObject c2 = contacts2.getJSONObject(i);

                        String primer_nombre_usuario = c2.getString("PRIMER_NOMBRE_USUARIO");
                        String segundo_nombre_usuario = c2.getString("SEGUNDO_NOMBRE_USUARIO");
                        String primer_apellido_usuario = c2.getString("PRIMER_APELLIDO_USUARIO");
                        String segundo_apellido_usuario = c2.getString("SEGUNDO_APELLIDO_USUARIO");
                        String condicion_usuario = c2.getString("CONDICION_USUARIO");
                        String imagen_usuario = c2.getString("FOTO_USUARIO");
                        //Toast.makeText(getApplicationContext(),primer_nombre_usuario+" "+primer_apellido_usuario,Toast.LENGTH_LONG).show();

                        TextView textView_Nombres = (TextView) findViewById(R.id.TextView_NombreBeneficiario);
                        textView_Nombres.setText("NOMBRES:\n"+primer_nombre_usuario+" "+segundo_nombre_usuario);

                        TextView textViewxt_Apellidos = (TextView) findViewById(R.id.TextView_ApellidoBeneficiario);
                        textViewxt_Apellidos.setText("APELLIDOS:\n"+primer_apellido_usuario+" "+segundo_apellido_usuario);

                        TextView condicion = (TextView) findViewById(R.id.TextView_CondicionBeneficiario);
                        condicion.setText("CONDICIÓN:\n"+condicion_usuario);

                        SmartImageView imageView= (SmartImageView) findViewById(R.id.ImageView_Foto);
                        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
                        imageView.setImageUrl("http://192.168.0.14:8080/ProyectoIntegrador/Images/Beneficiarios/"+imagen_usuario,rect);
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
            String url ="http://192.168.0.14:8080/ProyectoIntegrador/detalleBeneficiario_donador.php";
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

                            db.addProductosDetalle(new ProductosComprados(id_detalle_compra,id_producto,id_usuario,precio_detalle,cantidad_detalle,total_detalle,estado,imagen_producto,nombre_producto));

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
            adapter = new ElementosProductosCompradosAdaptador(DetallesDonante.this, arregloNombres,arregloCantidades,arregloTotales, "Productos", arregloImagenes);
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

        Toast.makeText(getApplicationContext(),messageIdCompra+" "+messageIdUsuario, Toast.LENGTH_LONG).show();

        startActivity(intent1);
    }

    public void regresarListaBeneficiarios (View view)
    {
        Intent intent1 = new Intent(getApplicationContext(), ListaBeneficiarioDonacion.class);

        startActivity(intent1);
    }











}
