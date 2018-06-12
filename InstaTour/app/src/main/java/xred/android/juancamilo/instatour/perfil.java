package xred.android.juancamilo.instatour;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import xred.android.juancamilo.instatour.Modelos.Usuario;

public class perfil extends AppCompatActivity {

    private Toolbar tool;
    TextView correo;
    TextView nombre;
    TextView contraseña;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        String permiso = "";
        Bundle b = getIntent().getExtras();
        if(b != null) {
            permiso = b.getString("usuario");
        }
        tool = findViewById(R.id.tool_bar);
        usuario = new Usuario();
        usuario.SetBd(this);
        usuario.busquedaUsuario(permiso);

        correo = findViewById(R.id.txtcorreo);
        nombre = findViewById(R.id.txtEdicionNombre);
        contraseña = findViewById(R.id.txtcontraseña);

        correo.setText(usuario.getCorreo());
        nombre.setText(usuario.getNombre());
        contraseña.setText(usuario.getContraseña());

        }







}
