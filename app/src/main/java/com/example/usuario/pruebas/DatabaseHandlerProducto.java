package com.example.usuario.pruebas;

import android.content.ContentValues;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Robinson Huacho on 24/07/17.
 */

public class DatabaseHandlerProducto extends SQLiteOpenHelper {

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "ProductosManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 3;

    //Nombre de la tabla de casos
    private static final String TABLE_PRODUCTOS = "Producto";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID = "ID_PRODUCTO";
    private static final String KEY_ID_CATEGORIA = "ID_CATEGORIA_PRODUCTO";
    private static final String KEY_NOMBRE = "NOMBRE_PRODUCTO";
    private static final String KEY_DESCRIPCION = "DETALLE_PRODUCTO";
    private static final String KEY_PRECIO_UNITARIO = "PRECIO_UNITARIO";
    private static final String KEY_IMAGEN = "IMAGEN_PRODUCTO";


    //Constructor de la base de datos
    public DatabaseHandlerProducto(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_CATEGORIA + " TEXT ,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DESCRIPCION + " TEXT,"
                + KEY_PRECIO_UNITARIO + " TEXT,"
                + KEY_IMAGEN + " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }


    public void addProductos(ElementoProducto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, producto.getIdProducto());
        values.put(KEY_ID_CATEGORIA, producto.getIdCategoriaProducto());
        values.put(KEY_NOMBRE, producto.getNombres());
        values.put(KEY_DESCRIPCION, producto.getDescripcion());
        values.put(KEY_PRECIO_UNITARIO, producto.getPrecio());
        values.put(KEY_IMAGEN, producto.getImagen());

        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

    public ElementoProducto getProducto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_PRODUCTOS, new
                    String[]{KEY_ID, KEY_ID_CATEGORIA, KEY_NOMBRE, KEY_DESCRIPCION, KEY_PRECIO_UNITARIO, KEY_IMAGEN}, KEY_ID + "=?", new
                    String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoProducto producto = new ElementoProducto(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            return producto;
        } catch (Exception error) {

            ElementoProducto productoR = new ElementoProducto();
            return productoR;
        }

    }



    /*public ElementoProducto getProductoNombre(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_PRODUCTOS, new
                    String[]{KEY_ID, KEY_ID_CATEGORIA, KEY_NOMBRE, KEY_DESCRIPCION, KEY_PRECIO_UNITARIO, KEY_IMAGEN}, KEY_NOMBRE + "=?", new
                    String[]{String.valueOf(nombre)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoProducto producto = new ElementoProducto(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            return producto;
        } catch (Exception error) {

            ElementoProducto productoR = new ElementoProducto();
            return productoR;
        }
    }*/

    public ElementoProducto getIdProductoPorNombre(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_PRODUCTOS, new
                    String[]{KEY_ID, KEY_ID_CATEGORIA, KEY_NOMBRE, KEY_DESCRIPCION, KEY_PRECIO_UNITARIO, KEY_IMAGEN}, KEY_NOMBRE + "=?", new
                    String[]{String.valueOf(nombre)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoProducto producto = new ElementoProducto(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            return producto;
        } catch (Exception error) {

            ElementoProducto productoR = new ElementoProducto();
            return productoR;
        }
    }



    public ArrayList getAllProductos(int elemento) {
        ArrayList<String> productosList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_PRODUCTOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                productosList.add(cursor.getString(elemento));
            } while (cursor.moveToNext());
        }
        return productosList;
    }

    public ArrayList getProductos(String id, int elemento) {
        ArrayList<String> productosList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_PRODUCTOS+" WHERE "+KEY_ID+" = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                productosList.add(cursor.getString(elemento));
            } while (cursor.moveToNext());
        }
        return productosList;
    }

    public int updateProducto(ElementoProducto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, producto.getIdProducto());
        values.put(KEY_ID_CATEGORIA, producto.getIdCategoriaProducto());
        values.put(KEY_NOMBRE, producto.getNombres());
        values.put(KEY_DESCRIPCION, producto.getDescripcion());
        values.put(KEY_PRECIO_UNITARIO, producto.getPrecio());
        values.put(KEY_IMAGEN, producto.getImagen());

        return db.update(TABLE_PRODUCTOS, values, KEY_ID + "=?",
                new String[]{String.valueOf(producto.getIdProducto())});
    }

    public void deleteProducto(ElementoProducto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, KEY_ID + " =?",
                new String[]{String.valueOf(producto.getIdProducto())});
        db.close();
    }

    public void deleteProductos(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTOS +" WHERE 1");
    }

}
