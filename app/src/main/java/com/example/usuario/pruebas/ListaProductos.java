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
import static android.content.Intent.EXTRA_REFERRER;
import static android.content.Intent.EXTRA_TITLE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ListaProductos extends AppCompatActivity {

    GridView gridView;
    private String idCategoriaProducto;
    private ArrayList<String> gridViewItemsID = new ArrayList<String>();
    private ArrayList<String> gridViewItemsNombre = new ArrayList<String>();
    private ArrayList<String> gridViewItemsDetalle = new ArrayList<String>();
    private ArrayList<String> gridViewItemsPrecio = new ArrayList<String>();
    private ArrayList<String> gridViewItemsImagen = new ArrayList<String>();

    private String selectedId, selectedImage,selectedItem, selectedPrecio;
    String[] arregloID;
    ElementosProductosAdaptador adapter;
    DatabaseHandlerProducto db = new DatabaseHandlerProducto(this);
    private String item;

    private static final int RECONOCEDOR_VOZ = 7;
    private TextToSpeech myTTS;
    private ConstraintLayout pantalla;
    private GridView GridView_Productos;
    private TextView InformacionPantalla,TextView_Precio$;
    private ImageButton ImageButtonZoomIn,ImageButtonZoomOut,ImageButtonActivar,ImageButtonDesactivar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        Intent intent = getIntent();
        idCategoriaProducto=intent.getStringExtra(EXTRA_INDEX);
        //Toast.makeText(getApplicationContext(),idCategoriaProducto, Toast.LENGTH_LONG).show();


             db.deleteProductos();



           DatabaseHandlerProducto db = new DatabaseHandlerProducto(getApplicationContext());
            db.addProductos(new ElementoProducto("1","1","LECHE","","1.5","leche.png"));
            db.addProductos(new ElementoProducto("5", "2", "ATUN", "", "0.5", "atun.jpg"));
            db.addProductos(new ElementoProducto("6", "2", "SARDINA", "", "1.25", "sardina.jpg"));
            db.addProductos(new ElementoProducto("7", "2", "ACEITE", "", "1.5", "aceite.jpg"));
            db.addProductos(new ElementoProducto("8", "2", "ARROZ", "", "2.5", "arroz.jpg"));
            db.addProductos(new ElementoProducto("9", "2", "AZUCAR", "", "2.75", "azucar.jpg"));
            db.addProductos(new ElementoProducto("10", "2", "FIDEOS", "", "1.75", "fideos.jpg"));
            db.addProductos(new ElementoProducto("11", "2", "GELATINA", "", "1.05", "gelatina.jpg"));
            db.addProductos(new ElementoProducto("12", "2", "HARINA", "", "2.25", "harina.jpg"));
            db.addProductos(new ElementoProducto("13", "2", "MAYONESA", "", "2.5", "mayonesa.jpg"));
            db.addProductos(new ElementoProducto("4", "2", "SAL", "", "1.65", "sal.jpg"));
            db.addProductos(new ElementoProducto("15", "2", "SALSA DE TOMATE", "", "1.75", "salsa_tomate.jpg"));
            db.addProductos(new ElementoProducto("16", "2", "SOPAS Y CREMAS", "", "0.85","sopa.jpg"));
            db.addProductos(new ElementoProducto("20", "2", "AVENA", "", "1.65", "avena.jpg"));
            db.addProductos(new ElementoProducto("26", "3", "ARVEJA", "", "1.25", "arvejas.jpg"));
            db.addProductos(new ElementoProducto("27", "3", "FREJOL", "", "1.25", "frejol.jpg"));
            db.addProductos(new ElementoProducto("28", "3", "LENTEJA", "", "1.25", "lentejas.jpg"));
            db.addProductos(new ElementoProducto("29", "3", "MAIZ", "", "1.25", "maiz.jpg"));
            db.addProductos(new ElementoProducto("32", "3", "GARBANZO", "", "1.25", "garbanzos.jpg"));
            db.addProductos(new ElementoProducto("33", "4", "CAFE", "", "1.75", "cafe.jpg"));
            db.addProductos(new ElementoProducto("34", "4", "AGUA", "", "1", "agua.jpg"));
            db.addProductos(new ElementoProducto("35", "4", "JUGO", "", "1.75", "jugos.jpg"));
            db.addProductos(new ElementoProducto("38", "5", "PASTA DE DIENTES", "", "2.25", "pasta_dientes.jpg"));
            db.addProductos(new ElementoProducto("39", "5", "JABON", "", "0.75", "jabon.jpg"));
            db.addProductos(new ElementoProducto("40", "5", "SHAMPOO", "", "3", "shampoo.png"));
            db.addProductos(new ElementoProducto("41", "5", "DETERGENTE", "", "1.75", "detergente.jpg"));
            db.addProductos(new ElementoProducto("42", "5", "CEPILLO DE DIENTES", "", "0.75", "cepilloDientes.jpg"));
            db.addProductos(new ElementoProducto("43", "5", "APEL HIGIENICO", "", "3.75", "papelHigienico.jpg"));
            db.addProductos(new ElementoProducto("44", "5", "DESODORANTE", "", "3.75", "desodorante.jpg"));


        gridViewItemsID = db.getAllProductos(0,idCategoriaProducto);
        gridViewItemsNombre = db.getAllProductos(2,idCategoriaProducto);
        gridViewItemsDetalle = db.getAllProductos(3,idCategoriaProducto);
        gridViewItemsPrecio = db.getAllProductos(4,idCategoriaProducto);
        gridViewItemsImagen = db.getAllProductos(5,idCategoriaProducto);

        //Toast.makeText(getApplicationContext(),gridViewItemsImagen.toString(), Toast.LENGTH_LONG).show();
        arregloID = gridViewItemsID.toArray(new String[0]);
        String[] arregloProductos = gridViewItemsNombre.toArray(new String[0]);
        String[] arregloDescripcion = gridViewItemsDetalle.toArray(new String[0]);
        String[] arregloImagenes = gridViewItemsImagen.toArray(new String[0]);
        String[] arregloPrecios = gridViewItemsPrecio.toArray(new String[0]);

        adapter = new ElementosProductosAdaptador(ListaProductos.this, arregloProductos, arregloDescripcion, arregloPrecios, "Productos", arregloImagenes,16);
        gridView = (GridView) findViewById(R.id.GridView_Productos);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                ConstraintLayout ll = (ConstraintLayout) view;
                                                TextView tv = (TextView) ll.findViewById(R.id.TextView_Nombre);
                                                TextView tv2 = (TextView) ll.findViewById(R.id.TextView_Precio);

                                                selectedId = arregloID[position].toString();
                                                selectedImage=arregloImagenes[position].toString();
                                                selectedItem = tv.getText().toString();
                                                selectedPrecio = tv2.getText().toString();

                                                Intent intent = new Intent(getApplicationContext(), AnadirDetalleCompra.class);

                                                intent.putExtra(EXTRA_INDEX, selectedId);
                                                intent.putExtra(EXTRA_TITLE,selectedImage);
                                                intent.putExtra(EXTRA_MESSAGE, selectedItem);
                                                intent.putExtra(Intent.EXTRA_TEXT, selectedPrecio);
                                                intent.putExtra(EXTRA_REFERRER, idCategoriaProducto);

                                                //Toast.makeText(getApplicationContext(),selectedId+" "+selectedItem+" "+selectedDescription+" "+selectedPrecio, Toast.LENGTH_LONG).show();

                                                startActivity(intent);
                                            }
                                        });







                InformacionPantalla=(TextView) findViewById(R.id.textView3);
        TextView_Precio$=(TextView) findViewById(R.id.textView3);

        pantalla = (ConstraintLayout) findViewById(R.id.Pantalla);
        GridView_Productos = (GridView) findViewById(R.id.GridView_Productos);
        GridView_Productos.setHorizontalScrollBarEnabled(true);
        GridView_Productos.setVerticalScrollBarEnabled(true);



        ImageButtonZoomIn= (ImageButton) findViewById(R.id.zoomIn) ;
        ImageButtonZoomOut= (ImageButton) findViewById(R.id.zoomOut) ;

        ImageButtonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()<90) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() + 12.0f);
                    TextView_Precio$.setTextSize(0, TextView_Precio$.getTextSize() + 12.0f);

                    String[] arregloProductos = gridViewItemsNombre.toArray(new String[0]);
                    String[] arregloDescripcion = gridViewItemsDetalle.toArray(new String[0]);
                    String[] arregloImagenes = gridViewItemsImagen.toArray(new String[0]);
                    String[] arregloPrecios = gridViewItemsPrecio.toArray(new String[0]);

                    adapter = new ElementosProductosAdaptador(ListaProductos.this, arregloProductos, arregloDescripcion, arregloPrecios, "Productos", arregloImagenes,22);
                    gridView.setAdapter(adapter);

                }




            }
        });

        ImageButtonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InformacionPantalla.getTextSize()>66) {
                    InformacionPantalla.setTextSize(0, InformacionPantalla.getTextSize() - 12.0f);
                    TextView_Precio$.setTextSize(0, TextView_Precio$.getTextSize() - 12.0f);

                    String[] arregloProductos = gridViewItemsNombre.toArray(new String[0]);
                    String[] arregloDescripcion = gridViewItemsDetalle.toArray(new String[0]);
                    String[] arregloImagenes = gridViewItemsImagen.toArray(new String[0]);
                    String[] arregloPrecios = gridViewItemsPrecio.toArray(new String[0]);

                    adapter = new ElementosProductosAdaptador(ListaProductos.this, arregloProductos, arregloDescripcion, arregloPrecios, "Productos", arregloImagenes,16);
                    gridView.setAdapter(adapter);

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
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronuncie el producto que desea comprar!");
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


        TextView tv = (TextView) findViewById(R.id.TextView_Nombre);
        TextView tv2 = (TextView) findViewById(R.id.TextView_Precio);

        try {
            selectedId = db.getIdProductoPorNombre(escuchado).getIdProducto().toString();

            selectedItem = tv.getText().toString();
            selectedPrecio = tv2.getText().toString();

            Intent intent = new Intent(getApplicationContext(), AnadirDetalleCompra.class);

            intent.putExtra(EXTRA_INDEX, selectedId);
            intent.putExtra(EXTRA_MESSAGE, selectedItem);
            intent.putExtra(Intent.EXTRA_TEXT, selectedPrecio);

            //Toast.makeText(getApplicationContext(), selectedId + " " + selectedItem + " " + selectedPrecio, Toast.LENGTH_LONG).show();

            startActivity(intent);
        }catch (Exception e){
            myTTS=new TextToSpeech(this,  new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(myTTS.getEngines().size()==0){
                        Toast.makeText(ListaProductos.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        myTTS.setLanguage(Locale.getDefault());
                        speak("Producto no se encuentra en la lista. Por favor intente nuevamente!.");

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
                    Toast.makeText(ListaProductos.this, "No se ha inicializado TextToSpeech en su dispositivo",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.getDefault());
                    speak("En esta pantalla se presentan los diferentes "+InformacionPantalla.getText().toString()+"." +
                            ". Toque la pantalla y pronuncie el que desee ");
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







