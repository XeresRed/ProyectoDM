package xred.android.juancamilo.instatour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xred.android.juancamilo.instatour.Modelos.Administrador;
import xred.android.juancamilo.instatour.Modelos.MD5;

public class Registro extends AppCompatActivity {

    EditText identificacion;
    EditText CorreoE;
    Spinner tipo;
    EditText Contraseña;
    EditText Contraseña1;
    Button btnRegistra;
    ImageButton btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tipo = (Spinner)findViewById(R.id.tipo_spinner);

        ArrayAdapter<CharSequence>  Adapter = ArrayAdapter.createFromResource(this,R.array.tipo, android.R.layout.simple_spinner_item);
        tipo.setAdapter(Adapter);

        btnRegistra = findViewById(R.id.button3);
        CorreoE = findViewById(R.id.editMailAdmin);
        identificacion = findViewById(R.id.idUser);
        Contraseña = findViewById(R.id.pass);
        Contraseña1 = findViewById(R.id.pass1);


         btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Administrador admin = new Administrador();
                admin.SetBd(v.getContext());
                MD5 EN = new MD5();
                String id = identificacion.getText().toString();
                String email = CorreoE.getText().toString().replace(" ","");
                String password = EN.md5(Contraseña.getText().toString());
                String password1 = EN.md5(Contraseña1.getText().toString());
                String permisos = tipo.getSelectedItem().toString();

                if(password.equals(password1)) {
                    admin.registro(id, email, password, permisos);
                    Toast.makeText(v.getContext(),"Usuario Registrado",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
