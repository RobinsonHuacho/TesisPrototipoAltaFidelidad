package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InformacionGeneral extends AppCompatActivity implements TextToSpeech.OnInitListener{


    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    private TextView TextViewTitulo;
    private TextView TextViewContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        TextView tv = (TextView) findViewById(R.id.TextViewInformacion);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setBackgroundResource(R.drawable.borde_textview);
        getSupportActionBar().hide();

        TextViewTitulo = (TextView) findViewById(R.id.textView);
        TextViewContenido = (TextView) findViewById(R.id.TextViewInformacion);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

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
                //mySpeechRecognizer.startListening(speechIntent);

            }
        });


        iniciarTextToSpeech();
        iniciarSpeechToText();


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

    private void iniciarSpeechToText() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String escuchado = result.get(0);
                    prepararRespuesta(escuchado);
                }



                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void prepararRespuesta(String escuchado) {

        escuchado = escuchado.toLowerCase();

        if(escuchado.indexOf("siguiente")!=-1){
            Intent intent = new Intent(this,SeleccionRol.class);
            startActivity(intent);
        }else{
            if(escuchado.indexOf("conocer")!=-1){
                speak(TextViewTitulo.getText().toString()+"."+TextViewContenido.getText().toString());
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
                    //speak("Bienvenido!. En esta pantalla se presenta la información general de la aplicación. " +
                      //      "Si desea conocer mas, pulse sobre cualquier parte de la pantalla, y pronuncie la palabra, " +
                        //    "conocer, después del tono. Caso contario, pronuncie la palabra siguiente");
                    speak(TextViewTitulo.getText().toString());
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


    public void irASeleccionRol (View view)
    {

        Intent intent = new Intent(this,SeleccionRol.class);
        startActivity(intent);
    }


    @Override
    public void onInit(int status) {

    }
}
