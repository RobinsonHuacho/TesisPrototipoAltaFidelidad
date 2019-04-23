package com.example.usuario.pruebas;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;

/**
 * Adaptador de leads
 */
public class ElementosProductosCompradosAdaptador extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] itemCantidad;
    private final String []itemImagenes;
    private final String [] itemTotal;
    private final String tipoImagen;
    private float tamanoTexto;


    public ElementosProductosCompradosAdaptador(Activity context, String[] itemname, String[] itemCantidad, String[] itemTotal, String tipoImagen, String []itemImagenes,float tamanoTexto) {
        super(context, R.layout.item_lista_detalle_beneficiario, itemname);

        this.context=context;
        this.itemname=itemname;
        this.itemCantidad=itemCantidad;
        this.itemTotal=itemTotal;
        this.tipoImagen=tipoImagen;
        this.itemImagenes=itemImagenes;
        this.tamanoTexto=tamanoTexto;
    }

    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_lista_detalle_beneficiario,null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_lbNombreProducto);
        TextView txtCantidad = (TextView) rowView.findViewById(R.id.TextView_Cantidad);
        TextView txtTotal = (TextView) rowView.findViewById(R.id.TextView_TotalDetalleProducto);

        TextView LabelSignoDolares = (TextView) rowView.findViewById(R.id.TextView_TotalDetalleProducto_$);
        TextView LabelCantidad = (TextView) rowView.findViewById(R.id.TextView_lbCantidad);
        TextView LabelTotal = (TextView) rowView.findViewById(R.id.TextView_lbTotalDetalleProducto);



        SmartImageView imageView= (SmartImageView) rowView.findViewById(R.id.ImageView_Foto);

        txtTitle.setText(itemname[posicion]);
        txtCantidad.setText(itemCantidad[posicion]);
        txtTotal.setText(itemTotal[posicion]);

        txtTitle.setTextSize(tamanoTexto);
        txtCantidad.setTextSize(tamanoTexto);
        txtTotal.setTextSize(tamanoTexto);

        LabelSignoDolares.setTextSize(tamanoTexto);
        LabelCantidad.setTextSize(tamanoTexto);
        LabelTotal.setTextSize(tamanoTexto);



        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
        imageView.setImageUrl("http://192.168.0.10:8080/ProyectoIntegrador/Images/"+tipoImagen+"/"+itemImagenes[posicion], rect);

        return rowView;
    }
}
