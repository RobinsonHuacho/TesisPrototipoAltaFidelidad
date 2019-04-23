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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class InformacionGeneral extends AppCompatActivity {


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla,contenedor;
    private SpeechRecognizer mySpeechRecognizer;

    private TextView TextViewContenido;
    private TextView TextViewTitulo;
        private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;
    private Button ButtonSiguiente;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        getSupportActionBar().hide();



        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        contenedor = (ConstraintLayout) findViewById(R.id.Contenedor);
        contenedor.bringToFront();


        TextViewContenido = (TextView) findViewById(R.id.TextViewInformacion);
        TextViewContenido.setMovementMethod(new ScrollingMovementMethod());

        TextViewTitulo = (TextView) findViewById(R.id.textView);

        ButtonSiguiente = (Button) findViewById(R.id.button3);



        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;



    ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(TextViewTitulo.getTextSize()<100) {
                    TextViewTitulo.setTextSize(0, TextViewTitulo.getTextSize() + 12.0f);
                    TextViewContenido.setTextSize(0, TextViewContenido.getTextSize() + 12.0f);
                    ButtonSiguiente.setTextSize(0, ButtonSiguiente.getTextSize() + 12.0f);

                }

            }


        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(TextViewTitulo.getTextSize()>76) {
                    TextViewTitulo.setTextSize(0, TextViewTitulo.getTextSize() - 12.0f);
                    TextViewContenido.setTextSize(0, TextViewContenido.getTextSize() -12.0f);
                    ButtonSiguiente.setTextSize(0, ButtonSiguiente.getTextSize() -12.0f);

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

                    TextViewContenido.setOnClickListener(new View.OnClickListener() {
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

        if(escuchado.indexOf("siguiente")!= -1){
            Intent intent = new Intent(this,SeleccionRol.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("conocer")!= -1){
              TextViewContenido.requestFocus();
              TextViewContenido.setBackgroundResource(R.drawable.borde_textview);
                myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(myTTS.getEngines().size()==0){
                            Toast.makeText(InformacionGeneral.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            myTTS.setLanguage(Locale.getDefault());
                            speak(TextViewContenido.getText().toString());

                        }
                    }
                });
            }else{
               speak("No entiendo esa orden! Por favor pulse la pantalla y pronuncie la palabra conocer, siguiente o repetir");
            }

        }
    }

    private void iniciarTextToSpeech() {
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
                            "conocer, . Caso contario, pronuncie la palabra siguiente. Si desea volver a escuchar las indicaciones, pulse sobre" +
                            " la parte superior derecha de la pantalla.");
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



    public void irASeleccionRol (View view)
    {

        Intent intent = new Intent(getApplicationContext(),SeleccionRol.class);
        startActivity(intent);
    }

        }










