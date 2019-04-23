package com.example.usuario.pruebas;

/**
 * Entidad ElementoCategoriaProducto
 */
public class ElementoProductosCompradosPorUsuario {
    private String conteoProducto;

    public ElementoProductosCompradosPorUsuario(String conteoProducto) {
        this.conteoProducto = conteoProducto;
    }

    public String getConteoProducto() {
        return conteoProducto;
    }

    public void setConteoProducto(String conteoProducto) {
        this.conteoProducto = conteoProducto;
    }

    @Override
    public String toString() {
        return "ConteoProducto {" +
                "Conteo : '" + conteoProducto +
                '}';
    }
}
