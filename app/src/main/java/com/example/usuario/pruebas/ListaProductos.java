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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
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

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ListaProductos extends AppCompatActivity {

    GridView gridView;
    public String idCategoriaProducto;
    private ArrayList<String> gridViewItemsID = new ArrayList<String>();
    private ArrayList<String> gridViewItemsNombre = new ArrayList<String>();
    private ArrayList<String> gridViewItemsDetalle = new ArrayList<String>();
    private ArrayList<String> gridViewItemsPrecio = new ArrayList<String>();
    private ArrayList<String> gridViewItemsImagen = new ArrayList<String>();

    private String selectedId, selectedItem, selectedDescription, selectedPrecio;
    String[] arregloID;
    ElementosProductosAdaptador adapter;
    DatabaseHandlerProducto db = new DatabaseHandlerProducto(this);
    private String item;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private GridView GridView_Productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        Intent intent = getIntent();
        idCategoriaProducto=intent.getStringExtra(EXTRA_INDEX);
        //Toast.makeText(getApplicationContext(),idCategoriaProducto, Toast.LENGTH_LONG).show();

        db.deleteProductos();

        for (int i=0;i<2;i++){
            ExecTasks t = new ExecTasks(ListaProductos.this);
            t.execute();
        }

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        GridView_Productos = (GridView) findViewById(R.id.GridView_Productos);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie el producto que desea comprar!");
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);


            }
        });


        /*GridView_Productos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie el producto que desea comprar!");
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);

            }
        });*/




        iniciarTextToSpeech();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);

            item=escuchado;
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {

        escuchado = escuchado.toUpperCase();

        TextView tv = (TextView) findViewById(R.id.TextView_Nombre);
        TextView tv1 = (TextView) findViewById(R.id.TextView_Descripcion);
        TextView tv2 = (TextView) findViewById(R.id.TextView_Precio);

        selectedId = db.getIdProductoPorNombre(escuchado).toString();
        selectedItem = tv.getText().toString();
        selectedDescription = tv1.getText().toString();
        selectedPrecio = tv2.getText().toString();

        Intent intent = new Intent(getApplicationContext(), AnadirDetalleCompra.class);

        intent.putExtra(EXTRA_INDEX, selectedId);
        intent.putExtra(EXTRA_MESSAGE, selectedItem);
        intent.putExtra(Intent.EXTRA_TITLE, selectedDescription);
        intent.putExtra(Intent.EXTRA_TEXT, selectedPrecio);

        Toast.makeText(getApplicationContext(),selectedId+" "+selectedItem+" "+selectedDescription+" "+selectedPrecio, Toast.LENGTH_LONG).show();

        startActivity(intent);

    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ListaProductos.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
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

    public class ExecTasks extends AsyncTask<Void, Void, ArrayAdapter<String>> {

        Context context;
        ProgressDialog pDialog;

        public ExecTasks(Context context){
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

            try{
                Thread.sleep(2000);


            }catch(Exception ex){
                ex.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url2 ="http://192.168.0.8:8080/ProyectoIntegrador/producto.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObj = new
                                JSONObject(response.toString());

                        JSONArray contacts = jsonObj.getJSONArray("producto");
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String id = c.getString("ID_PRODUCTO");
                            String id_categoria_producto  = c.getString("ID_CATEGORIA_PRODUCTO");
                            String nombre_producto = c.getString("NOMBRE_PRODUCTO");
                            String detalle_producto = c.getString("DETALLE_PRODUCTO");
                            String precio_unitario=c.getString("PRECIO_UNITARIO");
                            String imagen_producto = c.getString("IMAGEN_PRODUCTO");
                            DatabaseHandlerProducto db = new DatabaseHandlerProducto(getApplicationContext());
                            db.addProductos(new ElementoProducto(id, id_categoria_producto,nombre_producto,detalle_producto,precio_unitario,imagen_producto));
                            //Log.d("Insert","Contacto insertado correctamente");
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
                    params.put("idCategoria", idCategoriaProducto);
                    return params;
                }
            };
            queue.add(stringRequest);


            gridViewItemsID = db.getAllProductos(0);
            gridViewItemsNombre = db.getAllProductos(2);
            gridViewItemsDetalle = db.getAllProductos(3);
            gridViewItemsPrecio = db.getAllProductos(4);
            gridViewItemsImagen = db.getAllProductos(5);

            //Toast.makeText(getApplicationContext(),gridViewItemsImagen.toString(), Toast.LENGTH_LONG).show();
            arregloID = gridViewItemsID.toArray(new String[0]);
            String[] arregloProductos = gridViewItemsNombre.toArray(new String[0]);
            String[] arregloDescripcion = gridViewItemsDetalle.toArray(new String[0]);
            String[] arregloImagenes = gridViewItemsImagen.toArray(new String[0]);
            String[] arregloPrecios = gridViewItemsPrecio.toArray(new String[0]);

            adapter = new ElementosProductosAdaptador(ListaProductos.this, arregloProductos, arregloDescripcion, arregloPrecios, "Productos", arregloImagenes);
            gridView = (GridView) findViewById(R.id.GridView_Productos);

            return adapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
            super.onPostExecute(stringArrayAdapter);

            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pDialog.dismiss();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    LinearLayout ll = (LinearLayout) view;
                    TextView tv = (TextView) ll.findViewById(R.id.TextView_Nombre);
                    TextView tv1 = (TextView) ll.findViewById(R.id.TextView_Descripcion);
                    TextView tv2 = (TextView) ll.findViewById(R.id.TextView_Precio);

                    selectedId = arregloID[position].toString();
                    selectedItem = tv.getText().toString();
                    selectedDescription = tv1.getText().toString();
                    selectedPrecio = tv2.getText().toString();

                    Intent intent = new Intent(getApplicationContext(), AnadirDetalleCompra.class);

                    intent.putExtra(EXTRA_INDEX, selectedId);
                    intent.putExtra(EXTRA_MESSAGE, selectedItem);
                    intent.putExtra(Intent.EXTRA_TITLE, selectedDescription);
                    intent.putExtra(Intent.EXTRA_TEXT, selectedPrecio);

                    //Toast.makeText(getApplicationContext(),selectedId+" "+selectedItem+" "+selectedDescription+" "+selectedPrecio, Toast.LENGTH_LONG).show();

                    startActivity(intent);
                }
            });
        }

    }

}







