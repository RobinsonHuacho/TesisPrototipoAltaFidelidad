package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ErrorAnadirProducto extends AppCompatActivity {

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView TextoResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_anadir_producto);

        getSupportActionBar().hide();


        TextoResultado = (TextView) findViewById(R.id.textViewError);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);

        pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la frase Intentar nuevamente para regresar el detalle de su " +
                        "compra hasta ahora!");
                startActivityForResult(speechIntent,RECONOCEDOR_VOZ);


            }
        });



        iniciarTextToSpeech();
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
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {

        escuchado = escuchado.toLowerCase();

        if(escuchado.indexOf("intentar")!=-1){
            Intent intent1 = new Intent(this, DetallesBeneficiario.class);
            startActivity(intent1);
        }
    }


    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ErrorAnadirProducto.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak(TextoResultado.getText().toString()+". Pronuncie la frase, Intentar nuevamente para regresar el detalle de su " +
                                    "compra hasta ahora!");
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

    public void regresarDetalleBeneficiario(View view){
        Intent intent1 = new Intent(this, DetallesBeneficiario.class);
        startActivity(intent1);
    }

}