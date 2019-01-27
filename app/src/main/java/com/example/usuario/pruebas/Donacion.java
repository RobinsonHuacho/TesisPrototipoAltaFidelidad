package com.example.usuario.pruebas;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.pruebas.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Locale;

import static android.content.Intent.EXTRA_INDEX;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Donacion extends AppCompatActivity {

    private ValidadorTarjetaCredito validadorTarjetaCredito;

    private EditText EditTextMonto;

    private DatabaseHandlerCompras db1;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private TextView InformacionPantalla;
    private ImageButton ImageButtonActivar,ImageButtonDesactivar,ImageButtonZoomIn,ImageButtonZoomOut;

    private static final int PAYPAL_REQUEST_CODE=7171;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button buttonPagar;
    String monto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donacion);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        buttonPagar = (Button) findViewById(R.id.Btn_Continuar);

        EditTextMonto=(EditText) findViewById(R.id.EditText_Monto);
        EditTextMonto.setBackgroundResource(R.drawable.border_color);

        InformacionPantalla=(TextView) findViewById(R.id.textView14);

        ImageButtonActivar= (ImageButton) findViewById(R.id.btnHabiltarTTSSTT) ;
        ImageButtonDesactivar= (ImageButton) findViewById(R.id.btnDeshabiltarTTSSTT) ;


        ImageButtonActivar.setVisibility(View.VISIBLE);
        ImageButtonDesactivar.setVisibility(View.GONE);

        ImageButtonActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarTextToSpeech();



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

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                pantalla.setScaleX((float) (x + 1));
                pantalla.setScaleY((float) (y + 1));




            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pantalla.getScaleX();
                float y = pantalla.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality

                pantalla.setScaleX((float) (x - 1));
                pantalla.setScaleY((float) (y - 1));



            }
        });


        buttonPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(EditTextMonto.getText().toString())){
                    EditTextMonto.setError("Este campo no puede estar vacío. Por favor ingrese un monto desde 1 dólar");
                    return;
                }
                procesarPago();

            }
        });

    }

    private void procesarPago() {

        //Intent intent = getIntent();
        //String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);

        monto=EditTextMonto.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)),"USD","Donar:",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Intent intent = getIntent();
        final String messageIdCompra = intent.getStringExtra(EXTRA_INDEX);
        final String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);

        if(requestCode == PAYPAL_REQUEST_CODE ){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation != null){
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,ConfirmacionDonacion.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                .putExtra("PaymentAmount",EditTextMonto.getText().toString())
                                .putExtra(Intent.EXTRA_INDEX, messageIdCompra)
                                .putExtra(EXTRA_MESSAGE, messageIdUsuario)
                        );

                        Toast.makeText(this,monto,Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            }else if(resultCode== Activity.RESULT_CANCELED){
            Toast.makeText(this, "Cancelada",Toast.LENGTH_SHORT).show();

            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Inválida",Toast.LENGTH_SHORT).show();

        }



    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(Donacion.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla "+InformacionPantalla.getText().toString()+". Posteriormente, de clic en el botón Confirmar donación," +
                            "para continuar a la interfaz de donación. Caso contrario, clic en el botón regresar para volver" +
                            "a los detalles!.");
                    EditTextMonto.requestFocus();
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



   /* public void makeRequestInsertarDonacion(View view){

        if(TextUtils.isEmpty(EditTextMonto.getText().toString())){
            EditTextMonto.setError("Este campo no puede estar vacío. Por favor ingrese un monto desde 1 dólar");
            return;
        }

        final Intent intent = getIntent();
        final String messageIdCompra = intent.getStringExtra(EXTRA_INDEX);
        final String messageIdUsuario = intent.getStringExtra(EXTRA_MESSAGE);
        //Toast.makeText(getApplicationContext(),messageIdCompra,Toast.LENGTH_LONG).show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.4:8080/ProyectoIntegrador/nuevaDonacion.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.matches("1")){
                            Toast.makeText(getApplicationContext(),"Donación Realizada Exitosamente",Toast.LENGTH_LONG).show();
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

                Map<String,String> params = new HashMap<String, String>();
                params.put("id_compra", messageIdCompra);
                params.put("id_usuario", messageIdUsuario);
                params.put("monto", EditTextMonto.getText().toString());



                return params;
            }
        };
        queue.add(stringRequest);


        Intent intent1 = new Intent(this, ConfirmacionDonacion.class);
        startActivity(intent1);


        db1 = new DatabaseHandlerCompras(getApplicationContext());
        db1.deleteProductos();

    }*/



    public void volverCatalogo(View view){
        Intent intent = new Intent(this, DetallesDonante.class);
        startActivity(intent);
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

        stopService(new Intent(this,PayPalService.class));

        if(myTTS!=null){
            myTTS.stop();
            myTTS.shutdown();
        }


}

}





