package com.example.usuario.pruebas;

/**
 * Created by Robinson Huacho on 14/08/17.
 */

public class ElementoDetalleCompra {

    String _idDetalle;
    String _idCompra;
    String _idProducto;
    String _idUsuario;
    String _precioVenta;
    String _cantidadDetalle;
    String _totalDetalle;
    String _estado;

    public ElementoDetalleCompra(String _idDetalle, String _idCompra, String _idProducto, String _idUsuario, String _precioVenta, String _cantidadDetalle, String _totalDetalle, String _estado) {
        this._idDetalle = _idDetalle;
        this._idCompra = _idCompra;
        this._idProducto = _idProducto;
        this._idUsuario = _idUsuario;
        this._precioVenta = _precioVenta;
        this._cantidadDetalle = _cantidadDetalle;
        this._totalDetalle = _totalDetalle;
        this._estado = _estado;
    }

    public String get_idDetalle() {
        return _idDetalle;
    }

    public void set_idDetalle(String _idDetalle) {
        this._idDetalle = _idDetalle;
    }

    public String get_idCompra() {
        return _idCompra;
    }

    public void set_idCompra(String _idCompra) {
        this._idCompra = _idCompra;
    }

    public String get_idProducto() {
        return _idProducto;
    }

    public void set_idProducto(String _idProducto) {
        this._idProducto = _idProducto;
    }

    public String get_idUsuario() {
        return _idUsuario;
    }

    public void set_idUsuario(String _idUsuario) {
        this._idUsuario = _idUsuario;
    }

    public String get_precioVenta() {
        return _precioVenta;
    }

    public void set_precioVenta(String _precioVenta) {
        this._precioVenta = _precioVenta;
    }

    public String get_cantidadDetalle() {
        return _cantidadDetalle;
    }

    public void set_cantidadDetalle(String _cantidadDetalle) {
        this._cantidadDetalle = _cantidadDetalle;
    }

    public String get_totalDetalle() {
        return _totalDetalle;
    }

    public void set_totalDetalle(String _totalDetalle) {
        this._totalDetalle = _totalDetalle;
    }

    public String get_estado() {
        return _estado;
    }

    public void set_estado(String _estado) {
        this._estado = _estado;
    }
}
