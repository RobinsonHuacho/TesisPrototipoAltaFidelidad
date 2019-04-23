package com.example.usuario.pruebas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Robinson Huacho on 14/08/17.
 */

public class DatabaseHandlerUsuarios extends SQLiteOpenHelper{

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "usuariosManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 3;

    //Nombre de la tabla de casos
    private static final String TABLE_USUARIOS = "usuarios";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID_USUARIO = "ID_USUARIO";
    private static final String KEY_ID_ROL= "ID_ROL";
    private static final String KEY_FOTO_USUARIO = "FOTO_USUARIO";
    private static final String KEY_CEDULA_USUARIO = "CEDULA_USUARIO";
    private static final String KEY_PRIMER_NOMBRE_USUARIO = "PRIMER_NOMBRE_USUARIO";
    private static final String KEY_PRIMER_APELLIDO_USUARIO = "PRIMER_APELLIDO_USUARIO";
    private static final String KEY_CONDICION_USUARIO = "CONDICION_USUARIO";
    private static final String KEY_DIRECCION_USUARIO = "DIRECCION_USUARIO";
    private static final String KEY_TELEFONO_USUARIO = "TELEFONO_USUARIO";
    private static final String KEY_EMAIL_USUARIO = "EMAIL_USUARIO";
    private static final String KEY_USUARIO_APLICATIVO = "USUARIO_APLICATIVO";
    private static final String KEY_PASSWORD_APLICATIVO = "PASSWORD_APLICATIVO";

    //Constructor de la base de datos
    public DatabaseHandlerUsuarios(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USUARIOS + "("
                + KEY_ID_USUARIO + " INTEGER PRIMARY KEY,"
                + KEY_ID_ROL+ " TEXT ,"
                + KEY_FOTO_USUARIO + " TEXT,"
                + KEY_CEDULA_USUARIO + " TEXT,"
                + KEY_PRIMER_NOMBRE_USUARIO + " TEXT,"
                + KEY_PRIMER_APELLIDO_USUARIO + " TEXT,"
                + KEY_CONDICION_USUARIO + " TEXT,"
                + KEY_DIRECCION_USUARIO + " TEXT,"
                + KEY_TELEFONO_USUARIO + " TEXT,"
                + KEY_EMAIL_USUARIO + " TEXT,"
                + KEY_USUARIO_APLICATIVO + " TEXT,"
                + KEY_PASSWORD_APLICATIVO + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }


    public void addUsuarios(ElementoUsuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_USUARIO, usuario.get_idUsuario());
        values.put(KEY_ID_ROL, usuario.get_idRol());
        values.put(KEY_FOTO_USUARIO, usuario.get_fotoUsuario());
        values.put(KEY_CEDULA_USUARIO, usuario.get_cedulaUsuario());
        values.put(KEY_PRIMER_NOMBRE_USUARIO, usuario.get_primerNombreUsuario());
        values.put(KEY_PRIMER_APELLIDO_USUARIO, usuario.get_primerApellidoUsuario());
        values.put(KEY_CONDICION_USUARIO, usuario.get_condicionApellidoUsuario());
        values.put(KEY_DIRECCION_USUARIO, usuario.get_direccionUsuario());
        values.put(KEY_TELEFONO_USUARIO, usuario.get_telefonoUsuario());
        values.put(KEY_EMAIL_USUARIO, usuario.get_emailUsuario());
        values.put(KEY_USUARIO_APLICATIVO, usuario.get_usuarioAplicativo());
        values.put(KEY_PASSWORD_APLICATIVO, usuario.get_passwordAplicativo());



        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    public ArrayList getAllUsuarios(int elemento) {
        ArrayList<String> productosList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_USUARIOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                productosList.add(cursor.getString(elemento));
            } while (cursor.moveToNext());
        }
        return productosList;
    }



    public long conteoRegistros(String cedula){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + TABLE_USUARIOS +" WHERE "+ KEY_CEDULA_USUARIO +" =? ",new
                String[]{String.valueOf(cedula)}, null);
        long numRows = cursor.getCount();
        return numRows;
    }


    public void deleteUsuarios(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USUARIOS +" WHERE 1");
    }





}
