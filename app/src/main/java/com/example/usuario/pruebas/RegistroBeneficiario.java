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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroBeneficiario extends AppCompatActivity {

    private String resultado;

    private TextView InformacionPantalla;
    private TextView TextViewCedulaBeneficiario;
    private TextView TextViewPrimerNombreBeneficiario;
    private TextView TextViewPrimerApellidoBeneficiario;
    private TextView TextViewDireccionBeneficiario;
    private TextView TextViewTelefonoBeneficiario;
    private TextView TextViewUsuarioBeneficiario;
    private TextView TextViewContraseniaBeneficiario;

    private EditText editTextCedulaBeneficiario;
    private EditText editTextPrimerNombreBeneficiario;
    private EditText editTextPrimerApellidoBeneficiario;
    private EditText editTextDireccionBeneficiario;
    private EditText editTextTelefonoBeneficiario;
    private AutoCompleteTextView editTextUsuarioBeneficiario;
    private AutoCompleteTextView editTextContraseniaBeneficiario;

    private Button buttonRegistrarse;


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    DatabaseHandlerUsuarios db = new DatabaseHandlerUsuarios(this);

    private static String[] nombres=new String[5];
    private int numeroAleatorio1,numeroAleatorio2,numeroAleatorio3,numeroAleatorio4,numeroAleatorio5;
    private String nombreIngresado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_beneficiario);

        for (int i=0;i<2;i++){
            ExecTasks t = new ExecTasks(RegistroBeneficiario.this);
            t.execute();
        }

        InformacionPantalla = (TextView) findViewById(R.id.textView14);

       TextViewCedulaBeneficiario= (TextView) findViewById(R.id.textView15);
       TextViewPrimerNombreBeneficiario= (TextView) findViewById(R.id.textView16);
       TextViewPrimerApellidoBeneficiario= (TextView) findViewById(R.id.textView18);
       TextViewDireccionBeneficiario= (TextView) findViewById(R.id.textView21);
       TextViewTelefonoBeneficiario= (TextView) findViewById(R.id.textView22);
       TextViewUsuarioBeneficiario= (TextView) findViewById(R.id.LabelUsuario);
       TextViewContraseniaBeneficiario= (TextView) findViewById(R.id.LabelPassword);

        editTextCedulaBeneficiario = (EditText) findViewById(R.id.editText_CedulaBeneficiario);
        editTextCedulaBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextCedulaBeneficiario.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(TextUtils.isEmpty(editTextCedulaBeneficiario.getText().toString())){
                        editTextCedulaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
                        return true;
                    }else {
                        if (validadorDeCedula(editTextCedulaBeneficiario.getText().toString()) == false) {
                            editTextCedulaBeneficiario.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
                            return true;
                        }

                        if (db.conteoRegistros(editTextCedulaBeneficiario.getText().toString()) == 1) {
                            editTextCedulaBeneficiario.setError("Cédula ya registrada!");
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


        editTextPrimerNombreBeneficiario = (EditText) findViewById(R.id.editText_PrimerNombreBeneficiario);
        editTextPrimerNombreBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextPrimerNombreBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                         @Override
                                                         public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                             if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                 if (TextUtils.isEmpty(editTextPrimerNombreBeneficiario.getText().toString())) {
                                                                     editTextPrimerNombreBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su(s) nombre(s)");
                                                                     editTextPrimerNombreBeneficiario.requestFocus();
                                                                     return true;
                                                                 } else {
                                                                     if (editTextPrimerNombreBeneficiario.getText().length() < 1) {
                                                                         editTextPrimerNombreBeneficiario.setError("Por favor ingrese un nombre válido de mínimo 1 caracter");
                                                                         editTextPrimerNombreBeneficiario.requestFocus();
                                                                         return true;
                                                                     }
                                                                 }
                                                                 if(validadorNombres(editTextPrimerNombreBeneficiario.getText().toString())==false) {
                                                                     editTextPrimerNombreBeneficiario.setError("Por favor ingrese un nombre válido de solo letras");
                                                                     editTextPrimerNombreBeneficiario.requestFocus();
                                                                     return true;
                                                                 }
                                                                 nombreIngresado=editTextPrimerNombreBeneficiario.getText().toString();
                                                                 //Toast.makeText(RegistroBeneficiario.this, nombreIngresado,Toast.LENGTH_LONG).show();
                                                                 editTextUsuarioBeneficiario.setText(nombreIngresado+numeroAleatorio1);
                                                                 editTextContraseniaBeneficiario.setText(nombreIngresado+numeroAleatorio1);

                                                             }
                                                             return false;
                                                         }

                                                     }
        );




        editTextPrimerApellidoBeneficiario = (EditText) findViewById(R.id.editText_PrimerApellidoBeneficiario);
        editTextPrimerApellidoBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextPrimerApellidoBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                           @Override
                                                           public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                               if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                   if (TextUtils.isEmpty(editTextPrimerApellidoBeneficiario.getText().toString())) {
                                                                       editTextPrimerApellidoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su(s) apellido(s)");
                                                                       editTextPrimerApellidoBeneficiario.requestFocus();
                                                                       return true;
                                                                   } else {
                                                                       if (editTextPrimerApellidoBeneficiario.getText().length() < 1) {
                                                                           editTextPrimerApellidoBeneficiario.setError("Por favor ingrese un apellido válido de mínimo 1 caracter");
                                                                           editTextPrimerApellidoBeneficiario.requestFocus();
                                                                           return true;
                                                                       }
                                                                   }
                                                                   if(validadorNombres(editTextPrimerApellidoBeneficiario.getText().toString())==false) {
                                                                       editTextPrimerApellidoBeneficiario.setError("Por favor ingrese un apellido válido de solo letras");
                                                                       editTextPrimerApellidoBeneficiario.requestFocus();
                                                                       return true;
                                                                   }
                                                               }
                                                               return false;
                                                           }

                                                       }
        );


        editTextDireccionBeneficiario = (EditText) findViewById(R.id.editText_Direccion);
        editTextDireccionBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextDireccionBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                                @Override
                                                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                        if (TextUtils.isEmpty(editTextDireccionBeneficiario.getText().toString())) {
                                                                            editTextDireccionBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su sector");
                                                                            editTextDireccionBeneficiario.requestFocus();
                                                                            return true;
                                                                        } else {
                                                                            if (editTextDireccionBeneficiario.getText().length() < 4) {
                                                                                editTextDireccionBeneficiario.setError("Por favor ingrese un sector válido de mínimo 4 caracteres");
                                                                                editTextDireccionBeneficiario.requestFocus();
                                                                                return true;
                                                                            }
                                                                        }
                                                                        if(validadorNombres(editTextDireccionBeneficiario.getText().toString())==false) {
                                                                            editTextDireccionBeneficiario.setError("Por favor ingrese un sector válido de solo letras");
                                                                            editTextDireccionBeneficiario.requestFocus();
                                                                            return true;
                                                                        }
                                                                    }
                                                                    return false;
                                                                }

                                                            }
        );

        editTextTelefonoBeneficiario = (EditText) findViewById(R.id.editText_Telefono);
        editTextTelefonoBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextTelefonoBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                           @Override
                                                           public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                               if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                                   if (TextUtils.isEmpty(editTextTelefonoBeneficiario.getText().toString())) {
                                                                       editTextTelefonoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su teléfono");
                                                                       editTextTelefonoBeneficiario.requestFocus();
                                                                       return true;
                                                                   } else {
                                                                       if (editTextTelefonoBeneficiario.getText().length() < 7) {
                                                                           editTextTelefonoBeneficiario.setError("Por favor ingrese un teléfono válido de mínimo 7 números");
                                                                           editTextTelefonoBeneficiario.requestFocus();
                                                                           return true;
                                                                       }
                                                                   }
                                                                   if(validadorTelefono(editTextTelefonoBeneficiario.getText().toString())==false) {
                                                                       editTextTelefonoBeneficiario.setError("Por favor ingrese un teléfono válido de solo números");
                                                                       editTextTelefonoBeneficiario.requestFocus();
                                                                       return true;
                                                                   }
                                                               }
                                                               return false;
                                                           }

                                                       }
        );

        editTextUsuarioBeneficiario = (AutoCompleteTextView ) findViewById(R.id.editText_UsuarioBeneficiario);
        editTextUsuarioBeneficiario.setBackgroundResource(R.drawable.border_color);
        nombres = new String[]{
                nombreIngresado,nombreIngresado,nombreIngresado
        };
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombres);
        editTextUsuarioBeneficiario.setAdapter(adapter);

        editTextUsuarioBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                    @Override
                                                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                        if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                            if(TextUtils.isEmpty(editTextUsuarioBeneficiario.getText().toString())){
                                                                editTextUsuarioBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su usuario");
                                                                return true;
                                                            }else{
                                                                if(editTextUsuarioBeneficiario.getText().length()<4){
                                                                    editTextUsuarioBeneficiario.setError("Por favor ingrese un usuario válido de mínimo 4 caracteres de letras y números");
                                                                    return true;
                                                                }
                                                            }
                                                        }

                                                        return false;

                                                    }

                                                }
        );


        editTextContraseniaBeneficiario = (AutoCompleteTextView) findViewById(R.id.editText_ContraseniaBeneficiario);
        editTextContraseniaBeneficiario.setBackgroundResource(R.drawable.border_color);
        editTextContraseniaBeneficiario.setAdapter(adapter);
        editTextContraseniaBeneficiario.setOnKeyListener(new View.OnKeyListener() {
                                                     @Override
                                                     public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                         if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                             if(TextUtils.isEmpty(editTextContraseniaBeneficiario.getText().toString())){
                                                                 editTextContraseniaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su contraseña");
                                                                 return true;
                                                             }else{


                                                                 if(editTextContraseniaBeneficiario.getText().length()<6){
                                                                     editTextContraseniaBeneficiario.setError("Por favor ingrese una contraseña válida de mínimo 6 caracteres de letras y números");
                                                                     return true;
                                                                 }


                                                             }



                                                         }

                                                         return false;

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
                    TextViewCedulaBeneficiario.setTextSize(0, TextViewCedulaBeneficiario.getTextSize() + 12.0f);
                    editTextCedulaBeneficiario.setTextSize(0, editTextCedulaBeneficiario.getTextSize() + 12.0f);

                    TextViewPrimerNombreBeneficiario.setTextSize(0, TextViewPrimerNombreBeneficiario.getTextSize() + 12.0f);
                    editTextPrimerNombreBeneficiario.setTextSize(0, editTextPrimerNombreBeneficiario.getTextSize() + 12.0f);


                    TextViewPrimerApellidoBeneficiario.setTextSize(0, TextViewPrimerApellidoBeneficiario.getTextSize() + 12.0f);
                    editTextPrimerApellidoBeneficiario.setTextSize(0, editTextPrimerApellidoBeneficiario.getTextSize() + 12.0f);


                    TextViewDireccionBeneficiario.setTextSize(0, TextViewDireccionBeneficiario.getTextSize() + 12.0f);
                    editTextDireccionBeneficiario.setTextSize(0, editTextDireccionBeneficiario.getTextSize() + 12.0f);

                    TextViewTelefonoBeneficiario.setTextSize(0, TextViewTelefonoBeneficiario.getTextSize() + 12.0f);
                    editTextTelefonoBeneficiario.setTextSize(0, editTextTelefonoBeneficiario.getTextSize() + 12.0f);

                    TextViewUsuarioBeneficiario.setTextSize(0, TextViewUsuarioBeneficiario.getTextSize() + 12.0f);
                    editTextUsuarioBeneficiario.setTextSize(0, editTextUsuarioBeneficiario.getTextSize() + 12.0f);

                    TextViewContraseniaBeneficiario.setTextSize(0, TextViewContraseniaBeneficiario.getTextSize() + 12.0f);
                    editTextContraseniaBeneficiario.setTextSize(0, editTextContraseniaBeneficiario.getTextSize() + 12.0f);

                    buttonRegistrarse.setTextSize(0, buttonRegistrarse.getTextSize() + 12.0f);

                }



            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InformacionPantalla.getTextSize() > 66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);
                    TextViewCedulaBeneficiario.setTextSize(0, TextViewCedulaBeneficiario.getTextSize() - 12.0f);
                    editTextCedulaBeneficiario.setTextSize(0, editTextCedulaBeneficiario.getTextSize() - 12.0f);

                    TextViewPrimerNombreBeneficiario.setTextSize(0, TextViewPrimerNombreBeneficiario.getTextSize() - 12.0f);
                    editTextPrimerNombreBeneficiario.setTextSize(0, editTextPrimerNombreBeneficiario.getTextSize() - 12.0f);


                    TextViewPrimerApellidoBeneficiario.setTextSize(0, TextViewPrimerApellidoBeneficiario.getTextSize() - 12.0f);
                    editTextPrimerApellidoBeneficiario.setTextSize(0, editTextPrimerApellidoBeneficiario.getTextSize() - 12.0f);


                    TextViewDireccionBeneficiario.setTextSize(0, TextViewDireccionBeneficiario.getTextSize() - 12.0f);
                    editTextDireccionBeneficiario.setTextSize(0, editTextDireccionBeneficiario.getTextSize() - 12.0f);

                    TextViewTelefonoBeneficiario.setTextSize(0, TextViewTelefonoBeneficiario.getTextSize() - 12.0f);
                    editTextTelefonoBeneficiario.setTextSize(0, editTextTelefonoBeneficiario.getTextSize() - 12.0f);

                    TextViewUsuarioBeneficiario.setTextSize(0, TextViewUsuarioBeneficiario.getTextSize() - 12.0f);
                    editTextUsuarioBeneficiario.setTextSize(0, editTextUsuarioBeneficiario.getTextSize() - 12.0f);

                    TextViewContraseniaBeneficiario.setTextSize(0, TextViewContraseniaBeneficiario.getTextSize() - 12.0f);
                    editTextContraseniaBeneficiario.setTextSize(0, editTextContraseniaBeneficiario.getTextSize() - 12.0f);

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
                            //mySpeechRecognizer.startListening(speechIntent);
                            startActivityForResult(speechIntent,RECONOCEDOR_VOZ);
                        }
                    });

                    InformacionPantalla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Aceptar o Regresar!");
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

                pantalla.setOnClickListener(null);
                InformacionPantalla.setOnClickListener(null);

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
                    Toast.makeText(RegistroBeneficiario.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
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

    public void makeRequestRegistrarBeneficiario (View view)
    {

        if(TextUtils.isEmpty(editTextCedulaBeneficiario.getText().toString())){
            editTextCedulaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
            return;
        }else {
            if (validadorDeCedula(editTextCedulaBeneficiario.getText().toString()) == false) {
                editTextCedulaBeneficiario.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
                return;
            }

            if (db.conteoRegistros(editTextCedulaBeneficiario.getText().toString()) == 1) {
                editTextCedulaBeneficiario.setError("Cédula ya registrada!");
                return;
            }
        }

        if (TextUtils.isEmpty(editTextPrimerNombreBeneficiario.getText().toString())) {
            editTextPrimerNombreBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su(s) nombre(s)");
            editTextPrimerNombreBeneficiario.requestFocus();
            return;
        } else {
            if (editTextPrimerNombreBeneficiario.getText().length() < 1) {
                editTextPrimerNombreBeneficiario.setError("Por favor ingrese un nombre válido de mínimo 1 caracter");
                editTextPrimerNombreBeneficiario.requestFocus();
                return;
            }
        }
        if(validadorNombres(editTextPrimerNombreBeneficiario.getText().toString())==false) {
            editTextPrimerNombreBeneficiario.setError("Por favor ingrese un nombre válido de solo letras, sin tildes ni caracteres especiales");
            editTextPrimerNombreBeneficiario.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(editTextPrimerApellidoBeneficiario.getText().toString())) {
            editTextPrimerApellidoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su(s) apellido(s)");
            editTextPrimerApellidoBeneficiario.requestFocus();
            return;
        } else {
            if (editTextPrimerApellidoBeneficiario.getText().length() < 1) {
                editTextPrimerApellidoBeneficiario.setError("Por favor ingrese un apellido válido de mínimo 1 caracter");
                editTextPrimerApellidoBeneficiario.requestFocus();
                return;
            }
        }
        if(validadorNombres(editTextPrimerApellidoBeneficiario.getText().toString())==false) {
            editTextPrimerApellidoBeneficiario.setError("Por favor ingrese un apellido válido de solo letras, sin tildes ni caracteres especiales");
            editTextPrimerApellidoBeneficiario.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(editTextDireccionBeneficiario.getText().toString())) {
            editTextDireccionBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su sector");
            editTextDireccionBeneficiario.requestFocus();
            return;
        } else {
            if (editTextDireccionBeneficiario.getText().length() < 4) {
                editTextDireccionBeneficiario.setError("Por favor ingrese un sector válido de mínimo 4 caracteres, sin tildes ni caracteres especiales");
                editTextDireccionBeneficiario.requestFocus();
                return;
            }
        }
        if(validadorNombres(editTextDireccionBeneficiario.getText().toString())==false) {
            editTextDireccionBeneficiario.setError("Por favor ingrese un sector válido de solo letras");
            editTextDireccionBeneficiario.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(editTextTelefonoBeneficiario.getText().toString())) {
            editTextTelefonoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su teléfono");
            editTextTelefonoBeneficiario.requestFocus();
            return;
        } else {
            if (editTextTelefonoBeneficiario.getText().length() < 7) {
                editTextTelefonoBeneficiario.setError("Por favor ingrese un teléfono válido de mínimo 7 números");
                editTextTelefonoBeneficiario.requestFocus();
                return;
            }
        }
        if(validadorTelefono(editTextTelefonoBeneficiario.getText().toString())==false) {
            editTextTelefonoBeneficiario.setError("Por favor ingrese un teléfono válido de solo números");
            editTextTelefonoBeneficiario.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(editTextUsuarioBeneficiario.getText().toString())){
            editTextUsuarioBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su usuario");
            return;
        }else{
            if(editTextUsuarioBeneficiario.getText().length()<4){
                editTextUsuarioBeneficiario.setError("Por favor ingrese un usuario válido de mínimo 4 caracteres de letras y números, sin tildes ni caracteres especiales");
                return;
            }
        }

        if(TextUtils.isEmpty(editTextContraseniaBeneficiario.getText().toString())){
            editTextContraseniaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su contraseña");
            return;
        }else{


            if(editTextContraseniaBeneficiario.getText().length()<6){
                editTextContraseniaBeneficiario.setError("Por favor ingrese una contraseña válida de mínimo 6 caracteres de letras y números, sin tildes ni caracteres especiales");
                return;
            }


        }





        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.0.10:8080/ProyectoIntegrador/nuevoUsuario.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"["+response.toString()+"]",Toast.LENGTH_LONG).show();
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

                EditText editTextUsuario = (EditText) findViewById(R.id.editText_UsuarioBeneficiario);
                EditText editTextContraseña = (EditText) findViewById(R.id.editText_ContraseniaBeneficiario);

                Map<String,String> params = new HashMap<String, String>();
                params.put("rol", SeleccionRol.getActivityInstance().getIdRol());
                params.put("cedula", editTextCedulaBeneficiario.getText().toString());
                params.put("primer_nombre", editTextPrimerNombreBeneficiario.getText().toString());
                params.put("primer_apellido", editTextPrimerApellidoBeneficiario.getText().toString());
                params.put("condicion", SeleccionRol.getActivityInstance().getCondicion());
                params.put("direccion", editTextDireccionBeneficiario.getText().toString());
                params.put("telefono", editTextTelefonoBeneficiario.getText().toString());
                params.put("email","");
                params.put("usuario", editTextUsuarioBeneficiario.getText().toString());
                params.put("password", editTextContraseniaBeneficiario.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

        Intent intent1 = new Intent(this, IngresoAplicacion.class);
        intent1.putExtra(Intent.EXTRA_INDEX,editTextUsuarioBeneficiario.getText().toString());
        startActivity(intent1);




 }

    public boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
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

    public boolean validadorDeMail(String email){
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

    public boolean validadorTelefono(String nombres){
        boolean emailCorrecto = false;
        String emailPattern = "^[0-9 ]+$";

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
                                       // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                                            Log.d("Insert","Contacto insertado correctamente");
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


