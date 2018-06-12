package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Usuario {

    private static final String TAG = "error";
    private String Correo;
    private String Nombre;
    private String Contraseña;
    private String tipo;
    Connection conexion ;

    
    
    public Usuario(){
        Correo = "";
        Nombre = "";
        Contraseña = "";
        tipo = "";
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
            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña, tipo  FROM usuario ",null);

            if(cur.moveToFirst()){
                usuarios = new ArrayList<>();
                do{

                    Usuario persona = new Usuario();

                    persona.setCorreo(cur.getString(0));
                    persona.setNombre(cur.getString(1));
                    persona.setContraseña(cur.getString(2));
                    persona.setTipo(cur.getString(3));

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

    public String registro (String email, String password, String Nombre, String tipo){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String mensaje = "";
        if(busqueda(email)){
            String query = "INSERT INTO usuario (CorreoU, NombreU , Contraseña, tipo ) VALUES ('" + email + "', '" + Nombre+ "', '"+ password +"', '"+tipo+"')";
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

            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña, tipo  FROM usuario",null);

            if(cur.moveToFirst()){
                do{
                    Log.e(TAG,"lOS DATOS SON: -" + cur.getString(0)+ "-\n-" + cur.getString(1) + "-\n-" + cur.getString(2)+ "-\n-"+ cur.getString(3) +"-\n-"+ id+"-\n-"+pass+"-");
                    if(cur.getString(0).equals(id) && cur.getString(1).equals(pass))
                    {
                        Correo =cur.getString(0);
                        Contraseña= cur.getString(1);
                        Nombre = cur.getString(2);
                        tipo = cur.getString(3);
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

    public void busquedaUsuario(String email) {
        try {
            SQLiteDatabase db = conexion.getWritableDatabase();
            Cursor cur = db.rawQuery("SELECT CorreoU, NombreU , Contraseña  FROM usuario WHERE CorreoU ='" + email + "'", null);
            if (cur.moveToFirst()) {
                Correo =cur.getString(0);
                Contraseña= cur.getString(1);
                Nombre = cur.getString(2);
                tipo = cur.getString(3);
            }
        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void Actualiza( String nombre, String contraseña )
    {
        boolean log = true;
        try{
            SQLiteDatabase db = conexion.getWritableDatabase();
            db.execSQL("UPDATE usuario SET   NombreU  = '" + nombre + "', Contraseña = '" + contraseña + "' WHERE  CorreoU = '" + Correo + "'");
            db.close();

        }catch (Exception e){
            Log.e(TAG,"no actualizo" + e.getMessage());
        }
    }
}
