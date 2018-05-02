package xred.android.juancamilo.instatour.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Administrador {
    private static final String TAG = "error";
    private String Id;
    private String Correo;
    private String tipo;
    private String Contraseña;
    Connection conexion ;


    public Administrador(){
        Id = "";
        Correo = "";
        tipo = "";
        Contraseña = "";
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour",null,1);
    }

    public ArrayList<Administrador> traeUsuario(){
        ArrayList<Administrador> admin = null;
        try{
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT id,CorreoU , Tipo , Contraseña FROM Admin ",null);

            if(cur.moveToFirst()){
                admin = new ArrayList<>();
                do{

                    Administrador persona = new Administrador();

                    persona.setId(cur.getString(0));
                    persona.setCorreo(cur.getString(1));
                    persona.setTipo(cur.getString(2));
                    persona.setContraseña(cur.getString(3));

                    admin.add(persona);


                    Log.v(TAG,"ACTIVIDAD TRAE Nombre " + persona.getTipo() + "\n Contraseña " + persona.getContraseña() + "\n Correo " + persona.getCorreo());

                }while (cur.moveToNext());

            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        //conexion.act();
        return admin;
    }


    public boolean login(String id, String pass){
        boolean log = false;

        try{
            SQLiteDatabase db = conexion.getReadableDatabase();

            Cursor cur = db.rawQuery("SELECT id,CorreoU , Tipo , Contraseña FROM Admin",null);

            if(cur.moveToFirst()){
                do{
                    Log.e(TAG,"lOS DATOS SON:" + cur.getString(1) + "-" + id+"-");
                    if(cur.getString(1).equals(id) && cur.getString(3).equals(pass))
                    {
                        Id =cur.getString(0);
                        Correo = cur.getString(1);
                        tipo = cur.getString(2);
                        Contraseña = cur.getString(3);
                        log = true;
                    }
                }while (cur.moveToNext());
            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return log;
    }

    public void registro (String id, String email, String password, String tipo){
        SQLiteDatabase db = conexion.getWritableDatabase();
        Log.e(TAG,"lOS DATOS SON: " + id + " - " + email+ " - " + password + " - "+ tipo);
        final String query = "INSERT INTO Admin (id,CorreoU , Tipo , Contraseña ) VALUES ('" + id + "', '" + email+ "', '"+ tipo +"', '"+ password +"' )";
        db.execSQL(query);
        db.close();

    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }
}
