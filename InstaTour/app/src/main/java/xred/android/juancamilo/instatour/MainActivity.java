package xred.android.juancamilo.instatour;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xred.android.juancamilo.instatour.Modelos.Api;
import xred.android.juancamilo.instatour.Modelos.Ciudad;
import xred.android.juancamilo.instatour.Modelos.MD5;
import xred.android.juancamilo.instatour.Modelos.Usuario;

public class MainActivity extends AppCompatActivity {

    EditText identificacion;
    EditText Contraseña;
    Button btnIngresa;
    TextView btnRegistro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        identificacion=findViewById(R.id.editCorreo);
        Contraseña=findViewById(R.id.editContraseña);
        btnIngresa = findViewById(R.id.button);
        btnRegistro = findViewById(R.id.textViewReg);
        Ciudad c = new Ciudad();
        c.SetBd(this);
        btnIngresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario client = new Usuario();
                client.SetBd(v.getContext());
                Ciudad c = new Ciudad();
                c.SetBd(v.getContext());
                if(c.traeCiudad().isEmpty()){
                    c.registraCiudad("Guadalajara de Buga","Buga es una ciudad situada al oeste del departamento Valle del Cauca (Colombia). Es conocida por su emblemática basílica del Señor de los Milagros","http://valleesvalle.com/web/media/k2/items/cache/a07bb170c4a36161aa1f8f4859c19794_L.jpg");

                    Api a = new Api();
                    a.SetBd(v.getContext());
                    a.registraApi("1","Guadalajara de Buga","https://www.datos.gov.co/resource/3e2q-shug.json","AYNJSESuzmjGW1XaqaxlCoypr","Iglesias" , R.drawable.iglesia );
                    a.registraApi("2","Guadalajara de Buga","https://www.datos.gov.co/resource/bk5m-dhdb.json","AYNJSESuzmjGW1XaqaxlCoypr","Cajeros" , R.drawable.cajero );
                    a.registraApi("3","Guadalajara de Buga","https://www.datos.gov.co/resource/tbnf-tvbj.json","AYNJSESuzmjGW1XaqaxlCoypr","Parques" , R.drawable.park);
                    a.registraApi("4","Guadalajara de Buga","https://www.datos.gov.co/resource/jas6-84i5.json","AYNJSESuzmjGW1XaqaxlCoypr","Supermercados" , R.drawable.superm);
                    a.registraApi("5","Guadalajara de Buga","https://www.datos.gov.co/resource/n4ms-tprw.json","AYNJSESuzmjGW1XaqaxlCoypr","Wi Fi" , R.drawable.wf);
                    a.registraApi("6","Guadalajara de Buga","https://www.datos.gov.co/resource/syfn-73i7.json","AYNJSESuzmjGW1XaqaxlCoypr","Gasolineras" , R.drawable.gasolinera);
                    a.registraApi("7","Guadalajara de Buga","https://www.datos.gov.co/resource/9n8c-kkfd.json","AYNJSESuzmjGW1XaqaxlCoypr","Veterinarias", R.drawable.vete);
                }

                MD5 Cifrado = new MD5();
                if (client.login(identificacion.getText().toString(),Cifrado.md5(Contraseña.getText().toString()))){
                    Intent i = new Intent(v.getContext(), Menu.class);
                    i.putExtra("permiso",client.getCorreo());
                    i.putExtra("tipo",client.getTipo());
                    startActivity(i);
                }else {
                    Toast.makeText(v.getContext(),"Aventurero ingresaste datos Incorrectos :(",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Registro.class);
                startActivity(i);
            }
        });

    }

}
