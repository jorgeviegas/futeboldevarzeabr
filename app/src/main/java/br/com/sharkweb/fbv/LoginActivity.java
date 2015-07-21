package br.com.sharkweb.fbv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.sharkweb.fbv.controller.UsuarioController;


public class LoginActivity extends ActionBarActivity {

    private EditText txtemail;
    private EditText txtSenha;
    private Button btnLogin;
    private Button btnCadastrar;

    private UsuarioController usuarioControl = new UsuarioController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela de login

            if (params.getBoolean("salvo")) {
                //mudarTela(MainActivity.class);
            }
        }

        txtemail = (EditText) findViewById(R.id.login_txtemail);
        txtemail.setVisibility(EditText.VISIBLE);

        txtSenha = (EditText) findViewById(R.id.login_txtsenha);
        txtSenha.setVisibility(EditText.VISIBLE);

        btnLogin = (Button) findViewById(R.id.login_btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean ret = usuarioControl.validarLogin(txtemail.getText().toString(), txtSenha.getText().toString());
                if (ret) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Login feito com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    Bundle parametros = new Bundle();
                    parametros.putBoolean("login", true);
                    mudarTela(MainActivity.class, parametros);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Usuario nao cadastrado!", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });

        btnCadastrar = (Button) findViewById(R.id.login_btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Bundle parametros = new Bundle();
                // parametros.putBoolean("login", true);
                mudarTela(CadastroUsuarioActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
