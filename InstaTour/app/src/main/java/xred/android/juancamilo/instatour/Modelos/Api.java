package xred.android.juancamilo.instatour.Modelos;

import android.content.Context;

import xred.android.juancamilo.instatour.Conexion.Connection;

public class Api {

    private static final String TAG = "error";
    private String Id;
    private String NomCiu;
    private String NomApi;
    Connection conexion ;

    public Api(){
        Id = "";
        NomCiu = "";
        NomApi = "";
    }

    public void SetBd(Context c){
        conexion = new Connection(c, "instatour",null,1);
    }

    public void setId(String iden){
        Id = iden;
    }

    public void setNomCiu(String NombreCiu) {NomCiu = NombreCiu;}

    public void setNomApi(String NombreApi) { NomApi = NombreApi; }

    public String getId(){ return Id; }

    public String getNomCiu() { return NomCiu; }

    public String getNomApi() { return NomApi; }
}
