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

public class RegistroBeneficiarioUsuarioContrasenia extends AppCompatActivity {


    private String condicion_desde_seleccion_rol;
    private String condicionBeneficiario;
    private String cedulaBeneficiario;
    private String primerNombreBeneficiario;
    private String segundoNombreBeneficiario;
    private String primerApellidoBeneficiario;
    private String segundoApellidoBeneficiario;
    private String direccionBeneficiario;
    private String telefonoBeneficiario;

    private EditText editTextUsuarioBeneficiario;
    private EditText editTextContraseniaBeneficiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_beneficiario_usuario_contrasenia);

        Intent intent = getIntent();
        condicion_desde_seleccion_rol = SeleccionRol.getActivityInstance().getCondicion();
        cedulaBeneficiario = intent.getStringExtra("cedulaBeneficiario");
        primerNombreBeneficiario = intent.getStringExtra("primerNombreBeneficiario");
        segundoNombreBeneficiario = intent.getStringExtra("segundoNombreBeneficiario");
        primerApellidoBeneficiario = intent.getStringExtra("primerApellidoficiario");
        segundoApellidoBeneficiario = intent.getStringExtra("segundoApellidoBeneficiario");
        direccionBeneficiario = intent.getStringExtra("direccionBeneficiario");
        telefonoBeneficiario = intent.getStringExtra("telefonoBeneficiario");

        //Toast.makeText(getApplicationContext(), cedulaBeneficiario+" "+primerNombreBeneficiario+" "+segundoNombreBeneficiario+" "+primerApellidoBeneficiario+" "+segundoApellidoBeneficiario+" "+direccionBeneficiario+" "+telefonoBeneficiario, Toast.LENGTH_LONG).show();

        editTextUsuarioBeneficiario = (EditText) findViewById(R.id.editText_UsuarioBeneficiario);
        editTextUsuarioBeneficiario.setBackgroundResource(R.drawable.border_color);

        editTextContraseniaBeneficiario = (EditText) findViewById(R.id.editText_ContraseniaBeneficiario);
        editTextContraseniaBeneficiario.setBackgroundResource(R.drawable.border_color);

    }

    public void RegistrarBeneficiario(View view){
        if(TextUtils.isEmpty(editTextUsuarioBeneficiario.getText().toString())){
            editTextUsuarioBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese un usuario");
            return;
        }

        if(TextUtils.isEmpty(editTextContraseniaBeneficiario.getText().toString())){
            editTextContraseniaBeneficiario.setError("Este campo no puede estar vacío. Por favor ingrese una contraseña");
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.0.4:8080/ProyectoIntegrador/nuevoUsuario.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                EditText editTextUsuario = (EditText) findViewById(R.id.editText_UsuarioBeneficiario);
                EditText editTextContraseña = (EditText) findViewById(R.id.editText_ContraseniaBeneficiario);

                Map<String,String> params = new HashMap<String, String>();
                params.put("rol", SeleccionRol.getActivityInstance().getIdRol());
                params.put("cedula", cedulaBeneficiario);
                params.put("primer_nombre", primerNombreBeneficiario);
                params.put("segundo_nombre", segundoNombreBeneficiario);
                params.put("primer_apellido", primerApellidoBeneficiario);
                params.put("segundo_apellido", segundoApellidoBeneficiario);
                params.put("condicion", SeleccionRol.getActivityInstance().getCondicion());
                params.put("direccion", direccionBeneficiario);
                params.put("telefono", telefonoBeneficiario);
                params.put("email","");
                params.put("usuario", editTextUsuario.getText().toString());
                params.put("password", editTextContraseña.getText().toString());

                return params;
            }
        };
        queue.add(stringRequest);

        Intent intent1 = new Intent(this, IngresoAplicacion.class);
        startActivity(intent1);

    }
}
