package xred.android.juancamilo.instatour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    Button btnReg;
    Button btnVer;
    Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tool =  findViewById(R.id.tool_bar);

        tool.setTitle("InstaTour");

        setSupportActionBar(tool);

        String permiso = "";
        Bundle b = getIntent().getExtras();
        if(b != null){
            permiso = b.getString("permiso");
        }


        btnReg = findViewById(R.id.buttonRegistrar);

        if(permiso.equals("Gerente")){
            btnReg.setVisibility(View.INVISIBLE);
        }else if(permiso.equals("Admin")){
            btnReg.setVisibility(View.INVISIBLE);
        }


        btnVer = findViewById(R.id.buttonVer);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Registro_usuario.class);
                startActivity(i);
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Lista_usuarios.class);
                startActivity(i);
            }
        });

    }
}
