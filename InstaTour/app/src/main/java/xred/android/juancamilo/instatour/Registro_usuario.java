package xred.android.juancamilo.instatour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xred.android.juancamilo.instatour.Modelos.MD5;
import xred.android.juancamilo.instatour.Modelos.Usuario;

public class Registro_usuario extends AppCompatActivity {

    private Toolbar tool;
    EditText Nombre;
    EditText Correo;
    EditText pass1;
    EditText pass2;
    Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        tool =  findViewById(R.id.tool_bar);

        tool.setTitle("InstaTour");

        setSupportActionBar(tool);

        Nombre = findViewById(R.id.editNombre);
        Correo = findViewById(R.id.editMail);
        pass1 = findViewById(R.id.editContEst);
        pass2 = findViewById(R.id.editRepContEs);
        btnRegistrar = findViewById(R.id.buttonRegistrarEst);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario user = new Usuario();
                user.SetBd(v.getContext());

                String Nombre_estudiante = Nombre.getText().toString();
                String Correo_estudiante = Correo.getText().toString();
                String pass_estudiante = pass1.getText().toString();
                String pass1_estudiante = pass2.getText().toString();



                if(pass_estudiante.equals(pass1_estudiante))
                {
                    MD5 en = new MD5();
                    user.registro(Correo_estudiante,en.md5(pass_estudiante),Nombre_estudiante);
                    Toast.makeText(v.getContext(),"Se registro el estudiante de manera correcta",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(v.getContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
