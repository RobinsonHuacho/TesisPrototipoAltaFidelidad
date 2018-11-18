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
public class ElementosCategoriaProductoAdaptador extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String []itemImagenes;
    private final String tipoImagen;


    public ElementosCategoriaProductoAdaptador(Activity context, String[] itemname, String tipoImagen,String []itemImagenes) {
        super(context, R.layout.item_categoria_producto, itemname);
        this.context=context;
        this.itemname = itemname;
        this.itemImagenes = itemImagenes;
        this.tipoImagen = tipoImagen;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_categoria_producto,null,true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.TextView_Nombre_Categoria);
        SmartImageView imageView= (SmartImageView) rowView.findViewById(R.id.ImageView_Categoria_Producto);
        txtTitle.setText(itemname[position]);
        Rect rect = new Rect(imageView.getLeft(),imageView.getTop(), imageView.getRight(),imageView.getBottom());
        imageView.setImageUrl("http://192.168.0.8:8080/ProyectoIntegrador/Images/"+tipoImagen+"/"+itemImagenes[position], rect);

        return rowView;

    }


}
