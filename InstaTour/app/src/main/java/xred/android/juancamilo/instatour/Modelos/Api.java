package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Api {

    private static final String TAG = "error";
    private String Id;
    private String NomCiu;
    private String NomApi;
    private String tok;
    private String cate;
    private int img;
    Connection conexion ;
    List<Api> apis = null;

    public Api(){
        Id = "";
        NomCiu = "";
        NomApi = "";
        tok = "";
        cate = "";
        img = 0;
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour1",null,1);
    }

    public void setId(String iden){
        Id = iden;
    }

    public void setNomCiu(String NombreCiu) {NomCiu = NombreCiu;}

    public void setNomApi(String NombreApi) { NomApi = NombreApi; }

    public void setTok(String tok) { this.tok = tok; }

    public void setCate(String cate) { this.cate = cate; }

    public void setImg(int img) { this.img = img; }


    public String getId(){ return Id; }

    public String getNomCiu() { return NomCiu; }

    public String getNomApi() { return NomApi; }

    public String getTok() { return tok; }

    public String getCate() { return cate; }

    public int getImg() { return img; }





    public List<Api> traeApi(String NombreCiudad){

        try{
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT id, NombreCiu , NombreApi , token , categoria , imagen FROM api WHERE NombreCiu='"+NombreCiudad+"' ",null);

            if(cur.moveToFirst()){
                apis = new ArrayList<>();
                do{

                    Api ap = new Api();

                    ap.setId(cur.getString(0));
                    ap.setNomCiu(cur.getString(1));
                    ap.setNomApi(cur.getString(2));
                    ap.setTok(cur.getString(3));
                    ap.setCate(cur.getString(4));
                    ap.setImg(cur.getInt(5));

                    apis.add(ap);


                    Log.v(TAG,"ACTIVIDAD TRAE Api " + ap.getId() + "\n Nombre Ciudad " + ap.getNomCiu() + "\n Nombre Api " + ap.getNomApi()+ " \n Token" + ap.getTok() + " \n Categoria " + ap.getCate() + "\n imagen" + ap.getImg());

                }while (cur.moveToNext());

            }
            db.close();

        }catch (Exception e){
            Log.e(TAG,"CARGA" + e.getMessage());
        }
        return apis;
    }

    public void registraApi(String idApi, String nombreCiudad, String nombreApi , String tok, String categ , int img){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String query = "INSERT INTO api (id, NombreCiu , NombreApi , token , categoria , imagen) VALUES ('" + idApi + "', '" + nombreCiudad+ "', '"+ nombreApi + "' , '" + tok + "' , '" + categ +"' , '"+ img  +"')";
        db.execSQL(query);
        db.close();
    }


}
