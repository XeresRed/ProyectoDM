package xred.android.juancamilo.instatour.Json;

public class ItemsTipo2 {
    private String nombre;
    private String dato1;
    private String dato2;
    private String dato3;
    private String direccion;
    private Double Latitud;
    private Double Longitud;
    private String url;
    private double distancia;

    public ItemsTipo2(){
        nombre = "";
        dato1 = "";
        dato2 = "";
        dato3 = "";
        direccion = "";
        Latitud = 0.0;
        Longitud = 0.0;
        distancia = 0.0;
        url = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDato1() {
        return dato1;
    }

    public void setDato1(String dato1) {
        this.dato1 = dato1;
    }

    public String getDato2() {
        return dato2;
    }

    public void setDato2(String dato2) {
        this.dato2 = dato2;
    }

    public String getDato3() {
        return dato3;
    }

    public void setDato3(String dato3) {
        this.dato3 = dato3;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
