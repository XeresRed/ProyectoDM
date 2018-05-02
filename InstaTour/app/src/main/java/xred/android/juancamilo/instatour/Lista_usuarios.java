package xred.android.juancamilo.instatour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import xred.android.juancamilo.instatour.Modelos.Usuario;

public class Lista_usuarios extends AppCompatActivity {
    ArrayList<Usuario> list_user;
    Toolbar tool;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        tool =  findViewById(R.id.tool_bar);

        tool.setTitle("InstaTour");

        setSupportActionBar(tool);

        Usuario user = new Usuario();
        user.SetBd(this);

        list_user = user.traeUsuario();
        if(list_user == null){
            list_user = new ArrayList<>();
        }

        lista = findViewById(R.id.list_users);

        Lista_usuarios.CustomLista customLista = new Lista_usuarios.CustomLista();

        lista.setAdapter(customLista);
    }

    class CustomLista extends BaseAdapter {

        @Override
        public int getCount() {
            return list_user.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.layout_lista_usuarios,null);

            TextView Nombre =  view.findViewById(R.id.textNombre);
            TextView Correo =  view.findViewById(R.id.textCorreo);

            Nombre.setText(list_user.get(i).getNombre());
            Correo.setText(list_user.get(i).getCorreo());

            return view;
        }
    }
}
