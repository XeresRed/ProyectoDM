package xred.android.juancamilo.instatour;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Adapters.ApisAdapter;
import xred.android.juancamilo.instatour.Adapters.DecoracionAdapter;
import xred.android.juancamilo.instatour.Adapters.ItemsAdapter;
import xred.android.juancamilo.instatour.Json.ItemsTipo1;
import xred.android.juancamilo.instatour.Modelos.Api;

public class sitios_tipo_1 extends AppCompatActivity implements ItemsAdapter.apiAdapterListener{
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
    Double Latitud_user;
    Double Longitud_user;

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
            Latitud_user = b.getDouble("latitud_user");
            Longitud_user = b.getDouble("longitud_user");

        }
        calculaCat(categoria);
        cargaApi(url);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new ItemsAdapter(this,datosapi,this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DecoracionAdapter(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

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
        i.putExtra("lati_user",Latitud_user);
        i.putExtra("longi_user",Longitud_user);
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
                it.setNombre(object.optString(dato1));
                it.setDato1(object.optString(dato2));
                it.setDato2(sobreText + object.optString(dato3));
                it.setDireccion(object.optString(dato4));

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
}
