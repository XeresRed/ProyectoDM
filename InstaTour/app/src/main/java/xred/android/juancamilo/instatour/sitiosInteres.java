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
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
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
import xred.android.juancamilo.instatour.Adapters.Jsonhttp;
import xred.android.juancamilo.instatour.Modelos.Api;

public class sitiosInteres extends AppCompatActivity implements ApisAdapter.apiAdapterListener {


    private static final String TAG = sitiosInteres.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Api> apiList;
    private ApisAdapter mAdapter;
    private SearchView searchView;
    double latitud = 0.0;
    double longitud = 0.0;
    ArrayList<Double>  Distancias = new ArrayList<>();
    ArrayList<Double>  detalLat = new ArrayList<>();
    ArrayList<Double>  deltaLong = new ArrayList<>();
    ArrayList<Double> cordenadasLat = new ArrayList<>();
    ArrayList<Double> cordenadasLon = new ArrayList<>();
    Pintar obj;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_interes);

        String permiso = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            permiso = b.getString("ciudad");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sitios de interes");

        recyclerView = findViewById(R.id.recycler_view);

        Api a = new Api();
        a.SetBd(this);
        List<Api> apiCiudad = a.traeApi(permiso);
        apiList = apiCiudad;
        mAdapter = new ApisAdapter(this, apiList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DecoracionAdapter(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
    }

    private void locationStart() {
        if(checkLocationPermission()) {
            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                Toast.makeText(this, "LOCACION: " + longitud + " / " + latitud, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "LOCACION:Soy nulo", Toast.LENGTH_SHORT).show();
            }

        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Local);
        }else
        {
            Toast.makeText(this,"No tiene los permisos necesarios", Toast.LENGTH_SHORT).show();
        }

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

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
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
    public void onApiSelected(Api contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.getCate(), Toast.LENGTH_LONG).show();
        seleccion(contact.getCate(), contact.getNomApi());
    }

    private void seleccion(String categoria, String url) {
        Intent i;
        switch (categoria){
            case "Iglesias":
                i = new Intent(this, sitios_tipo_1.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
            case "Veterinarias":
                i = new Intent(this, sitios_tipo_1.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
            case "Supermercados":
                i = new Intent(this, sitios_tipo_1.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
            case "Wi Fi":
                i = new Intent(this, sitios_tipo_1.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
        }
    }

    private void cargaApi(String uri){
        try {
            cordenadasLat.clear();
            cordenadasLon.clear();

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

            for( int i = 0; i < jarrau.length(); i++){
                JSONObject object =jarrau.getJSONObject(i);
                JSONObject c = object.optJSONObject("georeferencia");
                Log.v("SALIDA", object.optString("georeferencia"));

                JSONArray jsonArray = c.getJSONArray("coordinates");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int j=0;j<len;j++){
                        if(j%2 == 0) {
                            deltaLong.add(Double.parseDouble(jsonArray.get(j).toString())-longitud);
                            cordenadasLon.add(Double.parseDouble(jsonArray.get(j).toString()));
                        }else {
                            detalLat.add(Double.parseDouble(jsonArray.get(j).toString())-latitud);
                            cordenadasLat.add(Double.parseDouble(jsonArray.get(j).toString()));
                        }
                    }
                }

            }
            double radio_ecuatiorial = 6378.137;
            for(int x = 0; x < deltaLong.size(); x++){
                double a = Math.pow(Math.sin(detalLat.get(x)/2),2) + Math.cos(cordenadasLat.get(x)) * Math.cos(latitud)  * Math.sin(deltaLong.get(x)/2);
                double c_ = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                Distancias.add(radio_ecuatiorial * c_);
                Log.v("SALIDA2","La distancia en kilometros es: " + radio_ecuatiorial * c_ );
            }
            Intent i = new Intent(this, VerSitio.class);
             //i.putExtra("x",Double.parseDouble(cordenadas.get(0)) );
            //i.putExtra("y",Double.parseDouble(cordenadas.get(1)) );
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        sitiosInteres mainActivity;

        public sitiosInteres getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(sitiosInteres mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            if(latitud == 0.0) {
                latitud = loc.getLatitude();
                longitud = loc.getLongitude();

                String Text = "Mi ubicacion actual es: " + "\n Lat = "
                        + loc.getLatitude() + "\n Long = " + loc.getLongitude();
                Log.v("LOCALIZACION", Text);
                //Toast.makeText(mainActivity,Text,Toast.LENGTH_SHORT).show();
                this.mainActivity.setLocation(loc);
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

    public class Pintar extends AsyncTask<Void,Void,Void>  implements LocationListener{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag=true;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            while (flag){
                publishProgress();
                if (isCancelled()){
                    break;
                }
            }
            return null;
        }
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flag=false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            flag=false;

        }

        sitiosInteres mainActivity;

        public sitiosInteres getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(sitiosInteres mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            Log.v("LOCALIZACION", Text);
            //Toast.makeText(mainActivity,Text,Toast.LENGTH_SHORT).show();
            this.mainActivity.setLocation(loc);
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
