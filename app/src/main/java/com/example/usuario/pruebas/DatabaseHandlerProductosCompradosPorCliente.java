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

public class DatabaseHandlerProductosCompradosPorCliente extends SQLiteOpenHelper{

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "productosCompradosPorClienteManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 2;

    //Nombre de la tabla de casos
    private static final String TABLE_USUARIOS = "productosCompradosPorCliente";

    //Nombres de las columnas de la tabla
    private static final String KEY_CONTEO_PRODUCTOS = "ID_CONTEO_PRODUCTOS";


    //Constructor de la base de datos
    public DatabaseHandlerProductosCompradosPorCliente(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USUARIOS + "("
                + KEY_CONTEO_PRODUCTOS + " INTEGER PRIMARY KEY)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }


    public void addUsuarios(ElementoProductosCompradosPorUsuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTEO_PRODUCTOS, usuario.getConteoProducto());

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

    public String conteoRegistros(){
        String numRows = "";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + KEY_CONTEO_PRODUCTOS + " FROM " + TABLE_USUARIOS,null);
        if (cursor.moveToFirst()) {
            do {
                numRows=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return numRows;
    }




    public void deleteUsuarios(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USUARIOS +" WHERE 1");
    }





}
