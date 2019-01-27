package com.example.usuario.pruebas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ListaBeneficiarioDonacion extends AppCompatActivity {


    private ArrayList<String> ListViewItemsIDCompra = new ArrayList<String>();
    private ArrayList<String> ListViewItemsIDUsuario = new ArrayList<String>();
    private ArrayList<String> ListViewItemsNombreUsuario = new ArrayList<String>();
    private ArrayList<String> ListViewItemsTotalCompra = new ArrayList<String>();
    private ArrayList<String> ListViewItemsSaldoCompra = new ArrayList<String>();
    private ArrayList<String> ListViewImagenes = new ArrayList<String>();

    String[] arregloIDCompra;
    String[] arregloIDUsuario;
    String[] arregloNombres;
    String[] arregloProductos;
    String[] arregloDescripcion;
    String[] arregloImagenes;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;
    private String item;
    private TextView InformacionPantalla;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    private String selectedIdCompra, selectedIdUsuario;

    private ListView gridView;
    private ElementosComprasAdaptador adapter;


    DatabaseHandlerCompras db = new DatabaseHandlerCompras(this);

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_beneficiario_donacion);

        for (int i=0;i<2;i++){
            ExecTasks t = new ExecTasks(ListaBeneficiarioDonacion.this);
            t.execute();
        }
       InformacionPantalla=(TextView) findViewById(R.id.textView3);

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
                       speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie el nombre del beneficiario al que desea ayudar!");
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

        StringTokenizer st = new StringTokenizer(escuchado);

