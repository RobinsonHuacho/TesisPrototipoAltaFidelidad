package com.example.usuario.pruebas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Intent.EXTRA_INDEX;

public class ListaCategoriaProducto extends AppCompatActivity {


    private GridView gridViewCategoriaProducto;
    private ArrayList<String> GridViewItems = new ArrayList<String>();
    private ArrayList<String> GridViewImagenes = new ArrayList<String>();
    private String[] arregloCategorias;
    DatabaseHandlerCategorias db = new DatabaseHandlerCategorias(this);
    ElementosCategoriaProductoAdaptador adapter;
    private String item;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private GridView GridView_Categoria_Productos;
    private TextView InformacionPantalla;
    private TextView TextView_Nombre_Categoria;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar,ImageButtonDesactivar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_producto);



        db = new DatabaseHandlerCategorias(getApplicationContext());
        db.addCategoria(new ElementoCategoriaProducto("1", "LACTEOS", "lacteos.png"));
        db.addCategoria(new ElementoCategoriaProducto("2", "VIVERES", "viveres.png"));
        db.addCategoria(new ElementoCategoriaProducto("3", "GRANOS", "granos.jpg"));
        db.addCategoria(new ElementoCategoriaProducto("4", "BEBIDAS", "bebidas.jpg"));
        db.addCategoria(new ElementoCategoriaProducto("5", "ASEO PERSONAL", "cuidado_personal.png"));



        GridViewItems = db.getAllCategorias(1);
        GridViewImagenes = db.getAllCategorias(2);

        arregloCategorias = GridViewItems.toArray(new String[0]);
        String[] arregloImagenes = GridViewImagenes.toArray(new String[0]);

        //Toast.makeText(getApplicationContext(),db.getAllCategorias(1).toString(),Toast.LENGTH_SHORT).show();
        adapter = new ElementosCategoriaProductoAdaptador(ListaCategoriaProducto.this, arregloCategorias, arregloImagenes,14);
        gridViewCategoriaProducto = (GridView) findViewById(R.id.GridView_Categoria_Productos);



        gridViewCategoriaProducto.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        gridViewCategoriaProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) gridViewCategoriaProducto.getItemAtPosition(position);
                final ElementoCategoriaProducto categoriaProducto = db.getCategoriaNombre(item);


                Intent intent = new Intent(getApplicationContext(), ListaProductos.class);
                intent.putExtra(EXTRA_INDEX, categoriaProducto.getIdCategoriaProducto());
                //Toast.makeText(getApplicationContext(),categoriaProducto.getIdCategoriaProducto(),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        InformacionPantalla=(TextView) findViewById(R.id.textView3);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        GridView_Categoria_Productos = (GridView) findViewById(R.id.GridView_Categoria_Productos);

        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()<90) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() + 12.0f);

                    arregloCategorias = GridViewItems.toArray(new String[0]);
                    String[] arregloImagenes = GridViewImagenes.toArray(new String[0]);

                    adapter = new ElementosCategoriaProductoAdaptador(ListaCategoriaProducto.this, arregloCategorias, arregloImagenes,22);
                    gridViewCategoriaProducto.setAdapter(adapter);

                }



            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()>66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);

                    arregloCategorias = GridViewItems.toArray(new String[0]);
                    String[] arregloImagenes = GridViewImagenes.toArray(new String[0]);

                    adapter = new ElementosCategoriaProductoAdaptador(ListaCategoriaProducto.this, arregloCategorias, arregloImagenes,18);
                    gridViewCategoriaProducto.setAdapter(adapter);


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
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie la categoría de producto!");
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

            item=escuchado;
            prepararRespuesta(escuchado);
        }
    }

    private void prepararRespuesta(String escuchado) {


        escuchado = Normalizer.normalize(escuchado, Normalizer.Form.NFD);
        escuchado = escuchado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        escuchado = escuchado.toUpperCase();

        try {
            final ElementoCategoriaProducto categoriaProducto = db.getCategoriaNombre(escuchado);

            Intent intent = new Intent(getApplicationContext(), ListaProductos.class);
            intent.putExtra(EXTRA_INDEX, categoriaProducto.getIdCategoriaProducto());
            //       Toast.makeText(getApplicationContext(),categoriaProducto.getIdCategoriaProducto(),Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }catch (Exception e){
            myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(myTTS.getEngines().size()==0){
                        Toast.makeText(ListaCategoriaProducto.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        myTTS.setLanguage(Locale.getDefault());
                        speak("Categoría no se encuentra en la lista. Por favor intente nuevamente!.");

                    }
                }
            });
        }

    }

    private void iniciarTextToSpeech() {
        myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(ListaCategoriaProducto.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presentan las diferentes "+InformacionPantalla.getText().toString()+"." +
                            ". Toque la pantalla y pronuncie la que desee ");

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











}