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
import android.widget.ImageView;
import android.widget.TextView;
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
    boolean flag = false;
    TextView t1,t2;
    ImageView im1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_interes);

        String permiso = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            permiso = b.getString("ciudad");
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
        //whiteNotificationBar(recyclerView);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DecoracionAdapter(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
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
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getCate(), Toast.LENGTH_LONG).show();
        seleccion(contact.getCate(), contact.getNomApi(), contact.getTok());
    }

    private void seleccion(String categoria, String url,String key) {
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
            case "Cajeros":
                i = new Intent(this, sitios_tipo_1.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
            case "Parques":
                i = new Intent(this, sitios_tipo_2.class);
                i.putExtra("link", url );
                i.putExtra("key", key );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
            case "Gasolineras":
                i = new Intent(this, sitios_tipo_2.class);
                i.putExtra("link", url );
                i.putExtra("categoria",categoria );
                i.putExtra("latitud_user", latitud );
                i.putExtra("longitud_user",longitud );
                startActivity(i);
                break;
        }
    }
}
