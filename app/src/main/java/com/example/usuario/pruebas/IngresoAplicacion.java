package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IngresoAplicacion extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextContraseña;
    private TextView textViewRegistro;
    private Button btnIngreso;
    private static String id_usuario;
    private static String id_rol;

    private static IngresoAplicacion INSTANCE;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_aplicacion);
        getSupportActionBar().hide();

        DatabaseHandlerCompras db = new DatabaseHandlerCompras(this);
        db.deleteProductos();

        INSTANCE = this;
        editTextUsuario = (EditText) findViewById(R.id.EditText_Usuario);
        editTextUsuario.setBackgroundResource(R.drawable.border_color);

        editTextContraseña = (EditText) findViewById(R.id.editText_Contrasenia);
        editTextContraseña.setBackgroundResource(R.drawable.border_color);

        textViewRegistro = (TextView) findViewById(R.id.TextView_Registro);
        textViewRegistro.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SeleccionRol.getActivityInstance().getIdRol()=="1"){
                Intent intent= new Intent(getApplicationContext(), RegistroBeneficiario.class);
                startActivity(intent);
            }else{
                    Intent intent= new Intent(getApplicationContext(), RegistroDonador.class);
                    startActivity(intent);
                }
            }
        });

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Deletree sus credenciales!");
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);


               }
        });

        iniciarTextToSpeech();


      }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == RESULT_OK && requestCode == RECONOCEDOR_VOZ){
            ArrayList<String> reconocido = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String escuchado = reconocido.get(0);
            editTextUsuario.setText(escuchado);
            speak("Usted ha ingresado:"+editTextUsuario.getText().toString()+". Si es correcto, pronuncie la palabra listo, caso " +
                    "contrario, la palabra, repetir para volver a ingresar su usuario");




        }
    }





    private void prepararRespuesta(String escuchado) {

        escuchado = escuchado.toLowerCase();

        if(escuchado.indexOf("registrar")!=-1){
                Intent intent = new Intent(getApplicationContext(), RegistroBeneficiario.class);
                startActivity(intent);
        }else {
            if (escuchado.indexOf("listo") != -1) {
                speak("Ahora deletree su contraseña");

            } else {
                if (escuchado.indexOf("repetir") != -1) {
                    speak("Deletree su usuario");

                }
            }
        }
    }


    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(IngresoAplicacion.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
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



    public void mostrarInterfazCorrespondienteRol(View view) {
        final String id_rol_desde_seleccion_rol=SeleccionRol.getActivityInstance().getIdRol();

        if(TextUtils.isEmpty(editTextUsuario.getText().toString())){
            editTextUsuario.setError("Este campo no puede estar vacío. Por favor ingrese su usuario");
            return;
        }
        if(TextUtils.isEmpty(editTextContraseña.getText().toString())){
            editTextContraseña.setError("Este campo no puede estar vacío. Por favor ingrese su contraseña");
            return;
        }else{
            RequestQueue queue = Volley.newRequestQueue(this);
            String url3 ="http://192.168.0.8:8080/ProyectoIntegrador/loginApp.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                    Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.trim().length()==2)
                            {
                                Toast.makeText(getApplicationContext(),"Usuario/Contraseña no válidos",Toast.LENGTH_LONG).show();
                            }
                            try{
                                JSONObject obj = new JSONObject(response);
                                JSONArray ids = obj.getJSONArray("ids");
                                for(int i=0;i<ids.length();i++){
                                    JSONObject c= ids.getJSONObject(i);
                                    id_usuario = c.getString("ID_USUARIO");
                                    id_rol = c.getString("ID_ROL");

                                    if(id_rol.matches("1")){
                                        Intent intent = new Intent(getApplicationContext(), ListaCategoriaProducto.class);
                                        startActivity(intent);

                                        Intent intent1 = new Intent("PASO_ID_USUARIO").putExtra("ID_USUARIO", id_usuario);

                                        //Toast.makeText(getApplicationContext(),id_rol+" Usuario Beneficiario",Toast.LENGTH_LONG).show();
                                    }else {
                                        if(id_rol.matches("2")){
                                            Intent intent = new Intent(getApplicationContext(), ListaBeneficiarioDonacion.class);
                                            startActivity(intent);
                                            //Toast.makeText(getApplicationContext(),id_rol+" Usuario Donador",Toast.LENGTH_LONG).show();
                                        }

                                    }

                                }
                            }catch (Throwable t){

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //nombreTextView.setText("That didn't work!");
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("id_rol", id_rol_desde_seleccion_rol);
                    params.put("usuario", editTextUsuario.getText().toString().replace(" ",""));
                    params.put("password",editTextContraseña.getText().toString().replace(" ",""));
                    return params;
                }
            };
            queue.add(stringRequest);

        }





    }

    public void mostrarInterfazCorrespondienteRolRegistro(View view) {
        if(SeleccionRol.getActivityInstance().getIdRol()=="1"){
            Intent intent = new Intent(getApplicationContext(), RegistroBeneficiario.class);
            startActivity(intent);
        }else
        {
            Intent intent = new Intent(getApplicationContext(), RegistroDonador.class);
            startActivity(intent);
        }

    }

    public static IngresoAplicacion getActivityInstance() {
        return INSTANCE;
    }

    public String getIdUsuario()
    {
        return this.id_usuario;
    }


}



