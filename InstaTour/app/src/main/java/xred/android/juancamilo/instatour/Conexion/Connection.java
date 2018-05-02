package xred.android.juancamilo.instatour.Conexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import xred.android.juancamilo.instatour.Modelos.Administrador;
import xred.android.juancamilo.instatour.Modelos.Api;
import xred.android.juancamilo.instatour.Modelos.Ciudad;
import xred.android.juancamilo.instatour.Modelos.Usuario;
import xred.android.juancamilo.instatour.Modelos.Visita;

public class Connection extends SQLiteOpenHelper {
    private static final String TAG = "error";

    List<Usuario> usuarios = null;
    List<Administrador> Administrador = null;
    List<Ciudad> Ciudad = null;
    List<Visita> Visita = null;
    List<xred.android.juancamilo.instatour.Modelos.Api> Api = null;

    String query = "create table Admin (id TEXT PRIMARY KEY,CorreoU TEXT unique, Tipo TEXT, Contraseña TEXT);";
    String query1 = "create table usuario (CorreoU TEXT PRIMARY KEY, NombreU TEXT, Contraseña TEXT);";
    String query2 = "create table visita (id INTEGER PRIMARY KEY AUTOINCREMENT, CorreoU TEXT, NombreCiu TEXT);";
    String query3 = "create table ciudad (NombreCiu TEXT PRIMARY KEY, Descripcion TEXT, Imagen TEXT);";
    String query4 = "create table api (id INTEGER PRIMARY KEY AUTOINCREMENT, NombreCiu TEXT, NombreApi TEXT);";


    public  Connection(Context context, String nameBd, SQLiteDatabase.CursorFactory factory, int version){
        super(context,nameBd,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS dieta");
        db.execSQL("DROP TABLE IF EXISTS comida");
        db.execSQL("DROP TABLE IF EXISTS Favoritos");
    }

    public  void borrarBD(){

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE  FROM usuario");
        db.execSQL("DELETE  FROM Admin");
        db.execSQL("DELETE  FROM visita");
        db.execSQL("DELETE  FROM api");
        db.execSQL("DELETE  FROM ciudad");

    }
    public void act(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS Admin");
        db.execSQL("DROP TABLE IF EXISTS visita");
        db.execSQL("DROP TABLE IF EXISTS api");
        db.execSQL("DROP TABLE IF EXISTS ciudad");
        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }





}
