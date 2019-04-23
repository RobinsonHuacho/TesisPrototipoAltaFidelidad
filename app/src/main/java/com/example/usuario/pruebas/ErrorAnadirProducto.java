package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.EXTRA_INDEX;

public class ErrorAnadirProducto extends AppCompatActivity {

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView TextoResultado;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar,ImageButtonDesactivar;
    private String idUsuario;
    private Button btnIntentarNuevamente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_anadir_producto);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        idUsuario=intent.getStringExtra(EXTRA_INDEX);

        TextoResultado = (TextView) findViewById(R.id.textViewError);
        btnIntentarNuevamente = (Button) findViewById(R.id.button);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);



        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(TextoResultado.getTextSize()<75) {
                    TextoResultado.setTextSize(0, TextoResultado.getTextSize() + 12.0f);
                    btnIntentarNuevamente.setTextSize(0, btnIntentarNuevamente.getTextSize() + 12.0f);

                }

            }


        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(btnIntentarNuevamente.getTextSize()>66) {
                    TextoResultado.setTextSize(0, TextoResultado.getTextSize() - 12.0f);
                    btnIntentarNuevamente.setTextSize(0, btnIntentarNuevamente.getTextSize() -12.0f);


                }
            }
        });


        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;
        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();

                pantalla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
                        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la frase Volver a intentar para seleccionar otro producto! O Ver detalles para conocer " +
                                "los productos agregados hasta el momento!");
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

        if(escuchado.indexOf("volver")!=-1){
            Intent intent = new Intent(this,ListaCategoriaProducto.class);
            startActivity(intent);

        }

        if(escuchado.indexOf("volver")!=-1){
            Intent intent = new Intent(this,ListaCategoriaProducto.class);
            startActivity(intent);

        }

        if(escuchado.indexOf("ver")!=-1){
            Intent intent = new Intent(this,DetallesDonante.class);
            startActivity(intent);

        }

        if(escuchado.indexOf("detalles")!=-1){
            Intent intent = new Intent(this,DetallesDonante.class);
            startActivity(intent);

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
                    speak(TextoResultado.getText().toString()+". Pronuncie la frase, Volver a intentar!. Posterior a esto se presentará" +
                            " la pantalla para seleccionar una categoría de producto nuevamente! O pronuncie la frase, Ver detalles, para conocer" +
                            " los productos añadadidos hasta el momento!");
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

    public void vovlerAIntentar(View view){
        Intent intent = new Intent(this,ListaCategoriaProducto.class);
        startActivity(intent);
    }

    public void verDetalles(View view){
        Intent intent = new Intent(this,DetallesBeneficiario.class);
        startActivity(intent);
    }
}
