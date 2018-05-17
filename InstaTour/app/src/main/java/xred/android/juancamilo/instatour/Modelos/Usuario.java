package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Usuario {

    private static final String TAG = "error";
    private String Correo;
    private String Nombre;
    private String Contraseña;
    Connection conexion ;

    
    
    public Usuario(){
        Correo = "";
        Nombre = "";
        Contraseña = "";
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour1",null,1);
        //conexion.act();
    }

    public void setNombre(String name){
        Nombre = name;
    }


    public void setContraseña(String pass) {
        Contraseña = pass;
    }

    public void setCorreo(String email) {
        Correo = email;
    }

    public String getNombre(){
        return Nombre;
    }



    public String getContraseña() {
        return Contraseña;
    }

    public String getCorreo() {
        return Correo;
    }

    public ArrayList<Usuario> traeUsuario(){
        ArrayList<Usuario> usuarios = null;
        try{
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña  FROM usuario ",null);

            if(cur.moveToFirst()){
                usuarios = new ArrayList<>();
                do{

                    Usuario persona = new Usuario();

                    persona.setCorreo(cur.getString(0));
                    persona.setNombre(cur.getString(1));
                    persona.setContraseña(cur.getString(2));

                    usuarios.add(persona);


                    Log.v(TAG,"ACTIVIDAD TRAE Nombre " + persona.getNombre() + "\n Contraseña " + persona.getContraseña() + "\n Correo " + persona.getCorreo());

                }while (cur.moveToNext());

            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return usuarios;
    }

    public String registro (String email, String password, String Nombre){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String mensaje = "";
        if(busqueda(email)){
            String query = "INSERT INTO usuario (CorreoU, NombreU , Contraseña ) VALUES ('" + email + "', '" + Nombre+ "', '"+ password +"')";
            db.execSQL(query);
            mensaje = "r";
            db.close();
        }else{
            mensaje = "El usuario con ese correo ya existe";
        }

        return  mensaje;
    }

    public boolean login(String id, String pass){
        boolean log = false;

        try{
            SQLiteDatabase db = conexion.getReadableDatabase();

            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña  FROM usuario",null);

            if(cur.moveToFirst()){
                do{
                    Log.e(TAG,"lOS DATOS SON: -" + cur.getString(0)+ "-\n-" + cur.getString(1) + "-\n-" + cur.getString(2)+ "-\n-" + id+"-\n-"+pass+"-");
                    if(cur.getString(0).equals(id) && cur.getString(1).equals(pass))
                    {
                        Correo =cur.getString(0);
                        Contraseña= cur.getString(1);
                        Nombre = cur.getString(2);
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


    private boolean busqueda(String email) {
        boolean log = true;
        try {
            SQLiteDatabase db = conexion.getWritableDatabase();
            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña  FROM usuario WHERE CorreoU ='" + email + "'", null);
            if (cur.moveToFirst()) {
                log = false;
            }
        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return log;
    }

}
