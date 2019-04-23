package com.example.usuario.pruebas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robinson Huacho on 14/08/17.
 */

public class DatabaseHandlerDetalleCompra extends SQLiteOpenHelper{

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "detallecompraManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    //Nombre de la tabla de casos
    private static final String TABLE_COMPRAS = "detallecompra";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID_DETALLE_COMPRA = "ID_DETALLE_COMPRA";
    private static final String KEY_ID_COMPRA = "ID_COMPRA";
    private static final String KEY_ID_PRODUCTO = "ID_PRODUCTO";
    private static final String KEY_ID_USUARIO = "ID_USUARIO";
    private static final String KEY_PRECIO_DETALLE = "PRECIO_DETALLE";
    private static final String KEY_CANTIDAD = "CANTIDAD_DETALLE";
    private static final String KEY_TOTAL = "TOTAL_DETALLE_VENTA";
    private static final String KEY_ESTADO = "ESTADO";

    //Constructor de la base de datos
    public DatabaseHandlerDetalleCompra(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COMPRAS + "("
                + KEY_ID_DETALLE_COMPRA + " INTEGER PRIMARY KEY,"
                + KEY_ID_COMPRA + " INTEGER,"
                + KEY_ID_PRODUCTO + " TEXT,"
                + KEY_ID_USUARIO+ " TEXT ,"
                + KEY_PRECIO_DETALLE + " TEXT,"
                + KEY_CANTIDAD + " TEXT,"
                + KEY_TOTAL + " TEXT,"
                + KEY_ESTADO+ " TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPRAS);
        onCreate(db);
    }


    public void addCompras(ElementoDetalleCompra producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DETALLE_COMPRA, producto.get_idDetalle());
        values.put(KEY_ID_COMPRA, producto.get_idCompra());
        values.put(KEY_ID_PRODUCTO, producto.get_idProducto());
        values.put(KEY_ID_USUARIO, producto.get_idUsuario());
        values.put(KEY_PRECIO_DETALLE, producto.get_precioVenta());
        values.put(KEY_CANTIDAD, producto.get_cantidadDetalle());
        values.put(KEY_TOTAL, producto.get_totalDetalle());
        values.put(KEY_ESTADO, producto.get_estado());
        db.insert(TABLE_COMPRAS, null, values);
        db.close();
    }

    /*public ElementoCompra getProducto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_COMPRAS, new
                    String[]{KEY_ID_COMPRA, KEY_ID_USUARIO, KEY_FECHA_COMPRA, KEY_TOTAL_COMPRA, KEY_SALDO_COMPRA, KEY_ESTADO_COMPRA}, KEY_ID_COMPRA + "=?", new
                    String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCompra producto = new ElementoCompra(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            return producto;
        } catch (Exception error) {

            ElementoCompra productoR = new ElementoCompra();
            return productoR;
        }

    }*/

    public long conteoRegistros(String cedula, String producto){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + TABLE_COMPRAS +" WHERE "+ KEY_ID_PRODUCTO +" =? ",new
                String[]{String.valueOf(cedula),String.valueOf(producto)}, null);
        long numRows = cursor.getCount();
        return numRows;
    }



    public void deleteProductos(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COMPRAS +" WHERE 1");
    }


}
