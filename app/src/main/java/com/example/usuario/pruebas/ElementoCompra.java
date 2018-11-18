package com.example.usuario.pruebas;
/**

 /**
 * Created by Robinson Huacho on 14/08/17.
 */

public class ElementoCompra {

    String _idCompra;
    String _idUsuario;
    String _fechaCompra;
    String _totalCompra;
    String _saldoCompra;
    String _estadoCompra;
    String _idRol;
    String _fotoUsuario;
    String _primerNombreUsuario;
    String _segundoNombreUsuario;
    String _primerApellidoUsuario;
    String _segundoApellidoUsuario;
    String _direccionUsuario;
    String _telefonoUsuario;
    String _emailUsuario;
    String _usuarioAplicativo;
    String _passwordAplicativo;
    String _nombres;

    public ElementoCompra(String _idCompra, String _idUsuario, String _fechaCompra, String _totalCompra, String _saldoCompra, String _estadoCompra, String _idRol, String _fotoUsuario, String _primerNombreUsuario, String _segundoNombreUsuario, String _primerApellidoUsuario, String _segundoApellidoUsuario, String _direccionUsuario, String _telefonoUsuario, String _emailUsuario, String _usuarioAplicativo, String _passwordAplicativo, String _nombres) {
        this._idCompra = _idCompra;
        this._idUsuario = _idUsuario;
        this._fechaCompra = _fechaCompra;
        this._totalCompra = _totalCompra;
        this._saldoCompra = _saldoCompra;
        this._estadoCompra = _estadoCompra;
        this._idRol = _idRol;
        this._fotoUsuario = _fotoUsuario;
        this._primerNombreUsuario = _primerNombreUsuario;
        this._segundoNombreUsuario = _segundoNombreUsuario;
        this._primerApellidoUsuario = _primerApellidoUsuario;
        this._segundoApellidoUsuario = _segundoApellidoUsuario;
        this._direccionUsuario = _direccionUsuario;
        this._telefonoUsuario = _telefonoUsuario;
        this._emailUsuario = _emailUsuario;
        this._usuarioAplicativo = _usuarioAplicativo;
        this._passwordAplicativo = _passwordAplicativo;
        this._nombres = _nombres;
    }

    public ElementoCompra() {
    }

    public String get_idCompra() {
        return _idCompra;
    }

    public void set_idCompra(String _idCompra) {
        this._idCompra = _idCompra;
    }

    public String get_idUsuario() {
        return _idUsuario;
    }

    public void set_idUsuario(String _idUsuario) {
        this._idUsuario = _idUsuario;
    }

    public String get_fechaCompra() {
        return _fechaCompra;
    }

    public void set_fechaCompra(String _fechaCompra) {
        this._fechaCompra = _fechaCompra;
    }

    public String get_totalCompra() {
        return _totalCompra;
    }

    public void set_totalCompra(String _totalCompra) {
        this._totalCompra = _totalCompra;
    }

    public String get_saldoCompra() {
        return _saldoCompra;
    }

    public void set_saldoCompra(String _saldoCompra) {
        this._saldoCompra = _saldoCompra;
    }

    public String get_estadoCompra() {
        return _estadoCompra;
    }

    public void set_estadoCompra(String _estadoCompra) {
        this._estadoCompra = _estadoCompra;
    }

    public String get_idRol() {
        return _idRol;
    }

    public void set_idRol(String _idRol) {
        this._idRol = _idRol;
    }

    public String get_fotoUsuario() {
        return _fotoUsuario;
    }

    public void set_fotoUsuario(String _fotoUsuario) {
        this._fotoUsuario = _fotoUsuario;
    }

    public String get_primerNombreUsuario() {
        return _primerNombreUsuario;
    }

    public void set_primerNombreUsuario(String _primerNombreUsuario) {
        this._primerNombreUsuario = _primerNombreUsuario;
    }

    public String get_segundoNombreUsuario() {
        return _segundoNombreUsuario;
    }

    public void set_segundoNombreUsuario(String _segundoNombreUsuario) {
        this._segundoNombreUsuario = _segundoNombreUsuario;
    }

    public String get_primerApellidoUsuario() {
        return _primerApellidoUsuario;
    }

    public void set_primerApellidoUsuario(String _primerApellidoUsuario) {
        this._primerApellidoUsuario = _primerApellidoUsuario;
    }

    public String get_segundoApellidoUsuario() {
        return _segundoApellidoUsuario;
    }

    public void set_segundoApellidoUsuario(String _segundoApellidoUsuario) {
        this._segundoApellidoUsuario = _segundoApellidoUsuario;
    }

    public String get_direccionUsuario() {
        return _direccionUsuario;
    }

    public void set_direccionUsuario(String _direccionUsuario) {
        this._direccionUsuario = _direccionUsuario;
    }

    public String get_telefonoUsuario() {
        return _telefonoUsuario;
    }

    public void set_telefonoUsuario(String _telefonoUsuario) {
        this._telefonoUsuario = _telefonoUsuario;
    }

    public String get_emailUsuario() {
        return _emailUsuario;
    }

    public void set_emailUsuario(String _emailUsuario) {
        this._emailUsuario = _emailUsuario;
    }

    public String get_usuarioAplicativo() {
        return _usuarioAplicativo;
    }

    public void set_usuarioAplicativo(String _usuarioAplicativo) {
        this._usuarioAplicativo = _usuarioAplicativo;
    }

    public String get_passwordAplicativo() {
        return _passwordAplicativo;
    }

    public void set_passwordAplicativo(String _passwordAplicativo) {
        this._passwordAplicativo = _passwordAplicativo;
    }

    public String get_nombres() {
        return _nombres;
    }

    public void set_nombres(String _nombres) {
        this._nombres = _nombres;
    }


    @Override
    public String toString() {
        return "ElementoCompra{" +
                "_idCompra='" + _idCompra + '\'' +
                ", _idUsuario='" + _idUsuario + '\'' +
                ", _fechaCompra='" + _fechaCompra + '\'' +
                ", _totalCompra='" + _totalCompra + '\'' +
                ", _saldoCompra='" + _saldoCompra + '\'' +
                ", _estadoCompra='" + _estadoCompra + '\'' +
                ", _idRol='" + _idRol + '\'' +
                ", _fotoUsuario='" + _fotoUsuario + '\'' +
                ", _primerNombreUsuario='" + _primerNombreUsuario + '\'' +
                ", _segundoNombreUsuario='" + _segundoNombreUsuario + '\'' +
                ", _primerApellidoUsuario='" + _primerApellidoUsuario + '\'' +
                ", _segundoApellidoUsuario='" + _segundoApellidoUsuario + '\'' +
                ", _direccionUsuario='" + _direccionUsuario + '\'' +
                ", _telefonoUsuario='" + _telefonoUsuario + '\'' +
                ", _emailUsuario='" + _emailUsuario + '\'' +
                ", _usuarioAplicativo='" + _usuarioAplicativo + '\'' +
                ", _passwordAplicativo='" + _passwordAplicativo + '\'' +
                ", _nombres='" + _nombres + '\'' +
                '}';
    }
}
