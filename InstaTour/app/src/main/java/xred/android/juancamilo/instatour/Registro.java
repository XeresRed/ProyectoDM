package xred.android.juancamilo.instatour;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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
import xred.android.juancamilo.instatour.Modelos.Usuario;

public class Registro extends AppCompatActivity {

    EditText identificacion;
    EditText CorreoE;
    Spinner tipo;
    EditText Contraseña;
    EditText Contraseña1;
    Button btnRegistra;
    ImageButton btn;
    Toolbar t;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        t = findViewById(R.id.tool_bar);
        t.setTitle("Instatour");
        setSupportActionBar(t);

        btnRegistra = findViewById(R.id.button3);
        CorreoE = findViewById(R.id.editMailAdmin);
        identificacion = findViewById(R.id.idUser);
        Contraseña = findViewById(R.id.pass);
        Contraseña1 = findViewById(R.id.pass1);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_ID);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_pass);

        identificacion.addTextChangedListener(new MyTextWatcher(identificacion));
        CorreoE.addTextChangedListener(new MyTextWatcher(CorreoE));
        Contraseña.addTextChangedListener(new MyTextWatcher(Contraseña));

         btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario client = new Usuario();
                client.SetBd(v.getContext());
                MD5 EN = new MD5();
                String id = identificacion.getText().toString();
                String email = CorreoE.getText().toString().replace(" ","");
                String password = EN.md5(Contraseña.getText().toString());
                String password1 = EN.md5(Contraseña1.getText().toString());

                if(password.equals(password1)) {
                    String m = client.registro( email, id, password,"user");
                    if(m.equals("r")){
                        Toast.makeText(v.getContext(),"¡Insta fantastico! Bienvenido aventurero",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(v.getContext(),"¡Oh no! Tenemos a otro aventurero registrado con ese correo",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(v.getContext(),"¡Oh no! Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private boolean validateName() {
        if (identificacion.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(identificacion);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = CorreoE.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(CorreoE);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (Contraseña.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(Contraseña);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.idUser:
                    validateName();
                    break;
                case R.id.editMailAdmin:
                    validateEmail();
                    break;
                case R.id.pass:
                    validatePassword();
                    break;
            }
        }
    }
}
