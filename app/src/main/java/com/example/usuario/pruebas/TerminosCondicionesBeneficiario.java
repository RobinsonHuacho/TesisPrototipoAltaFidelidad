package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TerminosCondicionesBeneficiario extends AppCompatActivity {


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    private TextView TextViewTitulo;
    private TextView TextViewContenido;
    private Button btnAceptar, btnRegresar;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones_beneficiario);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        TextViewTitulo = (TextView) findViewById(R.id.textView);
        TextViewContenido = (TextView) findViewById(R.id.TextViewInformacion);
        TextViewContenido.setMovementMethod(new ScrollingMovementMethod());

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;


        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;


        btnAceptar= (Button) findViewById(R.id.btn_Continuar) ;
        btnRegresar= (Button) findViewById(R.id.btn_Regresar) ;

        btnAceptar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroBeneficiario.class);
                startActivity(intent);
            }
        });

        btnRegresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IngresoAplicacion.class);
                startActivity(intent);
            }
        });

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextViewTitulo.getTextSize()<90) {
                    TextViewTitulo.setTextSize(0, TextViewTitulo.getTextSize() + 12.0f);
                    TextViewContenido.setTextSize(0, TextViewContenido.getTextSize() + 12.0f);
                    btnAceptar.setTextSize(0, btnAceptar.getTextSize() + 12.0f);
                    btnRegresar.setTextSize(0, btnRegresar.getTextSize() + 12.0f);

                }




            }
        });

        ImageButtonZoomOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextViewTitulo.getTextSize()>66) {
                    TextViewTitulo.setTextSize(0, TextViewTitulo.getTextSize() - 12.0f);
                    TextViewContenido.setTextSize(0, TextViewContenido.getTextSize() - 12.0f);
                    btnAceptar.setTextSize(0, btnAceptar.getTextSize() - 12.0f);
                    btnRegresar.setTextSize(0, btnRegresar.getTextSize() - 12.0f);

                }



            }
        });

        ImageButtonActivar.setVisibility(View.VISIBLE);
        ImageButtonDesactivar.setVisibility(View.GONE);


        ImageButtonActivar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                iniciarTextToSpeechBeneficiario();


                pantalla.setOnClickListener(new OnClickListener() {
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

                TextViewContenido.setOnClickListener(new OnClickListener() {
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
                TextViewContenido.setOnClickListener(null);

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

        if(escuchado.indexOf("aceptar")!= -1){
            Intent intent = new Intent(this,RegistroBeneficiario.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("regresar")!= -1){
                Intent intent = new Intent(this,IngresoAplicacion.class);
                startActivity(intent);
            }else{
                if(escuchado.indexOf("repetir")!= -1){
                    iniciarTextToSpeechBeneficiario();
                }else{
                    myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(myTTS.getEngines().size()==0){
                                Toast.makeText(TerminosCondicionesBeneficiario.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                myTTS.setLanguage(Locale.getDefault());
                                speak("No entiendo esa orden! Por favor pulse la pantalla y pronuncie la palabra aceptar, regresar o repetir");
                            }
                        }
                    });

                }
            }


        }
    }



    private void iniciarTextToSpeechBeneficiario() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(TerminosCondicionesBeneficiario.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presentan los términos y condiciones para el uso de la aplicación.\n" + TextViewContenido.getText().toString()+".. " +
                            " Si está de acuerdo con las condiciones, toque sobre cualquier parte de la pantalla y pronuncie la palabra, aceptar, " +
                            "para dirigirse a la pantalla de registro. Caso contario, la palabra, regresar para volver a la pantalla de " +
                            "ingreso a la aplicación. Si desea volver a escuchar las indicaciones, pulse sobre la parte superior derecha de la " +
                            "pantalla.");

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





}
