package xred.android.juancamilo.instatour;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xred.android.juancamilo.instatour.Adapters.ApisAdapter;
import xred.android.juancamilo.instatour.Adapters.DecoracionAdapter;
import xred.android.juancamilo.instatour.Adapters.ItemsAdapter;
import xred.android.juancamilo.instatour.Json.ItemsTipo1;
import xred.android.juancamilo.instatour.Modelos.Api;

public class sitios_tipo_1 extends AppCompatActivity implements ItemsAdapter.apiAdapterListener,LocationListener {
    private static final String TAG = sitiosInteres.class.getSimpleName();
    private RecyclerView recyclerView;
    private ItemsAdapter mAdapter;
    private SearchView searchView;
    String dato1 = "";
    String dato2 = "";
    String dato3= "";
    String dato4= "";
    Double Latitud;
    Double Longitud;
    String sobreText = "";
    List<ItemsTipo1> datosapi;
    double latitud = 0.0;
    double longitud = 0.0;
    LocationManager mlocManager;
    boolean pass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_tipo_1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sitios de interes");

        String url = "";
        String categoria = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            url = b.getString("link");
            categoria = b.getString("categoria");
        }

        calculaCat(categoria);
        cargaApi(url);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new ItemsAdapter(this,datosapi,this);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DecoracionAdapter(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void locationStart() {
        if(checkLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                return;
            }

            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location1 = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            final AlertDialog dialogo = new AlertDialog.Builder(this).create();
            dialogo.setTitle("¿Quieres seguimiento en tiempo real?");
            dialogo.setMessage("Requiere el uso de red movil o wi fi para poder usar");
            dialogo.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pass= false;
                }
            });
            dialogo.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogo.dismiss();
                }
            });
            dialogo.show();

            if(location1 != null) {
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }else{
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }

            final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }

        }else
        {
            Toast.makeText(this,"No tiene los permisos necesarios", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, location.getLatitude() + " " + location.getLongitude());
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            //Toast.makeText(this, "LOCACION: " + longitud + " / " + latitud, Toast.LENGTH_SHORT).show();

            int r = 6371000; //radio terrestre medio, en metros

            Double c = Math.PI/180; //constante para transformar grados en radianes

            //  Fórmula de haversine
            for(int x = 0; x < datosapi.size(); x++) {
                Double d = 2 * r * Math.asin(Math.sqrt(Math.pow(Math.sin(c * (datosapi.get(x).getLatitud() - latitud) / 2), 2) + Math.cos(c * latitud) * Math.cos(c * datosapi.get(x).getLatitud()) * Math.pow(Math.sin(c * (datosapi.get(x).getLongitud() - longitud) / 2), 2)));
                datosapi.get(x).setDistancia(redondearDecimales(d/1000,2));
                //Toast.makeText(this,"La distancia entre " + datosapi.get(x).getNombre() + " y  ud es de: "  + redondearDecimales(d/1000 ,2)+ " Km", Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
            //en el if de arriba no fue capaz de regresar la ubicacion, asi que entro al listener y aca ya es una ubicacion valida
            if(pass) {
                mlocManager.removeUpdates(this); //para remover el listener y solo escuchar el cambio de ubicacion 1 vez
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
            double parteEntera, resultado;
            resultado = valorInicial;
            parteEntera = Math.floor(resultado);
            resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
            resultado=Math.round(resultado);
            resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
            return resultado;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    Log.v("Dirección","Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculaCat(String categoria) {
        switch (categoria){
            case "Iglesias":
                dato1 = "parroquia";
                dato2 = "horario_de_eucarist_as";
                dato3 = "fiesta_patronal";
                dato4 = "direcci_n";
                sobreText = "Fiestas: ";
                break;
            case "Veterinarias":
                dato1 = "nombre";
                dato2 = "representante";
                dato3 = "telefono";
                dato4 = "direcci_n";
                sobreText = "Teléfono: ";
                break;
            case "Supermercados":
                dato1 = "establecimiento";
                dato2 = "horarios";
                dato3 = "tarjetas_de_cr_dito";
                dato4 = "direcci_n";
                sobreText = "¿Acepta tarjetas de crédito?: ";
                break;
            case "Wi Fi":
                dato1 = "nombre_de_la_zona_wifi";
                dato2 = "ancho_de_banda";
                dato3 = "direcci_n";
                dato4 = "direcci_n";
                sobreText = "";
                break;
            case "Cajeros":
                dato1 = "nombre";
                dato2 = "disponibilidad";
                dato3 = "d_as_de_atencion";
                dato4 = "direccion";
                sobreText = "Tipo de cajero: ";
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apis, (android.view.Menu) menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onApiSelected(ItemsTipo1 contact) {
        //Toast.makeText(getApplicationContext(), "Locacion:  Lat: " + contact.getLatitud() + " / Long: " + contact.getLongitud(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, VerSitio.class);
        i.putExtra("nombre", contact.getNombre()  );
        i.putExtra("direccion", contact.getDireccion()  );
        i.putExtra("lati", contact.getLatitud()  );
        i.putExtra("long", contact.getLongitud() );
        i.putExtra("lati_user",latitud);
        i.putExtra("longi_user",longitud);
        startActivity(i);

    }

    private void cargaApi(String uri){
        try {
            datosapi = new ArrayList<>();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = null;
            HttpURLConnection conn;

            url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jarrau = null;

            jarrau = new JSONArray(json);

            for( int i = 0; i < jarrau.length(); i++) {
                ItemsTipo1 it = new ItemsTipo1();
                JSONObject object = jarrau.getJSONObject(i);
                if(dato2.equals("disponibilidad")){
                    it.setNombre("Cajero de " + object.optString(dato1));
                    it.setDato1("Disponibilidad de " + object.optString(dato3) + " " + object.optString(dato2));
                    it.setDato2(sobreText + object.optString("tipo"));
                    it.setDireccion(object.optString(dato4));
                    it.setDistancia(0.0);
                }else{
                    it.setNombre(object.optString(dato1));
                    it.setDato1(object.optString(dato2));
                    it.setDato2(sobreText + object.optString(dato3));
                    it.setDireccion(object.optString(dato4));
                    it.setDistancia(0.0);
                }

                JSONObject c = object.optJSONObject("georeferencia");
                JSONArray locacion = c.getJSONArray("coordinates");

                if (locacion != null) {
                    int len = locacion.length();
                    for (int j=0;j<len;j++){
                        if(j%2 == 0) {
                            it.setLongitud(Double.parseDouble(locacion.get(j).toString()));
                        }else {
                            it.setLatitud(Double.parseDouble(locacion.get(j).toString()));
                        }
                    }
                }

                Log.v("SALIDA", object.optString(dato1));
                datosapi.add(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        sitios_tipo_1 mainActivity;

        public sitios_tipo_1 getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(sitios_tipo_1 mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            if(latitud == 0.0) {
                latitud = loc.getLatitude();
                longitud = loc.getLongitude();

                    int r = 6371000; //radio terrestre medio, en metros

                    Double c = Math.PI/180; //constante para transformar grados en radianes

                //  Fórmula de haversine
                for(int x = 0; x < datosapi.size(); x++) {
                    Double d = 2 * r * Math.asin(Math.sqrt(Math.pow(Math.sin(c * (datosapi.get(x).getLatitud() - latitud) / 2), 2) + Math.cos(c * latitud) * Math.cos(c * datosapi.get(x).getLatitud()) * Math.pow(Math.sin(c * (datosapi.get(x).getLongitud() - longitud) / 2), 2)));
                    datosapi.get(x).setDistancia(d);
                    Toast.makeText(mainActivity,"La distancia entre " + datosapi.get(x).getNombre() + " y  ud es de: "  + d + " m", Toast.LENGTH_SHORT);
                }
                String Text = "Mi ubicacion actual es: " + "\n Lat = "
                        + loc.getLatitude() + "\n Long = " + loc.getLongitude();



                Log.v("LOCALIZACION", Text);
                //Toast.makeText(mainActivity,Text,Toast.LENGTH_SHORT).show();
                this.mainActivity.setLocation(loc);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //coordenadas.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //coordenadas.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
