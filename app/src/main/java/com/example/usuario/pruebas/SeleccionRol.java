package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SeleccionRol extends AppCompatActivity {
    private static final int MY_DATA_CHECK_CODE = 1;
    private DatabaseHandlerCategorias db;
    private static SeleccionRol INSTANCE;
    private String id_rol, condicion;

    private Button btnDiscapacidadVisual, btnDiscapacidadFisica, btnAdultoMayor;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private SpeechRecognizer mySpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_rol);

         INSTANCE=this;

        db = new DatabaseHandlerCategorias(getApplicationContext());
        db.deleteCategorias();


        btnDiscapacidadVisual= (Button) findViewById(R.id.buttonDiscapacidadVisual);
        btnDiscapacidadVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                startActivity(intent);

                id_rol="1";
                Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);

                condicion=btnDiscapacidadVisual.getText().toString();
                Intent intent2 = new Intent("PASO_CONDICION").putExtra("CONDICION", condicion);
                //Toast.makeText(getApplicationContext(),condicion,Toast.LENGTH_LONG).show();
            }
        });

        btnDiscapacidadFisica= (Button) findViewById(R.id.buttonDiscapacidadFisica);
        btnDiscapacidadFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                startActivity(intent);

                id_rol="1";
                Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);

                condicion=btnDiscapacidadFisica.getText().toString();
                Intent intent2 = new Intent("PASO_CONDICION").putExtra("CONDICION", condicion);
                //Toast.makeText(getApplicationContext(),condicion, Toast.LENGTH_LONG).show();

            }
        });

        btnAdultoMayor= (Button) findViewById(R.id.buttonAdultoMayor);
        btnAdultoMayor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                startActivity(intent);

                id_rol="1";
                Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);

                condicion=btnAdultoMayor.getText().toString();
                Intent intent2 = new Intent("PASO_CONDICION").putExtra("CONDICION", condicion);
                //Toast.makeText(getApplicationContext(),condicion,Toast.LENGTH_LONG).show();
            }
        });

        ImageButtonActivar=(ImageButton) findViewById(R.id.btnHabiltarTTSSTT);
                ImageButtonDesactivar=(ImageButton)findViewById(R.id.btnDeshabiltarTTSSTT);


        ImageButtonActivar.setVisibility(View.VISIBLE);
        ImageButtonDesactivar.setVisibility(View.GONE);


        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();


                ImageButtonActivar.setVisibility(View.GONE);
                ImageButtonDesactivar.setVisibility(View.VISIBLE);
            }
        });


        ImageButtonDesactivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myTTS.stop();
                myTTS.shutdown();



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

        if(escuchado.indexOf("ceguera")!=-1){
            Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
            startActivity(intent);

            id_rol="1";
            Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
        }else{
            if(escuchado.indexOf("visual")!=-1){
                Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                startActivity(intent);

                id_rol="1";
                Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
            }else{
                if(escuchado.indexOf("física")!=-1){
                    Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                    startActivity(intent);

                    id_rol="1";
                    Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                }else {
                    if (escuchado.indexOf("mayor") != -1) {
                        Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                        startActivity(intent);

                        id_rol="1";
                        Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                    } else {
                        if (escuchado.indexOf("anciano") != -1) {
                            Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                            startActivity(intent);

                            id_rol="1";
                            Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                            }else{
                            if (escuchado.indexOf("tercera") != -1) {
                                Intent intent = new Intent(getApplicationContext(),InformacionGeneralBeneficiario.class);
                                startActivity(intent);

                                id_rol="1";
                                Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                            }else{
                                if (escuchado.indexOf("donador") != -1) {
                                    Intent intent = new Intent(this,InformacionGeneralDonador.class);
                                    startActivity(intent);

                                    id_rol="2";
                                    Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                            }else{
                                    if (escuchado.indexOf("ayudar") != -1) {
                                        Intent intent = new Intent(this,InformacionGeneralDonador.class);
                                        startActivity(intent);

                                        id_rol="2";
                                        Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
                                }else{
                                        if (escuchado.indexOf("repetir") != -1) {
                                            iniciarTextToSpeech();


                                    }else {
                                        speak("No entiendo esa orden! Por favor pulse la pantalla y pronuncie alguna de la siguientes" +
                                                " condiciones: "+btnDiscapacidadVisual.getText().toString()+","+btnDiscapacidadFisica.getText().toString()
                                                +", o "+btnAdultoMayor.getText().toString()+". Caso contrario si desea ayudar a personas con " +
                                                " estas condiciones pruebe acceder como donador.");
                                        }
                            }

                            }
                            }
                }
                }
        }
    }
        }
        }


    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(SeleccionRol.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("Bienvenido a Dona y ayuda!. En esta pantalla puede escoger " +
                            "utilizar la aplicación como beneficiario o donador. Beneficiario si tiene " +
                            "alguna de las siguientes condiciones: "+btnDiscapacidadVisual.getText().toString()+","+btnDiscapacidadFisica.getText().toString()
                            +", o "+btnAdultoMayor.getText().toString()+". Caso contrario si desea ayudar a personas con " +
                            " estas condiciones puede acceder como donador. Pulse la pantalla y pronuncie alguna de las anteriores condiciones. Caso contario, si desea volver a escuchar las indicaciones, pronuncie la palabra," +
                            " repetir.");

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

    public void invocarActividadDonador(View view)
    {
        Intent intent = new Intent(this,InformacionGeneralDonador.class);
        startActivity(intent);

        id_rol="2";
        Intent intent1 = new Intent("PASO_ID_ROL").putExtra("ID_ROL", id_rol);
    }

    public static SeleccionRol getActivityInstance() {
        return INSTANCE;
    }

    public String getIdRol()
    {
        return this.id_rol;
    }

    public String getCondicion()
    {
        return this.condicion;
    }
}
