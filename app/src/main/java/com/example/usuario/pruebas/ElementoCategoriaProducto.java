package com.example.usuario.pruebas;

/**
 * Created by Robinson Huacho on 16/06/2018.
 */

public class ElementoCategoriaProducto {
    //Componentes Categorías Productos
    private String IdCategoriaProducto;
    private String NombreCategoriaProducto;
    private String ImagenCategoriaProducto;

    public ElementoCategoriaProducto() {
    }

    public ElementoCategoriaProducto(String idCategoriaProducto, String nombreCategoriaProducto, String imagenCategoriaProducto) {
        IdCategoriaProducto = idCategoriaProducto;
        NombreCategoriaProducto = nombreCategoriaProducto;
        ImagenCategoriaProducto = imagenCategoriaProducto;
    }

    public String getIdCategoriaProducto() {
        return IdCategoriaProducto;
    }

    public void setIdCategoriaProducto(String idCategoriaProducto) {
        IdCategoriaProducto = idCategoriaProducto;
    }

    public String getNombreCategoriaProducto() {
        return NombreCategoriaProducto;
    }

    public void setNombreCategoriaProducto(String nombreCategoriaProducto) {
        NombreCategoriaProducto = nombreCategoriaProducto;
    }

    public String getImagenCategoriaProducto() {
        return ImagenCategoriaProducto;
    }

    public void setImagenCategoriaProducto(String imagenCategoriaProducto) {
        ImagenCategoriaProducto = imagenCategoriaProducto;
    }
}
