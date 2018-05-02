package xred.android.juancamilo.instatour;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import xred.android.juancamilo.instatour.Modelos.Administrador;
import xred.android.juancamilo.instatour.Modelos.MD5;

public class MainActivity extends AppCompatActivity {

    EditText identificacion;
    EditText Contraseña;
    Button btnIngresa;
    Button btnRegistro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        identificacion = findViewById(R.id.editCorreo);
        Contraseña = findViewById(R.id.editContraseña);
        btnIngresa = findViewById(R.id.button);
        btnRegistro = findViewById(R.id.button2);

        btnIngresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Administrador admin = new Administrador();
                admin.SetBd(v.getContext());

                MD5 Cifrado = new MD5();

                if (admin.login(identificacion.getText().toString(),Cifrado.md5(Contraseña.getText().toString()))){
                    Intent i = new Intent(v.getContext(), Menu.class);
                    i.putExtra("permiso",admin.getTipo());
                    startActivity(i);
                }else {
                    Toast.makeText(v.getContext(),"Lo siento we :(",Toast.LENGTH_SHORT).show();
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