try {
    if (st.countTokens() == 1) {

        selectedIdCompra = db.getCompraPorPrimerNombreBeneficiario(escuchado).get_idCompra().toString();
        selectedIdUsuario = db.getCompraPorPrimerNombreBeneficiario(escuchado).get_idUsuario().toString();

    }
    if (st.countTokens() == 2) {
        String[] nombres = new String[2];
        int i = 0;

        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            nombres[i] = str;
            i++;
        }

        selectedIdCompra = db.getCompraPorDosIdentificativos(nombres[0], nombres[1]).get_idCompra().toString();
        selectedIdUsuario = db.getCompraPorDosIdentificativos(nombres[0], nombres[1]).get_idUsuario().toString();

    }

    if (st.countTokens() == 3) {

        String[] nombres = new String[2];
        int i = 0;

        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            nombres[i] = str;
            i++;
        }

        selectedIdCompra = db.getCompraPorTresIdentificativos(nombres[0], nombres[1], nombres[2]).get_idCompra().toString();
        selectedIdUsuario = db.getCompraPorTresIdentificativos(nombres[0], nombres[1], nombres[2]).get_idUsuario().toString();

    }

    if (st.countTokens() == 4) {

        String[] nombres = new String[3];
        int i = 0;

        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            nombres[i] = str;
            i++;
        }

        selectedIdCompra = db.getCompraPorCuatroIdentificativos(nombres[0], nombres[1], nombres[2], nombres[3]).get_idCompra().toString();
        selectedIdUsuario = db.getCompraPorCuatroIdentificativos(nombres[0], nombres[1], nombres[2], nombres[3]).get_idUsuario().toString();

    }

    Intent intent = new Intent(getApplicationContext(), DetallesDonante.class);

    intent.putExtra(Intent.EXTRA_INDEX, selectedIdCompra);
    intent.putExtra(EXTRA_MESSAGE, selectedIdUsuario);

    Toast.makeText(getApplicationContext(), selectedIdCompra + " " + selectedIdUsuario, Toast.LENGTH_LONG).show();

    startActivity(intent);
}catch (Exception e){
    myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if(myTTS.getEngines().size()==0){
                Toast.makeText(ListaBeneficiarioDonacion.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                finish();
            }else{
                myTTS.setLanguage(Locale.getDefault());
                speak("Beneficiario no se encuentra en la lista. Por favor intente nuevamente!.");

            }
        }
    });
}


    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ListaBeneficiarioDonacion.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presentan los distintos "+InformacionPantalla.getText().toString()+"." +
                            ". Toque la pantalla y pronuncie el nombre del que desee ayudar después del tono");

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

            db.deleteProductos();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando Lista");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

            TextView textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setVisibility(View.GONE);
            ListView ListView_Beneficiarios = (ListView) findViewById(R.id.ListView_Beneficiarios);
            ListView_Beneficiarios.setVisibility(View.GONE);

            ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
            imageView3.setVisibility(View.VISIBLE);
            TextView textView4 = (TextView) findViewById(R.id.TextViewInformacion);
            textView4.setVisibility(View.VISIBLE);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="http://192.168.0.4:8080/ProyectoIntegrador/ActualizarDetalleBeneficiario.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
            queue.add(stringRequest);
        }

        @Override
        protected ArrayAdapter<String> doInBackground(Void... params) {

            try{
                Thread.sleep(2000);

            }catch(Exception ex){
                ex.printStackTrace();
            }



            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            String url3 ="http://192.168.0.4:8080/ProyectoIntegrador/comprasBeneficiario.php";
            StringRequest jsObjRequest1 = new StringRequest
                    (Request.Method.GET, url3, new
                            Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObj1 = new
                                                JSONObject(response.toString());
                                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getApplicationContext(),String.valueOf(db.conteoRegistros()),Toast.LENGTH_LONG).show();
                                        JSONArray contacts = jsonObj1.getJSONArray("compras");

                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);

                                            String id_compra = c.getString("ID_COMPRA");
                                            String id_usuario  = c.getString("ID_USUARIO");
                                            String fecha_compra = c.getString("FECHA_COMPRA");
                                            String total_compra = c.getString("TOTAL_COMPRA");
                                            String saldo_compra = c.getString("SALDO_COMPRA");
                                            String estado_compra = c.getString("ESTADO_COMPRA");
                                            String id_rol = c.getString("ID_ROL");
                                            String foto_usuario = c.getString("FOTO_USUARIO");
                                            String primer_nombre_usuario = c.getString("PRIMER_NOMBRE_USUARIO");
                                            String segundo_nombre_usuario = c.getString("SEGUNDO_NOMBRE_USUARIO");
                                            String primer_apellido_usuario = c.getString("PRIMER_APELLIDO_USUARIO");
                                            String segundo_apellido_usuario = c.getString("SEGUNDO_APELLIDO_USUARIO");
                                            String direccion_usuario = c.getString("DIRECCION_USUARIO");
                                            String telefono_usuario = c.getString("TELEFONO_USUARIO");
                                            String email_usuario = c.getString("EMAIL_USUARIO");
                                            String usuario_aplicativo = c.getString("USUARIO_APLICATIVO");
                                            String password_aplicativo = c.getString("PASSWORD_APLICATIVO");
                                            String nombres = c.getString("NOMBRES");

                                            //Toast.makeText(getApplicationContext(),nombres,Toast.LENGTH_LONG).show();
                                            db = new DatabaseHandlerCompras(getApplicationContext());
                                            db.addCompras(new ElementoCompra(id_compra,id_usuario,fecha_compra,total_compra,saldo_compra,estado_compra,id_rol,foto_usuario,primer_nombre_usuario,segundo_nombre_usuario,primer_apellido_usuario,segundo_apellido_usuario,direccion_usuario,telefono_usuario,email_usuario,usuario_aplicativo,password_aplicativo,nombres));
                                            //Log.d("Insert","Contacto insertado correctamente");
                                            //db1.deleteProductos();

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
            queue1.add(jsObjRequest1);

            //Toast.makeText(getApplicationContext(),arregloIDCompra.toString(), Toast.LENGTH_LONG).show();


                ListViewItemsIDCompra = db.getAllProductos(0);
                ListViewItemsIDUsuario = db.getAllProductos(1);
                ListViewItemsNombreUsuario = db.getAllProductos(17);
                ListViewItemsTotalCompra = db.getAllProductos(3);
                ListViewItemsSaldoCompra = db.getAllProductos(4);
                ListViewImagenes = db.getAllProductos(7);

                arregloIDCompra = ListViewItemsIDCompra.toArray(new String[0]);
                arregloIDUsuario = ListViewItemsIDUsuario.toArray(new String[0]);
                arregloNombres = ListViewItemsNombreUsuario.toArray(new String[0]);
                arregloProductos = ListViewItemsTotalCompra.toArray(new String[0]);
                arregloDescripcion = ListViewItemsSaldoCompra.toArray(new String[0]);
                arregloImagenes = ListViewImagenes.toArray(new String[0]);


                adapter = new ElementosComprasAdaptador(ListaBeneficiarioDonacion.this, arregloNombres, arregloProductos, arregloDescripcion, "Beneficiarios", arregloImagenes);
                gridView = (ListView) findViewById(R.id.ListView_Beneficiarios);



            return adapter;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
            super.onPostExecute(stringArrayAdapter);

            if(db.conteoRegistros()!=0){

                TextView textView3 = (TextView) findViewById(R.id.textView3);
                textView3.setVisibility(View.VISIBLE);
                ListView ListView_Beneficiarios = (ListView) findViewById(R.id.ListView_Beneficiarios);
                ListView_Beneficiarios.setVisibility(View.VISIBLE);

                ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                imageView3.setVisibility(View.GONE);
                TextView textView4 = (TextView) findViewById(R.id.TextViewInformacion);
                textView4.setVisibility(View.GONE);

                gridView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                pDialog.dismiss();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedIdCompra=arregloIDCompra[position].toString();
                        selectedIdUsuario=arregloIDUsuario[position].toString();


                        Intent intent = new Intent(getApplicationContext(), DetallesDonante.class);

                        intent.putExtra(Intent.EXTRA_INDEX, selectedIdCompra);
                        intent.putExtra(EXTRA_MESSAGE, selectedIdUsuario);

                        //Toast.makeText(getApplicationContext(),selectedIdCompra+" "+selectedIdUsuario, Toast.LENGTH_LONG).show();

                        startActivity(intent);

                    }
                });

            }else{
                pDialog.dismiss();
            }
        }

        }



    }




