package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Visita {
    private static final String TAG = "error";
    private String Id;
    private String Correo;
    private String NomCiu;
    Connection conexion ;
    List<Visita> visitas = null;

    public Visita(){
        Id = "";
        Correo = "";
        NomCiu = "";
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour",null,1);
    }

    public void setId(String iden){
        Id = iden;
    }

    public void setCorreo(String email) {Correo = email;}

    public void setNomCiu(String NombreCiu) { NomCiu = NombreCiu; }

    public String getId(){ return Id; }

    public String getCorreo() { return Correo; }

    public String getNomCiu() { return NomCiu; }


    public List<Visita> traeVisita(){

        try{
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT *  FROM visita ",null);

            if(cur.moveToFirst()){
                visitas = new ArrayList<>();
                do{

                    Visita visit = new Visita();

                    visit.setId(cur.getString(0));
                    visit.setCorreo(cur.getString(1));
                    visit.setNomCiu(cur.getString(2));

                    visitas.add(visit);


                    Log.v(TAG,"ACTIVIDAD TRAE Id " + visit.getId() + "\n Correo " + visit.getCorreo() + "\n Nombre Ciudad " + visit.getNomCiu());

                }while (cur.moveToNext());

            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return visitas;
    }
}

