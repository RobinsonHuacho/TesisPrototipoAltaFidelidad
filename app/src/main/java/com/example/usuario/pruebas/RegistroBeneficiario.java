package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroBeneficiario extends AppCompatActivity {

    private EditText editTextCedulaBeneficiario;
    private EditText editTextPrimerNombreBeneficiario;
    private EditText editTextSegundoNombreBeneficiario;
    private EditText editTextPrimerApellidoBeneficiario;
    private EditText editTextSegundoApellidoBeneficiario;
    private EditText editTextDireccionBeneficiario;
    private EditText editTextTelefonoBeneficiario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_beneficiario);
        editTextCedulaBeneficiario = (EditText) findViewById(R.id.editText_CedulaBeneficiario);
        editTextCedulaBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextPrimerNombreBeneficiario = (EditText) findViewById(R.id.editText_PrimerNombreBeneficiario);
        editTextPrimerNombreBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextSegundoNombreBeneficiario = (EditText) findViewById(R.id.editText_SegundoNombreBeneficiario);
        editTextSegundoNombreBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextPrimerApellidoBeneficiario = (EditText) findViewById(R.id.editText_PrimerApellidoBeneficiario);
        editTextPrimerApellidoBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextSegundoApellidoBeneficiario = (EditText) findViewById(R.id.editText_SegundoApellidoBeneficiario);
        editTextSegundoApellidoBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextDireccionBeneficiario = (EditText) findViewById(R.id.editText_Direccion);
        editTextDireccionBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextTelefonoBeneficiario = (EditText) findViewById(R.id.editText_Telefono);
        editTextTelefonoBeneficiario.setBackgroundResource(R.drawable.border_color);
    }

    public void continuarRegistroBeneficiario (View view)
    {
        Intent intent = new Intent(this, RegistroBeneficiarioUsuarioContrasenia.class);

        if(TextUtils.isEmpty(editTextCedulaBeneficiario.getText().toString())){
            editTextCedulaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
            intent.putExtra("ErrorCedulaBeneficiarioCampoVacío","El campo CÉDULA no puede estar vacío. Por favor ingrese su cédula");
            return;
        }
        if(validadorDeCedula(editTextCedulaBeneficiario.getText().toString())==false){
            editTextCedulaBeneficiario.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
            return;
        }

        if(cedulaYaRegistrada(editTextCedulaBeneficiario.getText().toString())==true){
            editTextCedulaBeneficiario.setError("Cédula ya registrada!");
            return;
        }

        if(TextUtils.isEmpty(editTextPrimerNombreBeneficiario.getText().toString())){
            editTextPrimerNombreBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su Primer Nombre");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoNombreBeneficiario.getText().toString())){
            editTextSegundoNombreBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su Segundo Nombre");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoNombreBeneficiario.getText().toString())){
            editTextSegundoNombreBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su Primer Apellido");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoApellidoBeneficiario.getText().toString())){
            editTextSegundoApellidoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su Segundo Apellido");
            return;
        }
        if(TextUtils.isEmpty(editTextDireccionBeneficiario.getText().toString())){
            editTextDireccionBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su dirección");
            return;
        }
        if(TextUtils.isEmpty(editTextTelefonoBeneficiario.getText().toString())){
            editTextTelefonoBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese su teléfono");
            return;
        }

        intent.putExtra("cedulaBeneficiario",editTextCedulaBeneficiario.getText().toString());
        intent.putExtra("primerNombreBeneficiario",editTextPrimerNombreBeneficiario.getText().toString());
        intent.putExtra("segundoNombreBeneficiario",editTextSegundoNombreBeneficiario.getText().toString());
        intent.putExtra("primerApellidoficiario",editTextPrimerApellidoBeneficiario.getText().toString());
        intent.putExtra("segundoApellidoBeneficiario",editTextSegundoApellidoBeneficiario.getText().toString());
        intent.putExtra("direccionBeneficiario",editTextDireccionBeneficiario.getText().toString());
        intent.putExtra("telefonoBeneficiario",editTextTelefonoBeneficiario.getText().toString());
        startActivity(intent);
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

    public boolean cedulaYaRegistrada(final String cedula){
        final boolean[] resultado = {true};
        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.0.14:8080/ProyectoIntegrador/consultarCedulaYaRegistrada.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if(response.matches("1")){
                            resultado[0] = true;
                        }else{
                            resultado[0] = false;
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
                params.put("id_usuario", cedula);

                return params;
            }
        };
        queue.add(stringRequest);
        return resultado[0];
    }

}
