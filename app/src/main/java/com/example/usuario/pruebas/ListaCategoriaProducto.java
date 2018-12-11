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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.EXTRA_INDEX;

public class ListaCategoriaProducto extends AppCompatActivity {

    private GridView gridViewCategoriaProducto;
    private ArrayList<String> GridViewItems = new ArrayList<String>();
    private ArrayList<String> GridViewImagenes = new ArrayList<String>();
    private String[] arregloCategorias;
    DatabaseHandlerCategorias db = new DatabaseHandlerCategorias(this);
    ElementosCategoriaProductoAdaptador adapter;
    private String item;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private GridView GridView_Categoria_Productos;
    private TextView InformacionPantalla;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_producto);



        for (int i=0;i<2;i++){
            ExecTasks t = new ExecTasks(ListaCategoriaProducto.this);
            t.execute();
        }

        InformacionPantalla=(TextView) findViewById(R.id.textView3);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        GridView_Categoria_Productos = (GridView) findViewById(R.id.GridView_Categoria_Productos);

       pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la categoría de producto!");
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

            item=escuchado;
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {


        escuchado = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        escuchado = escuchado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        escuchado = escuchado.toUpperCase();

            final ElementoCategoriaProducto categoriaProducto = db.getCategoriaNombre(escuchado);

            Intent intent = new Intent(getApplicationContext(), ListaProductos.class);
            intent.putExtra(EXTRA_INDEX,categoriaProducto.getIdCategoriaProducto());
        //       Toast.makeText(getApplicationContext(),categoriaProducto.getIdCategoriaProducto(),Toast.LENGTH_SHORT).show();
            startActivity(intent);

    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ListaCategoriaProducto.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presentan las diferentes "+InformacionPantalla.getText().toString()+"." +
                    ". Toque la pantalla y pronuncie la que desee después del tono");

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

    public class ExecTasks extends AsyncTask<Void, Void, ArrayAdapter<String>>{

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
            String url2 = "http://192.168.0.14:8080/ProyectoIntegrador/categoriaProducto.php";
            StringRequest jsObjRequest = new StringRequest
                    (Request.Method.GET, url2, new
                            Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObj = new
                                                JSONObject(response.toString());

                                        JSONArray contacts = jsonObj.getJSONArray("categoriaProducto");
                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);

                                            String id = c.getString("ID_CATEGORIA_PRODUCTO");
                                            String nombre_categoria = c.getString("NOMBRE_CATEGORIA_PRODUCTO");
                                            String imagen_categoria = c.getString("IMAGEN_CATEGORIA");
                                            db = new DatabaseHandlerCategorias(getApplicationContext());
                                            db.addCategoria(new ElementoCategoriaProducto(id, nombre_categoria, imagen_categoria));
                                            //Log.d("Insert","Contacto insertado correctamente");
                                        }
                                    } catch (final JSONException e) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //mTextView.setText("That didn't work!");
                        }
                    });
            queue.add(jsObjRequest);

            GridViewItems = db.getAllCategorias(1);
            GridViewImagenes = db.getAllCategorias(2);

            arregloCategorias = GridViewItems.toArray(new String[0]);
            String[] arregloImagenes = GridViewImagenes.toArray(new String[0]);

            //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
            adapter = new ElementosCategoriaProductoAdaptador(ListaCategoriaProducto.this, arregloCategorias, "CategoriaProductos", arregloImagenes);
            gridViewCategoriaProducto = (GridView) findViewById(R.id.GridView_Categoria_Productos);

            return adapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
            super.onPostExecute(stringArrayAdapter);

            gridViewCategoriaProducto.setAdapter(adapter);
           // adapter.notifyDataSetChanged();
            pDialog.dismiss();

            gridViewCategoriaProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) gridViewCategoriaProducto.getItemAtPosition(position);
                    final ElementoCategoriaProducto categoriaProducto = db.getCategoriaNombre(item);

                    Intent intent = new Intent(getApplicationContext(), ListaProductos.class);
                    intent.putExtra(EXTRA_INDEX,categoriaProducto.getIdCategoriaProducto());
                    //Toast.makeText(getApplicationContext(),categoriaProducto.getIdCategoriaProducto(),Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
        }





    }


}