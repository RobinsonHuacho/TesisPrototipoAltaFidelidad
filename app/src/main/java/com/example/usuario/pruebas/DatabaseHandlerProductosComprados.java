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

public class DatabaseHandlerProductosComprados extends SQLiteOpenHelper{

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "productosCompradosManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 3;

    //Nombre de la tabla de casos
    private static final String TABLE_PRODUCTOS = "productosComprados";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID_DETALLE = "ID_DETALLE_VENTA";
    private static final String KEY_ID_PRODUCTO = "ID_PRODUCTO";
    private static final String KEY_ID_USUARIO = "ID_USUARIO";
    private static final String KEY_PRECIO = "PRECIO_VENTA_DETALLE";
    private static final String KEY_CANTIDAD = "CANTIDAD_DETALLE";
    private static final String KEY_TOTAL = "TOTAL_DETALLE_VENTA";
    private static final String KEY_ESTADO = "ESTADO";
    private static final String KEY_IMAGEN__PRODUCTO = "IMAGEN_PRODUCTO";
    private static final String KEY_NOMBRE_PRODUCTO = "NOMBRE_PRODUCTO";



    //Constructor de la base de datos
    public DatabaseHandlerProductosComprados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTOS + "("
                + KEY_ID_DETALLE + " INTEGER PRIMARY KEY,"
                + KEY_ID_PRODUCTO + " TEXT,"
                + KEY_ID_USUARIO + " TEXT,"
                + KEY_PRECIO + " TEXT,"
                + KEY_CANTIDAD + " TEXT,"
                + KEY_TOTAL + " TEXT,"
                + KEY_ESTADO+ " TEXT ,"
                + KEY_IMAGEN__PRODUCTO + " TEXT,"
                + KEY_NOMBRE_PRODUCTO + " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }


    public void addProductosDetalle(ProductosComprados producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DETALLE, producto.get_idDetalle());
        values.put(KEY_ID_PRODUCTO, producto.get_idProducto());
        values.put(KEY_ID_USUARIO, producto.get_idUsuario());
        values.put(KEY_PRECIO, producto.get_precioVenta());
        values.put(KEY_CANTIDAD, producto.get_cantidadDetalle());
        values.put(KEY_TOTAL, producto.get_totalDetalle());
        values.put(KEY_ESTADO, producto.get_estado());
        values.put(KEY_IMAGEN__PRODUCTO, producto.get_imagenProducto());
        values.put(KEY_NOMBRE_PRODUCTO, producto.get_nombreProdcuto());

        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

     public ArrayList getAllProductosComprados(int elemento) {
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

    public void deleteProductos(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTOS +" WHERE 1");
    }


}
