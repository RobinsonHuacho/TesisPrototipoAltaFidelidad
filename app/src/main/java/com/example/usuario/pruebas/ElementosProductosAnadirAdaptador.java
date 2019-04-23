package com.example.usuario.pruebas;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;

/**
 * Adaptador de leads
 */
public class ElementosProductosAnadirAdaptador {

    private final Activity context;
    private final String itemname;
    private final String itemImagenes;
    private final String itemPrecios;
    private final String tipoImagen;
    private final String cantidad;

    private float tamanoTexto;

    public ElementosProductosAnadirAdaptador(Activity context, String itemname, String itemImagenes, String itemPrecios, String tipoImagen, String cantidad, float tamanoTexto) {
        this.context = context;
        this.itemname = itemname;
        this.itemImagenes = itemImagenes;
        this.itemPrecios = itemPrecios;
        this.tipoImagen = tipoImagen;
        this.cantidad = cantidad;
        this.tamanoTexto = tamanoTexto;
    }



    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_producto_detalle_beneficiario,null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_Nombre);
        TextView txtPrecio = (TextView) rowView.findViewById(R.id.TextView_Precio);
        TextView labelPrecio = (TextView) rowView.findViewById(R.id.Label_Precio);
        EditText Label_SaldoCompra=(EditText) rowView.findViewById(R.id.Label_SaldoCompra);

        SmartImageView imageView= (SmartImageView) rowView.findViewById(R.id.ImageView_Foto);
        txtTitle.setText(itemname);
        txtPrecio.setText(itemPrecios);
        Label_SaldoCompra.setText(cantidad);

        txtTitle.setTextSize(tamanoTexto);
        txtPrecio.setTextSize(tamanoTexto);
        labelPrecio.setTextSize(tamanoTexto);


        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
        imageView.setImageUrl("http://192.168.0.10:8080/ProyectoIntegrador/Images/"+tipoImagen+"/"+itemImagenes, rect);



        return rowView;
    }


}
