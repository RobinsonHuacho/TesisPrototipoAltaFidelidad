package com.example.usuario.pruebas;

/**
 * Entidad ElementoBeneficiarioDonacion
 */
public class ElementoBeneficiarioDonacion {
    private String Id;
    private String Nombres;
    private double TotalCompra;
    private double Saldo;
    private int Imagen;

    private String IdProducto;
    private String IdCategoriaProducto;
    private int Cantidad;
    private String Descripcion;
    private double Precio;
    private double Total;



    public ElementoBeneficiarioDonacion(String idBeneficiario, String nombres, double totalCompra, double saldo, int imagen) {
        Id = idBeneficiario;
        Nombres = nombres;
        TotalCompra = totalCompra;
        Saldo = saldo;
        Imagen = imagen;
    }

    public ElementoBeneficiarioDonacion(String idProducto, String idCategoriaProducto, String nombres, String descripcion, double precio, int cantidad, int imagen){
        IdProducto = idProducto;
        IdCategoriaProducto= idCategoriaProducto;
        Nombres = nombres;
        Descripcion=descripcion;
        Precio=precio;
        Cantidad=cantidad;
        Imagen=imagen;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        this.Nombres = nombres;
    }

    public double getTotalCompra() {
        return TotalCompra;
    }

    public void setTotalCompra(double totalCompra) { this.TotalCompra = totalCompra; }

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        this.Saldo = saldo;
    }

    public String getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(String idProducto) {
        IdProducto = idProducto;
    }

    public String getIdCategoriaProducto() {
        return IdCategoriaProducto;
    }

    public void setIdCategoriaProducto(String idCategoriaProducto) {
        IdCategoriaProducto = idCategoriaProducto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getTotal() {
        return Math.round(Cantidad*Precio*100d)/100d;
    }


    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        this.Imagen = imagen;
    }

    @Override
    public String toString() {
        return "Beneficiario {" +
                "ID : '" + Id + '\'' +
                ", Nombres : '" + Nombres + '\'' +
                ", Total Compra : '" + TotalCompra + '\'' +
                ", Saldo : '" + Saldo + '\'' +
                '}';
    }
}
