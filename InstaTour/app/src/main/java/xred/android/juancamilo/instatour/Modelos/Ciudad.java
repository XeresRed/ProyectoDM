package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Ciudad {

    private static final String TAG = "error";
    private String NomCiu;
    private String Descrip;
    private String Imag;
    Connection conexion ;
    List<Ciudad> ciudades = null;

    public Ciudad(){
        NomCiu = "";
        Descrip = "";
        Imag = "";
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour",null,1);
    }

    public void setNomCiu(String NombreCiu){
        NomCiu = NombreCiu;
    }

    public void setDescrip(String Des) {
        Descrip = Des;
    }

    public void setImag(String Img) {
        Imag = Img;
    }

    public String getNomCiu(){
        return NomCiu;
    }

    public String getDescrip() {
        return Descrip;
    }

    public String getImag() {
        return Imag;
    }

    public List<Ciudad> traeCiudad(){

        try{
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT *  FROM ciudad ",null);

            if(cur.moveToFirst()){
                ciudades = new ArrayList<>();
                do{

                    Ciudad ciu = new Ciudad();

                    ciu.setNomCiu(cur.getString(0));
                    ciu.setDescrip(cur.getString(1));
                    ciu.setImag(cur.getString(2));

                    ciudades.add(ciu);


                    Log.v(TAG,"ACTIVIDAD TRAE Ciudad " + ciu.getNomCiu() + "\n Descripción " + ciu.getDescrip() + "\n Imagen " + ciu.getImag());

                }while (cur.moveToNext());

            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return ciudades;
    }
}
