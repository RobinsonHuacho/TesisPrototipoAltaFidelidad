package com.example.usuario.pruebas;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Robinson Huacho on 23/07/17.
 */

public class DatabaseHandlerCategorias extends SQLiteOpenHelper {

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 3;

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "CategoriaProductosManager";

    //Nombre de la tabla de casos
    private static final String TABLE_CATEGORIA_PRODUCTOS = "CategoriaProducto";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID = "ID_CATEGORIA_PRODUCTO";
    private static final String KEY_NOMBRE = "NOMBRE_CATEGORIA_PRODUCTO";
    private static final String KEY_IMAGEN = "IMAGEN_CATEGORIA";


    //Constructor de la base de datos
    public DatabaseHandlerCategorias(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CATEGORIA_PRODUCTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_IMAGEN + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA_PRODUCTOS);
        onCreate(db);
    }


    public void addCategoria(ElementoCategoriaProducto categoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, categoria.getIdCategoriaProducto());
        values.put(KEY_NOMBRE, categoria.getNombreCategoriaProducto());
        values.put(KEY_IMAGEN, categoria.getImagenCategoriaProducto());

        db.insert(TABLE_CATEGORIA_PRODUCTOS, null, values);
        db.close();
    }

    public ElementoCategoriaProducto getCategoria(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_CATEGORIA_PRODUCTOS, new
                    String[]{KEY_ID, KEY_NOMBRE, KEY_IMAGEN}, KEY_ID + "=?", new
                    String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCategoriaProducto categoria = new ElementoCategoriaProducto(cursor.getString(0), cursor.getString(1), cursor.getString(2));

            return categoria;
        } catch (Exception error) {

            ElementoCategoriaProducto categoriaR = new ElementoCategoriaProducto();
            return categoriaR;
        }

    }

    public ElementoCategoriaProducto getCategoriaNombre(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_CATEGORIA_PRODUCTOS, new
                    String[]{KEY_ID, KEY_NOMBRE, KEY_IMAGEN}, KEY_NOMBRE + "=?", new
                    String[]{String.valueOf(nombre)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCategoriaProducto categoria = new ElementoCategoriaProducto(cursor.getString(0), cursor.getString(1), cursor.getString(2));

            return categoria;
        } catch (Exception error) {

            ElementoCategoriaProducto categoriaR = new ElementoCategoriaProducto();
            return categoriaR;
        }

    }



    public ArrayList getAllCategorias(int elemento) {
        ArrayList<String> categoriasList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_CATEGORIA_PRODUCTOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                categoriasList.add(cursor.getString(elemento));
            } while (cursor.moveToNext());
        }
        return categoriasList;
    }



    public void deleteCategorias(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORIA_PRODUCTOS +" WHERE 1");
    }
}
