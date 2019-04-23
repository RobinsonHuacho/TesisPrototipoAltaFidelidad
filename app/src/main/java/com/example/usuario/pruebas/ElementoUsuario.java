package com.example.usuario.pruebas;
/**

 /**
 * Created by Robinson Huacho on 14/08/17.
 */

public class ElementoUsuario {


    String _idUsuario;
    String _idRol;
    String _fotoUsuario;
    String _cedulaUsuario;
    String _primerNombreUsuario;
    String _primerApellidoUsuario;
    String _condicionApellidoUsuario;
    String _direccionUsuario;
    String _telefonoUsuario;
    String _emailUsuario;
    String _usuarioAplicativo;
    String _passwordAplicativo;


    public ElementoUsuario(String _idUsuario, String _idRol, String _fotoUsuario, String _cedulaUsuario,String _primerNombreUsuario, String _primerApellidoUsuario,String _condicionApellidoUsuario, String _direccionUsuario, String _telefonoUsuario, String _emailUsuario, String _usuarioAplicativo, String _passwordAplicativo) {
        this._idUsuario = _idUsuario;
        this._idRol = _idRol;
        this._fotoUsuario = _fotoUsuario;
        this._cedulaUsuario=_cedulaUsuario;
        this._primerNombreUsuario = _primerNombreUsuario;
         this._primerApellidoUsuario = _primerApellidoUsuario;
         this._condicionApellidoUsuario = _condicionApellidoUsuario;
        this._direccionUsuario = _direccionUsuario;
        this._telefonoUsuario = _telefonoUsuario;
        this._emailUsuario = _emailUsuario;
        this._usuarioAplicativo = _usuarioAplicativo;
        this._passwordAplicativo = _passwordAplicativo;
    }

    public ElementoUsuario() {
    }

    public String get_idUsuario() {
        return _idUsuario;
    }

    public void set_idUsuario(String _idUsuario) {
        this._idUsuario = _idUsuario;
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


    public String get_primerApellidoUsuario() {
        return _primerApellidoUsuario;
    }

    public void set_primerApellidoUsuario(String _primerApellidoUsuario) {
        this._primerApellidoUsuario = _primerApellidoUsuario;
    }



    public String get_condicionApellidoUsuario() {
        return _condicionApellidoUsuario;
    }

    public void set_condicionApellidoUsuario(String _condicionApellidoUsuario) {
        this._condicionApellidoUsuario = _condicionApellidoUsuario;
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

    public String get_cedulaUsuario() {
        return _cedulaUsuario;
    }

    public void set_cedulaUsuario(String _cedulaUsuario) {
        this._cedulaUsuario = _cedulaUsuario;
    }

    @Override
    public String toString() {
        return "ElementoCompra{" +
                "_idUsuario='" + _idUsuario + '\'' +
                ", _idRol='" + _idRol + '\'' +
                ", _fotoUsuario='" + _fotoUsuario + '\'' +
                ", _primerNombreUsuario='" + _primerNombreUsuario + '\'' +
                ", _primerApellidoUsuario='" + _primerApellidoUsuario + '\'' +
                ", _condicionApellidoUsuario='" + _condicionApellidoUsuario + '\'' +
                ", _direccionUsuario='" + _direccionUsuario + '\'' +
                ", _telefonoUsuario='" + _telefonoUsuario + '\'' +
                ", _emailUsuario='" + _emailUsuario + '\'' +
                ", _usuarioAplicativo='" + _usuarioAplicativo + '\'' +
                ", _passwordAplicativo='" + _passwordAplicativo + '\'' +
                '}';
    }
}
