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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class InformacionGeneral extends AppCompatActivity implements TextToSpeech.OnInitListener{


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    private TextView TextViewTitulo;
    private TextView TextViewContenido;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        getSupportActionBar().hide();

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        TextViewTitulo = (TextView) findViewById(R.id.textView);
        TextViewContenido = (TextView) findViewById(R.id.TextViewInformacion);
        TextViewContenido.setMovementMethod(new ScrollingMovementMethod());

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                    pantalla.setScaleX((float) (x + 1));
                    pantalla.setScaleY((float) (y + 1));




            }
        });

        ImageButtonZoomOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                    pantalla.setScaleX((float) (x - 1));
                    pantalla.setScaleY((float) (y - 1));



            }
        });


        if(Integer.parseInt(SeleccionRol.getActivityInstance().getIdRol())==2){
            iniciarTextToSpeechDonador();

            ImageButtonActivar.setVisibility(View.VISIBLE);
            ImageButtonDesactivar.setVisibility(View.GONE);

            ImageButtonActivar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    myTTS.stop();

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
                            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Conocer o Siguiente!");
                            startActivityForResult(speechIntent,RECONOCEDOR_VOZ);
                        }
                    });

                    ImageButtonActivar.setVisibility(View.GONE);
                    ImageButtonDesactivar.setVisibility(View.VISIBLE);
                }
            });


            ImageButtonDesactivar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    myTTS.stop();

                    pantalla.setOnClickListener(null);
                    TextViewContenido.setOnClickListener(null);

                    ImageButtonActivar.setVisibility(View.VISIBLE);
                    ImageButtonDesactivar.setVisibility(View.GONE);
                }
            });




        }else{
            ImageButtonActivar.setVisibility(View.VISIBLE);
            ImageButtonDesactivar.setVisibility(View.GONE);

            iniciarTextToSpeechBeneficiario();

            ImageButtonActivar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    myTTS.stop();

                    iniciarTextToSpeechBeneficiario();

                    }
            });

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
                    speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la palabra Conocer o Siguiente!");
                    startActivityForResult(speechIntent,RECONOCEDOR_VOZ);
                }
            });

        }



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

        if(escuchado.indexOf("siguiente")!=-1){
            Intent intent = new Intent(this,IngresoAplicacion.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("conocer")!=-1){
                TextViewContenido.requestFocus();
                TextViewContenido.setBackgroundResource(R.drawable.borde_textview);
                speak(TextViewContenido.getText().toString());
            }else{
                if(Integer.parseInt(SeleccionRol.getActivityInstance().getIdRol())==2) {
                    iniciarTextToSpeechDonador();
                }else{
                    iniciarTextToSpeechBeneficiario();
                }
            }
        }
    }

    private void iniciarTextToSpeechBeneficiario() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(InformacionGeneral.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presenta la información general de la aplicación. " +
                          "Si desea conocer mas, pulse sobre cualquier parte de la pantalla, y pronuncie la palabra, " +
                        "conocer, después del tono. Caso contario, pronuncie la palabra siguiente");
                    //speak(TextViewTitulo.getText().toString());
                    //speak(TextViewContenido.getText().toString());
                }
            }
        });
    }

    private void iniciarTextToSpeechDonador() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(InformacionGeneral.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("Si desea manejar la aplicación mediante comandos por voz, pulse sobre el" +
                            " botón en la parte superior derecha, caso contrario manéjela normalmente.");
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



    public void irAIngresoAplicacion (View view)
    {

        Intent intent = new Intent(getApplicationContext(),IngresoAplicacion.class);
        startActivity(intent);
    }


    @Override
    public void onInit(int status) {

    }



}
