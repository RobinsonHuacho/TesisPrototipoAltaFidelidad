package com.example.usuario.pruebas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Robinson Huacho on 14/08/17.
 */

public class DatabaseHandlerCompras extends SQLiteOpenHelper{

    //Nombre de la base de datos
    private static final String DATABASE_NAME = "comprasManager";

    //Versión de la base de datos
    private static final int DATABASE_VERSION = 2;

    //Nombre de la tabla de casos
    private static final String TABLE_COMPRAS = "compras";

    //Nombres de las columnas de la tabla
    private static final String KEY_ID_COMPRA = "ID_COMPRA";
    private static final String KEY_ID_USUARIO = "ID_USUARIO";
    private static final String KEY_FECHA_COMPRA = "FECHA_COMPRA";
    private static final String KEY_TOTAL_COMPRA = "TOTAL_COMPRA";
    private static final String KEY_SALDO_COMPRA = "SALDO_COMPRA";
    private static final String KEY_ESTADO_COMPRA = "ESTADO_COMPRA";
    private static final String KEY_ID_ROL = "ID_ROL";
    private static final String KEY_FOTO_USUARIO = "FOTO_USUARIO";
    private static final String KEY_PRIMER_NOMBRE_USUARIO = "PRIMER_NOMBRE_USUARIO";
    private static final String KEY_SEGUNDO_NOMBRE_USUARIO = "SEGUNDO_NOMBRE_USUARIO";
    private static final String KEY_PRIMER_APELLIDO_USUARIO = "PRIMER_APELLIDO_USUARIO";
    private static final String KEY_SEGUNDO_APELLIDO_USUARIO = "SEGUNDO_APELLIDO_USUARIO";
    private static final String KEY_DIRECCION_USUARIO = "DIRECCION_USUARIO";
    private static final String KEY_TELEFONO_USUARIO = "TELEFONO_USUARIO";
    private static final String KEY_EMAIL_USUARIO = "EMAIL_USUARIO";
    private static final String KEY_USUARIO_APLICATIVO = "USUARIO_APLICATIVO";
    private static final String KEY_PASSWORD_APLICATIVO = "PASSWORD_APLICATIVO";
    private static final String KEY_NOMBRES = "NOMBRES";



