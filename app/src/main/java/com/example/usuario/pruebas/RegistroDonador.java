package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegistroDonador extends AppCompatActivity {


    private EditText editTextCedulaDonador;
    private EditText editTextPrimerNombreDonador;
    private EditText editTextSegundoNombreDonador;
    private EditText editTextPrimerApellidoDonador;
    private EditText editTextSegundoApellidoDonador;
    private EditText editTextEmailDonador;
    private EditText editTextUsuarioDonador;
    private EditText editTextPasswordDonador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_donador);
        editTextCedulaDonador = (EditText) findViewById(R.id.editText_CedulaDonador);
        editTextCedulaDonador.setBackgroundResource(R.drawable.border_color);

        editTextPrimerNombreDonador = (EditText) findViewById(R.id.editText_PrimerNombreDonador);
        editTextPrimerNombreDonador.setBackgroundResource(R.drawable.border_color);

        editTextSegundoNombreDonador = (EditText) findViewById(R.id.editText_SegundoNombreDonador);
        editTextSegundoNombreDonador.setBackgroundResource(R.drawable.border_color);

        editTextPrimerApellidoDonador = (EditText) findViewById(R.id.editText_PrimerApellidoDonador);
        editTextPrimerApellidoDonador.setBackgroundResource(R.drawable.border_color);

        editTextSegundoApellidoDonador = (EditText) findViewById(R.id.editText_SegundoApellidoDonador);
        editTextSegundoApellidoDonador.setBackgroundResource(R.drawable.border_color);

        editTextEmailDonador = (EditText) findViewById(R.id.editText_Email);
        editTextEmailDonador.setBackgroundResource(R.drawable.border_color);

        editTextUsuarioDonador = (EditText) findViewById(R.id.editText_Usuario);
        editTextUsuarioDonador.setBackgroundResource(R.drawable.border_color);

        editTextPasswordDonador = (EditText) findViewById(R.id.editText_Contrasenia);
        editTextPasswordDonador.setBackgroundResource(R.drawable.border_color);


    }

    public void makeRequestRegistrarDonador (View view){
        if(TextUtils.isEmpty(editTextCedulaDonador.getText().toString())){
            editTextCedulaDonador.setError("Este campo no puede estar vacío. Por favor ingrese su cédula");
            return;
        }
        if(validadorCedula(editTextCedulaDonador.getText().toString())==false){
           editTextCedulaDonador.setError("Cédula incorrecta. Por favor ingrese su cédula nuevamente");
           return;
        }

        if(cedulaYaRegistrada(editTextCedulaDonador.getText().toString())==true){
            editTextCedulaDonador.setError("Cédula ya registrada!");
            return;
        }

        if(TextUtils.isEmpty(editTextPrimerNombreDonador.getText().toString())){
            editTextPrimerNombreDonador.setError("Este campo no puede estar vacío. Por favor ingrese su Primer Nombre");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoNombreDonador.getText().toString())){
            editTextSegundoNombreDonador.setError("Este campo no puede estar vacío. Por favor ingrese su Segundo Nombre");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoNombreDonador.getText().toString())){
            editTextSegundoNombreDonador.setError("Este campo no puede estar vacío. Por favor ingrese su Primer Apellido");
            return;
        }
        if(TextUtils.isEmpty(editTextSegundoApellidoDonador.getText().toString())){
            editTextSegundoApellidoDonador.setError("Este campo no puede estar vacío. Por favor ingrese su Segundo Apellido");
            return;
        }
        if(TextUtils.isEmpty(editTextEmailDonador.getText().toString())){
            editTextEmailDonador.setError("Este campo no puede estar vacío. Por favor ingrese su dirección de correo electrónico");
            return;
        }
        if(validadorMail(editTextEmailDonador.getText().toString())==false){
            editTextEmailDonador.setError("Correo electrónico no válido. Por favor ingrese nuevamente un correo electrónico válido");
            return;
        }

        if(TextUtils.isEmpty(editTextUsuarioDonador.getText().toString())){
            editTextUsuarioDonador.setError("Este campo no puede estar vacío. Por favor ingrese un usuario");
            return;
        }

        if(TextUtils.isEmpty(editTextPasswordDonador.getText().toString())){
            editTextPasswordDonador.setError("Este campo no puede estar vacío. Por favor ingrese una contraseña");
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.0.4:8080/ProyectoIntegrador/nuevoUsuario.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                EditText editTextCedula = (EditText) findViewById(R.id.editText_CedulaDonador);
                EditText editTextPrimerNombre = (EditText) findViewById(R.id.editText_PrimerNombreDonador);
                EditText editTextSegundoNombre = (EditText) findViewById(R.id.editText_SegundoNombreDonador);
                EditText editTextPrimerApellido = (EditText) findViewById(R.id.editText_PrimerApellidoDonador);
                EditText editTextSegundoApellido = (EditText) findViewById(R.id.editText_SegundoApellidoDonador);
                EditText editTextEmail = (EditText) findViewById(R.id.editText_Email);
                EditText editTextUsuario = (EditText) findViewById(R.id.editText_Usuario);
                EditText editTextContraseña = (EditText) findViewById(R.id.editText_Contrasenia);


                Map<String,String> params = new HashMap<String, String>();
                params.put("rol", SeleccionRol.getActivityInstance().getIdRol());
                params.put("cedula", editTextCedula.getText().toString());
                params.put("primer_nombre", editTextPrimerNombre.getText().toString());
                params.put("segundo_nombre", editTextSegundoNombre.getText().toString());
                params.put("primer_apellido", editTextPrimerApellido.getText().toString());
                params.put("segundo_apellido", editTextSegundoApellido.getText().toString());
                params.put("condicion", "NO APLICA");
                params.put("direccion", "");
                params.put("telefono", "");
                params.put("email", editTextEmail.getText().toString());
                params.put("usuario", editTextUsuario.getText().toString());
                params.put("password", editTextContraseña.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

        Intent intent = new Intent(this, IngresoAplicacion.class);
        startActivity(intent);
    }

    public boolean validadorCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // LongitudCedula
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

    public boolean validadorMail(String email){
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
        String url3 ="http://192.168.0.4:8080/ProyectoIntegrador/consultarCedulaYaRegistrada.php";
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
