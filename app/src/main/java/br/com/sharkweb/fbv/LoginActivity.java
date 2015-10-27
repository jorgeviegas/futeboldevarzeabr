package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.LoginController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Login;
import br.com.sharkweb.fbv.model.Usuario;

public class LoginActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtemail;
    private EditText txtSenha;
    private TextView txtCadastrar;
    private TextView txtEsqueceuSenha;
    private Button btnLogin;
    private UsuarioController usuarioControl = new UsuarioController(this);
    private LoginController loginControl = new LoginController(this);
    private ArrayList<Usuario> usuarioLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela de login
        }

        txtemail = (EditText) findViewById(R.id.login_txtemail);
        txtemail.setVisibility(EditText.VISIBLE);

        txtSenha = (EditText) findViewById(R.id.login_txtsenha);
        txtSenha.setVisibility(EditText.VISIBLE);

        txtCadastrar = (TextView) findViewById(R.id.login_cadastrar);
        txtCadastrar.setVisibility(EditText.VISIBLE);
        txtCadastrar.setTextColor(Color.WHITE);

        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle parametros = new Bundle();
                parametros.putString("tipoAcesso", "write");
                mudarTela(CadastroUsuarioActivity.class, parametros);
            }
        });
        txtEsqueceuSenha = (TextView) findViewById(R.id.login_esqueceu_senha);
        txtEsqueceuSenha.setVisibility(EditText.VISIBLE);
        txtEsqueceuSenha.setTextColor(Color.WHITE);
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        btnLogin = (Button) findViewById(R.id.login_btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean retV = validarCampos();
                if (retV) {
                    if (usuarioLogin == null || usuarioLogin.size() == 0 ||
                            ((usuarioControl.validarEmail(txtemail.getText().toString().trim())
                                    && !usuarioLogin.get(0).getEmail().toString().trim().
                                    equals(txtemail.getText().toString().trim()))
                                    ||
                                    (!usuarioControl.validarEmail(txtemail.getText().toString().trim())
                                            && !usuarioLogin.get(0).getApelido().toString().trim().
                                            equals(txtemail.getText().toString().trim())))) {
                        usuarioLogin = usuarioControl.selectUsuarioPorEmailouApelido(txtemail.getText().toString().trim(), true);
                    }

                    if (usuarioLogin.size() > 0) {
                        Usuario user = usuarioLogin.get(0);
                        if (usuarioControl.validarLogin(user, txtSenha.getText().toString().trim())) {

                            Login login = new Login(user.getIdParse().trim());
                            if (loginControl.selecLogin().isEmpty()) {
                                Long ret = loginControl.inserir(login);
                                if (ret > 0) {
                                    usuarioControl.inserir(user, false);
                                }
                            } else {
                                String id_parse_old = loginControl.selecLogin().get(0).getIdParse().trim();
                                user.setCodigo(id_parse_old);
                                login.setId(1);
                                loginControl.alterar(login);
                                usuarioControl.alterar(user, false);
                            }
                            Toast toast = Toast.makeText(getApplicationContext(), "Login feito com sucesso!", Toast.LENGTH_LONG);
                            toast.show();

                            Constantes.setUsuarioLogado(user);
                            Bundle parametros = new Bundle();
                            parametros.putBoolean("login", true);
                            mudarTela(NewMainActivity.class, parametros);
                        } else {
                            Toast toast2 = Toast.makeText(getApplicationContext(), "Senha incorreta.", Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    } else {
                        Toast toast2 = Toast.makeText(getApplicationContext(), "Usuario não encontrado!", Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                }
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }

    private boolean validarCampos() {
        if (txtemail.getText().toString().isEmpty() || txtSenha.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
