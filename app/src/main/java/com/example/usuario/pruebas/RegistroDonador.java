package com.example.usuario.pruebas;

import android.app.Dialog;
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
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroDonador extends AppCompatActivity {

    private TextView InformacionPantalla;
    private TextView TextViewCedulaDonador;
    private TextView TextViewPrimerNombreDonador;
    private TextView TextViewPrimerApellidoDonador;
    private TextView TextViewEmailDonador;
    private TextView TextViewUsuarioDonador;
    private TextView TextViewContraseniaDonador;


    private EditText editTextCedulaDonador;
    private EditText editTextPrimerNombreDonador;
    private EditText editTextPrimerApellidoDonador;
    private EditText editTextEmailDonador;
    private AutoCompleteTextView editTextUsuarioDonador;
    private AutoCompleteTextView editTextPasswordDonador;
    private String nombreIngresado;

    private Button buttonRegistrarse;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;


    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    private static String[] nombres=new String[5];
    private int numeroAleatorio1,numeroAleatorio2,numeroAleatorio3,numeroAleatorio4,numeroAleatorio5;



    Dialog ventanaEmergente;
    private TextView terminos;
    private Button boton;
    CheckBox check;

    DatabaseHandlerUsuarios db = new DatabaseHandlerUsuarios(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_donador);

        for (int i=0;i<2;i++){
            ExecTasks t = new ExecTasks(RegistroDonador.this);
            t.execute();
        }

        ventanaEmergente = new Dialog(this);
        habilitar();


        InformacionPantalla = (TextView) findViewById(R.id.textView14);

        TextViewCedulaDonador= (TextView) findViewById(R.id.textView15);
        TextViewPrimerNombreDonador= (TextView) findViewById(R.id.textView16);
        TextViewPrimerApellidoDonador= (TextView) findViewById(R.id.textView18);
        TextViewEmailDonador= (TextView) findViewById(R.id.textView23);
        TextViewUsuarioDonador= (TextView) findViewById(R.id.textView20);
        TextViewContraseniaDonador= (TextView) findViewById(R.id.textView21);

        editTextCedulaDonador = (EditText) findViewById(R.id.editText_CedulaDonador);
        editTextCedulaDonador.setBackgroundResource(R.drawable.border_color);
        editTextCedulaDonador.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(TextUtils.isEmpty(editTextCedulaDonador.getText().toString())){
                        editTextCedulaDonador.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
                        return true;
                    }else {
                        if (validadorDeCedula(editTextCedulaDonador.getText().toString()) == false) {
                            editTextCedulaDonador.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
                            return true;
                        }

                        if (db.conteoRegistros(editTextCedulaDonador.getText().toString()) == 1) {
                            editTextCedulaDonador.setError("Cédula ya registrada!");
                            return true;
                        }
                    }


                }

                return false;
            }
        });

        numeroAleatorio1=(int) (Math.random() * 100) + 1;
        numeroAleatorio2=(int) (Math.random() * 100) + 1;
        numeroAleatorio3=(int) (Math.random() * 100) + 1;
        numeroAleatorio4=(int) (Math.random() * 100) + 1;
        numeroAleatorio5=(int) (Math.random() * 100) + 1;




        editTextPrimerNombreDonador = (EditText) findViewById(R.id.editText_PrimerNombreDonador);
        editTextPrimerNombreDonador.setBackgroundResource(R.drawable.border_color);


        editTextPrimerNombreDonador.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(editTextPrimerNombreDonador.getText().toString())) {
                        editTextPrimerNombreDonador.setError("Este campo no puede estar vacío. Por favor ingrese su(s) nombre(s)");
                        editTextPrimerNombreDonador.requestFocus();
                        return true;
                    } else {
                        if (editTextPrimerNombreDonador.getText().length() < 1) {
                            editTextPrimerNombreDonador.setError("Por favor ingrese un nombre válido de mínimo 1 caracter");
                            editTextPrimerNombreDonador.requestFocus();
                            return true;
                        }
                    }
                    if(validadorNombres(editTextPrimerNombreDonador.getText().toString())==false) {
                        editTextPrimerNombreDonador.setError("Por favor ingrese un nombre válido de solo letras");
                        editTextPrimerNombreDonador.requestFocus();
                        return true;
                    }
                    nombreIngresado=editTextPrimerNombreDonador.getText().toString();
                    Toast.makeText(RegistroDonador.this, nombreIngresado,Toast.LENGTH_LONG).show();


                }
                return false;


        }
        }
        );




        editTextPrimerApellidoDonador = (EditText) findViewById(R.id.editText_PrimerApellidoDonador);
        editTextPrimerApellidoDonador.setBackgroundResource(R.drawable.border_color);
        editTextPrimerApellidoDonador.setOnKeyListener(new View.OnKeyListener() {
                                                         @Override
                                                         public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                             if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                 if (TextUtils.isEmpty(editTextPrimerApellidoDonador.getText().toString())) {
                                                                     editTextPrimerApellidoDonador.setError("Este campo no puede estar vacío. Por favor ingrese su(s) apellido(s)");
                                                                     editTextPrimerApellidoDonador.requestFocus();
                                                                     return true;
                                                                 } else {
                                                                     if (editTextPrimerApellidoDonador.getText().length() < 1) {
                                                                         editTextPrimerApellidoDonador.setError("Por favor ingrese un apellido válido de mínimo 1 caracteres");
                                                                         editTextPrimerApellidoDonador.requestFocus();
                                                                         return true;
                                                                     }
                                                                 }
                                                                 if(validadorNombres(editTextPrimerNombreDonador.getText().toString())==false) {
                                                                     editTextPrimerApellidoDonador.setError("Por favor ingrese un apellido válido de solo letras");
                                                                     editTextPrimerApellidoDonador.requestFocus();
                                                                     return true;
                                                                 }
                                                             }
                                                             return false;
                                                         }

                                                     }
        );

        editTextEmailDonador = (EditText) findViewById(R.id.editText_Email);
        editTextEmailDonador.setBackgroundResource(R.drawable.border_color);
        editTextEmailDonador.setOnKeyListener(new View.OnKeyListener() {
                                                           @Override
                                                           public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                               if ((keyCode == KeyEvent.KEYCODE_ENTER)) {

                                                                   if(TextUtils.isEmpty(editTextEmailDonador.getText().toString())){
                                                                       editTextEmailDonador.setError("Este campo no puede estar vacío. Por favor ingrese su dirección de correo electrónico");
                                                                       return true;
                                                                   }else{
                                                                   if(validadorMail(editTextEmailDonador.getText().toString())==false){
                                                                       editTextEmailDonador.setError("Correo electrónico no válido. Por favor ingrese nuevamente un correo electrónico válido");
                                                                       return true;
                                                                   }
                                                                   }

                                                               }

                                                               return false;

                                                           }

                                                       }
        );



        editTextUsuarioDonador = (AutoCompleteTextView) findViewById(R.id.editText_Usuario);
        editTextUsuarioDonador.setBackgroundResource(R.drawable.border_color);


        nombres = new String[]{
                "Ayuda1", "Ayuda1", "Ayuda1"
        };
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombres);
        editTextUsuarioDonador.setAdapter(adapter);
        editTextUsuarioDonador.setOnKeyListener(new View.OnKeyListener() {
                                                           @Override
                                                           public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                               if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                   if(TextUtils.isEmpty(editTextUsuarioDonador.getText().toString())){
                                                                       editTextUsuarioDonador.setError("Este campo no puede estar vacío. Por favor ingrese su usuario");
                                                                       return true;
                                                                   }else{
                                                                       if(editTextUsuarioDonador.getText().length()<4){
                                                                           editTextUsuarioDonador.setError("Por favor ingrese un usuario válido de mínimo 4 caracteres de letras y números");
                                                                           return true;
                                                                       }
                                                                   }
       }

                                                               return false;

                                                           }

                                                       }
        );



        editTextPasswordDonador = (AutoCompleteTextView) findViewById(R.id.editText_Contrasenia);
        editTextPasswordDonador.setBackgroundResource(R.drawable.border_color);
        editTextPasswordDonador.setAdapter(adapter);
        editTextPasswordDonador.setOnKeyListener(new View.OnKeyListener() {
                                                    @Override
                                                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                            if(TextUtils.isEmpty(editTextPasswordDonador.getText().toString())){
                                                                editTextPasswordDonador.setError("Este campo no puede estar vacío. Por favor ingrese su contraseña");

                                                            }else{
                                                                if(editTextPasswordDonador.getText().length()<6){
                                                                    editTextPasswordDonador.setError("Por favor ingrese una contraseña válida de mínimo 6 caracteres de letras y números");

                                                                }


                                                            }



                                                        }

                                                        return true;

                                                    }

                                                }
        );

        buttonRegistrarse = (Button) findViewById(R.id.button2) ;

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()<60) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() + 12.0f);
                    TextViewCedulaDonador.setTextSize(0, TextViewCedulaDonador.getTextSize() + 12.0f);
                    editTextCedulaDonador.setTextSize(0, editTextCedulaDonador.getTextSize() + 12.0f);

                    TextViewPrimerNombreDonador.setTextSize(0, TextViewPrimerNombreDonador.getTextSize() + 12.0f);
                    editTextPrimerNombreDonador.setTextSize(0, editTextPrimerNombreDonador.getTextSize() + 12.0f);

                    TextViewPrimerApellidoDonador.setTextSize(0, TextViewPrimerApellidoDonador.getTextSize() + 12.0f);
                    editTextPrimerApellidoDonador.setTextSize(0, editTextPrimerApellidoDonador.getTextSize() + 12.0f);

                    TextViewEmailDonador.setTextSize(0, TextViewEmailDonador.getTextSize() + 12.0f);
                    editTextEmailDonador.setTextSize(0, editTextEmailDonador.getTextSize() + 12.0f);

                     TextViewUsuarioDonador.setTextSize(0, TextViewUsuarioDonador.getTextSize() + 12.0f);
                    editTextUsuarioDonador.setTextSize(0, editTextUsuarioDonador.getTextSize() + 12.0f);

                    TextViewContraseniaDonador.setTextSize(0, TextViewContraseniaDonador.getTextSize() + 12.0f);
                    editTextPasswordDonador.setTextSize(0, editTextPasswordDonador.getTextSize() + 12.0f);

                    buttonRegistrarse.setTextSize(0, buttonRegistrarse.getTextSize() + 12.0f);

                }




            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InformacionPantalla.getTextSize()>66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);
                    TextViewCedulaDonador.setTextSize(0, TextViewCedulaDonador.getTextSize() - 12.0f);
                    editTextCedulaDonador.setTextSize(0, editTextCedulaDonador.getTextSize() - 12.0f);

                    TextViewPrimerNombreDonador.setTextSize(0, TextViewPrimerNombreDonador.getTextSize() - 12.0f);
                    editTextPrimerNombreDonador.setTextSize(0, editTextPrimerNombreDonador.getTextSize() - 12.0f);

                    TextViewPrimerApellidoDonador.setTextSize(0, TextViewPrimerApellidoDonador.getTextSize() - 12.0f);
                    editTextPrimerApellidoDonador.setTextSize(0, editTextPrimerApellidoDonador.getTextSize() - 12.0f);

                    TextViewEmailDonador.setTextSize(0, TextViewEmailDonador.getTextSize() - 12.0f);
                    editTextEmailDonador.setTextSize(0, editTextEmailDonador.getTextSize() - 12.0f);

                    TextViewUsuarioDonador.setTextSize(0, TextViewUsuarioDonador.getTextSize() - 12.0f);
                    editTextUsuarioDonador.setTextSize(0, editTextUsuarioDonador.getTextSize() - 12.0f);

                    TextViewContraseniaDonador.setTextSize(0, TextViewContraseniaDonador.getTextSize() - 12.0f);
                    editTextPasswordDonador.setTextSize(0, editTextPasswordDonador.getTextSize() - 12.0f);

                    buttonRegistrarse.setTextSize(0, buttonRegistrarse.getTextSize() - 12.0f);

                }


            }
        });



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
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Conocer o Siguiente!");
                        mySpeechRecognizer.startListening(speechIntent);

                    }
                });

                InformacionPantalla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Aceptar o Regresar!");
                        //mySpeechRecognizer.startListening(speechIntent);
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

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(RegistroDonador.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla, "+InformacionPantalla.getText().toString()+". Presione sobre cualquier parte de la pantalla" +
                            " y pronuncie su cédula! Se le solicitará una confirmación antes de ingresar los demás campos!.");

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

    private void habilitar() {
        terminos = (TextView)findViewById(R.id.textView2);
        TextView styleterminos = (TextView)findViewById(R.id.textView2);
        SpannableString content = new SpannableString(styleterminos.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        styleterminos.setText(content);


        check = (CheckBox) findViewById(R.id.checkBox);
        boton = (Button) findViewById(R.id.button2);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    boton.setEnabled(true);
                }
                else
                {
                    boton.setEnabled(false);
                }
            }
        });
    }

    public void mostrarTerminos(View v) {

        ventanaEmergente.setContentView(R.layout.custompopup);
    }


    public void makeRequestRegistrarDonador (View view){
        if(TextUtils.isEmpty(editTextCedulaDonador.getText().toString())){
            editTextCedulaDonador.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
            return;
        }else {
            if (validadorDeCedula(editTextCedulaDonador.getText().toString()) == false) {
                editTextCedulaDonador.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
                return;
            }

            if (db.conteoRegistros(editTextCedulaDonador.getText().toString()) == 1) {
                editTextCedulaDonador.setError("Cédula ya registrada!");
                return;
            }
        }

        if (TextUtils.isEmpty(editTextPrimerNombreDonador.getText().toString())) {
            editTextPrimerNombreDonador.setError("Este campo no puede estar vacío. Por favor ingrese su(s) nombre(s)");
            editTextPrimerNombreDonador.requestFocus();
            return;
        } else {
            if (editTextPrimerNombreDonador.getText().length() < 1) {
                editTextPrimerNombreDonador.setError("Por favor ingrese un nombre válido de mínimo 1 caracter");
                editTextPrimerNombreDonador.requestFocus();
                return;
            }
        }
        if(validadorNombres(editTextPrimerNombreDonador.getText().toString())==false) {
            editTextPrimerNombreDonador.setError("Por favor ingrese un nombre válido de solo letras, sin tildes ni caracteres especiales");
            editTextPrimerNombreDonador.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(editTextPrimerApellidoDonador.getText().toString())) {
            editTextPrimerApellidoDonador.setError("Este campo no puede estar vacío. Por favor ingrese su(s) apellido(s)");
            editTextPrimerApellidoDonador.requestFocus();
            return;
        } else {
            if (editTextPrimerApellidoDonador.getText().length() < 1) {
                editTextPrimerApellidoDonador.setError("Por favor ingrese un apellido válido de mínimo 1 caracter");
                editTextPrimerApellidoDonador.requestFocus();
                return;
            }
        }
        if(validadorNombres(editTextPrimerApellidoDonador.getText().toString())==false) {
            editTextPrimerApellidoDonador.setError("Por favor ingrese un apellido válido de solo letras, sin tildes ni caracteres especiales");
            editTextPrimerApellidoDonador.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(editTextEmailDonador.getText().toString())) {
            editTextEmailDonador.setError("Este campo no puede estar vacío. Por favor ingrese su sector");
            editTextEmailDonador.requestFocus();
            return;
        } else {
            if (editTextEmailDonador.getText().length() < 4) {
                editTextEmailDonador.setError("Por favor ingrese un sector válido de mínimo 4 caracteres, sin tildes ni caracteres especiales");
                editTextEmailDonador.requestFocus();
                return;
            }
        }
        if(TextUtils.isEmpty(editTextUsuarioDonador.getText().toString())){
            editTextUsuarioDonador.setError("Este campo no puede estar vacío. Por favor ingrese su usuario");
            return;
        }else{
            if(editTextUsuarioDonador.getText().length()<4){
                editTextUsuarioDonador.setError("Por favor ingrese un usuario válido de mínimo 4 caracteres de letras y números, sin tildes ni caracteres especiales");
                return;
            }
        }

        if(TextUtils.isEmpty(editTextPasswordDonador.getText().toString())){
            editTextPasswordDonador.setError("Este campo no puede estar vacío. Por favor ingrese su contraseña");
            return;
        }else{


            if(editTextPasswordDonador.getText().length()<6){
                editTextPasswordDonador.setError("Por favor ingrese una contraseña válida de mínimo 6 caracteres de letras y números, sin tildes ni caracteres especiales");
                return;
            }


        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.0.10:8080/ProyectoIntegrador/nuevoUsuario.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if(response.matches("1")){
                            Toast.makeText(getApplicationContext(),"Usuario Registrado Exitosamente",Toast.LENGTH_LONG).show();
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
                EditText editTextCedula = (EditText) findViewById(R.id.editText_CedulaDonador);
                EditText editTextPrimerNombre = (EditText) findViewById(R.id.editText_PrimerNombreDonador);

                EditText editTextPrimerApellido = (EditText) findViewById(R.id.editText_PrimerApellidoDonador);

                EditText editTextEmail = (EditText) findViewById(R.id.editText_Email);
                EditText editTextUsuario = (EditText) findViewById(R.id.editText_Usuario);
                EditText editTextContraseña = (EditText) findViewById(R.id.editText_Contrasenia);


                Map<String,String> params = new HashMap<String, String>();
                params.put("rol", SeleccionRol.getActivityInstance().getIdRol());
                params.put("cedula", editTextCedula.getText().toString());
                params.put("primer_nombre", editTextPrimerNombre.getText().toString());
                params.put("primer_apellido", editTextPrimerApellido.getText().toString());
                params.put("condicion", "NO APLICA");
                params.put("direccion", "");
                params.put("telefono", "");
                params.put("email", editTextEmail.getText().toString());
                params.put("usuario", editTextUsuario.getText().toString());
                params.put("password", editTextContraseña.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

        Intent intent = new Intent(this, IngresoAplicacion.class);
        intent.putExtra(Intent.EXTRA_INDEX,editTextUsuarioDonador.getText().toString());
        startActivity(intent);
    }

    public boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
            // Coeficientes de validación cédula
            // El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                    int verificador = Integer.parseInt(cedula.substring(9,10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    }
                    else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        }
        return cedulaCorrecta;
    }

    public boolean validadorNombres(String nombres){
        boolean emailCorrecto = false;
        String emailPattern = "^[a-zA-Z ]+$";

        Pattern pattern = Pattern.compile(emailPattern);

        if (nombres != null) {
            Matcher matcher = pattern.matcher(nombres);
            if (matcher.matches()) {
                emailCorrecto=true;
            }else{
                emailCorrecto=false;
            }
        }
        return emailCorrecto;

    }

    public boolean validadorMail(String email){
        boolean emailCorrecto = false;
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

        Pattern pattern = Pattern.compile(emailPattern);

        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
            emailCorrecto=true;
            }else{
                emailCorrecto=false;
            }
        }
        return emailCorrecto;

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

            db.deleteUsuarios();

            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            String url3 ="http://192.168.0.10:8080/ProyectoIntegrador/Usuarios.php";
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

                                            String id_usuario  = c.getString("ID_USUARIO");
                                            String id_rol = c.getString("ID_ROL");
                                            String foto_usuario = c.getString("FOTO_USUARIO");
                                            String cedula_usuario = c.getString("CEDULA_USUARIO");
                                            String primer_nombre_usuario = c.getString("PRIMER_NOMBRE_USUARIO");
                                            String primer_apellido_usuario = c.getString("PRIMER_APELLIDO_USUARIO");
                                            String condicion_usuario = c.getString("CONDICION_USUARIO");
                                            String direccion_usuario = c.getString("DIRECCION_USUARIO");
                                            String telefono_usuario = c.getString("TELEFONO_USUARIO");
                                            String email_usuario = c.getString("EMAIL_USUARIO");
                                            String usuario_aplicativo = c.getString("USUARIO_APLICATIVO");
                                            String password_aplicativo = c.getString("PASSWORD_APLICATIVO");

                                            //Toast.makeText(getApplicationContext(),nombres,Toast.LENGTH_LONG).show();
                                            db = new DatabaseHandlerUsuarios(getApplicationContext());
                                            db.addUsuarios(new ElementoUsuario(id_usuario,id_rol,foto_usuario,cedula_usuario,primer_nombre_usuario,primer_apellido_usuario,condicion_usuario,direccion_usuario,telefono_usuario,email_usuario,usuario_aplicativo,password_aplicativo));
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

        }


        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
