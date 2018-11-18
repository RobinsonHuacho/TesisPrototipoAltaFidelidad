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
public class ElementosProductosAdaptador extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] itemDescription;
    private final String []itemImagenes;
    private final String [] itemPrecios;
    private final String tipoImagen;

    public ElementosProductosAdaptador(Activity context, String[] itemname, String[] itemDescription, String[] itemPrecios, String tipoImagen,String []itemImagenes) {
        super(context, R.layout.item_producto, itemname);

        this.context=context;
        this.itemname=itemname;
        this.itemDescription=itemDescription;
        this.itemPrecios=itemPrecios;
        this.tipoImagen=tipoImagen;
        this.itemImagenes=itemImagenes;
    }

    public View getView(int posicion, View view, ViewGroup parent){

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_producto,null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_Nombre);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.TextView_Descripcion);
        TextView txtPrecio = (TextView) rowView.findViewById(R.id.TextView_Precio);

        SmartImageView imageView= (SmartImageView) rowView.findViewById(R.id.ImageView_Foto);
        txtTitle.setText(itemname[posicion]);
        txtDescription.setText(itemDescription[posicion]);
        txtPrecio.setText(itemPrecios[posicion]);
        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
        imageView.setImageUrl("http://192.168.0.8:8080/ProyectoIntegrador/Images/"+tipoImagen+"/"+itemImagenes[posicion], rect);

        return rowView;
    }
}