    //Constructor de la base de datos
    public DatabaseHandlerCompras(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COMPRAS + "("
                + KEY_ID_COMPRA + " INTEGER PRIMARY KEY,"
                + KEY_ID_USUARIO+ " TEXT ,"
                + KEY_FECHA_COMPRA + " TEXT,"
                + KEY_TOTAL_COMPRA + " TEXT,"
                + KEY_SALDO_COMPRA + " TEXT,"
                + KEY_ESTADO_COMPRA + " TEXT,"
                + KEY_ID_ROL + " TEXT,"
                + KEY_FOTO_USUARIO + " TEXT,"
                + KEY_PRIMER_NOMBRE_USUARIO + " TEXT,"
                + KEY_SEGUNDO_NOMBRE_USUARIO + " TEXT,"
                + KEY_PRIMER_APELLIDO_USUARIO + " TEXT,"
                + KEY_SEGUNDO_APELLIDO_USUARIO + " TEXT,"
                + KEY_DIRECCION_USUARIO + " TEXT,"
                + KEY_TELEFONO_USUARIO + " TEXT,"
                + KEY_EMAIL_USUARIO + " TEXT,"
                + KEY_USUARIO_APLICATIVO + " TEXT,"
                + KEY_PASSWORD_APLICATIVO + " TEXT,"
                + KEY_NOMBRES + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPRAS);
        onCreate(db);
    }


    public void addCompras(ElementoCompra producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_COMPRA, producto.get_idCompra());
        values.put(KEY_ID_USUARIO, producto.get_idUsuario());
        values.put(KEY_FECHA_COMPRA, producto.get_fechaCompra());
        values.put(KEY_TOTAL_COMPRA, producto.get_totalCompra());
        values.put(KEY_SALDO_COMPRA, producto.get_saldoCompra());
        values.put(KEY_ESTADO_COMPRA, producto.get_estadoCompra());
        values.put(KEY_ID_ROL, producto.get_idRol());
        values.put(KEY_FOTO_USUARIO, producto.get_fotoUsuario());
        values.put(KEY_PRIMER_NOMBRE_USUARIO, producto.get_primerNombreUsuario());
        values.put(KEY_SEGUNDO_NOMBRE_USUARIO, producto.get_segundoNombreUsuario());
        values.put(KEY_PRIMER_APELLIDO_USUARIO, producto.get_primerApellidoUsuario());
        values.put(KEY_SEGUNDO_APELLIDO_USUARIO, producto.get_segundoApellidoUsuario());
        values.put(KEY_DIRECCION_USUARIO, producto.get_direccionUsuario());
        values.put(KEY_TELEFONO_USUARIO, producto.get_telefonoUsuario());
        values.put(KEY_EMAIL_USUARIO, producto.get_emailUsuario());
        values.put(KEY_USUARIO_APLICATIVO, producto.get_usuarioAplicativo());
        values.put(KEY_PASSWORD_APLICATIVO, producto.get_passwordAplicativo());
        values.put(KEY_NOMBRES, producto.get_nombres());


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
    public ElementoCompra getCompraPorPrimerNombreBeneficiario(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_COMPRAS, new
                    String[]{KEY_ID_COMPRA, KEY_ID_USUARIO, KEY_FECHA_COMPRA, KEY_TOTAL_COMPRA, KEY_SALDO_COMPRA, KEY_ESTADO_COMPRA,
                    KEY_ID_ROL,KEY_FOTO_USUARIO,KEY_PRIMER_NOMBRE_USUARIO,KEY_SEGUNDO_NOMBRE_USUARIO, KEY_PRIMER_APELLIDO_USUARIO,
                    KEY_SEGUNDO_APELLIDO_USUARIO,KEY_DIRECCION_USUARIO, KEY_TELEFONO_USUARIO, KEY_EMAIL_USUARIO,
                    KEY_USUARIO_APLICATIVO, KEY_PASSWORD_APLICATIVO, KEY_NOMBRES}, KEY_PRIMER_NOMBRE_USUARIO + "=?", new
                    String[]{String.valueOf(nombre)}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCompra producto = new ElementoCompra(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17));

            return producto;
        } catch (Exception error) {

            ElementoCompra productoR = new ElementoCompra();
            return productoR;
        }
    }

    public ElementoCompra getCompraPorDosIdentificativos(String nombre1,String nombre2) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_COMPRAS+" WHERE "+KEY_NOMBRES+" LIKE '%"+nombre1+"%"+nombre2+"%'",null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCompra producto = new ElementoCompra(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17));

            return producto;
        } catch (Exception error) {

            ElementoCompra productoR = new ElementoCompra();
            return productoR;
        }
    }

    public ElementoCompra getCompraPorTresIdentificativos(String nombre1,String nombre2,String nombre3) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_COMPRAS+" WHERE "+KEY_NOMBRES+" LIKE '%"+nombre1+"%"+nombre2+"%"+nombre3+"%'",null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCompra producto = new ElementoCompra(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17));

            return producto;
        } catch (Exception error) {

            ElementoCompra productoR = new ElementoCompra();
            return productoR;
        }
    }

    public ElementoCompra getCompraPorCuatroIdentificativos(String nombre1,String nombre2,String nombre3,String nombre4) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_COMPRAS+" WHERE "+KEY_NOMBRES+" LIKE '%"+nombre1+"%"+nombre2+"%"+nombre3+"%"+nombre4+"%'",null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            ElementoCompra producto = new ElementoCompra(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                    cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17));

            return producto;
        } catch (Exception error) {

            ElementoCompra productoR = new ElementoCompra();
            return productoR;
        }
    }

    public ArrayList getAllProductos(int elemento) {
        ArrayList<String> productosList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_COMPRAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                productosList.add(cursor.getString(elemento));
            } while (cursor.moveToNext());
        }
        return productosList;
    }



    /*public ArrayList getProductos(String idCategoria) {
        ArrayList<String> productosList = new ArrayList<>();
        String sql_select = "SELECT * FROM " + TABLE_PRODUCTOS+" WHERE "+KEY_ID_CATEGORIA+" = "+idCategoria;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);
        if (cursor.moveToFirst()) {
            do {
                productosList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return productosList;
    }*/

   /* public int updateProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, producto.get_id());
        values.put(KEY_ID_CATEGORIA, producto.get_idCategoria());
        values.put(KEY_NOMBRE, producto.get_nombre());
        values.put(KEY_DESCRIPCION, producto.get_descripcion());
        values.put(KEY_PRECIO_UNITARIO, producto.get_precioUnitario());
        values.put(KEY_CANTIDAD, producto.get_cantidad());
        values.put(KEY_IMAGEN, producto.get_imagen());

        return db.update(TABLE_PRODUCTOS, values, KEY_ID + "=?",
                new String[]{String.valueOf(producto.get_id())});
    }


    }*/

    public long conteoRegistros(){
        SQLiteDatabase db=this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_COMPRAS);
        return numRows;
    }

    public void deleteProducto(String idProducto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPRAS, KEY_ID_COMPRA + " =?",
                new String[]{String.valueOf(idProducto)});
        db.close();
    }

    public void deleteProductos(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COMPRAS +" WHERE 1");
    }


}
