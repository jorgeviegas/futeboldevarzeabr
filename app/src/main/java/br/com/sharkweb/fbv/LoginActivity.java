package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.LoginController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Usuario;

public class LoginActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtemail;
    private EditText txtSenha;
    private TextView txtCadastrar;
    private TextView txtEsqueceuSenha;
    private Button btnLogin;
    //private Button btnCadastrar;
    private UsuarioController usuarioControl = new UsuarioController(this);
    private LoginController loginControl = new LoginController(this);

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
                    Boolean ret = usuarioControl.validarLogin(txtemail.getText().toString(), txtSenha.getText().toString());
                    if (ret) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Login feito com sucesso!", Toast.LENGTH_LONG);
                        toast.show();

                        Usuario user = usuarioControl.selectUsuarioPorEmailouApelido(txtemail.getText().toString()).get(0);

                        if (loginControl.selecLogin().isEmpty()) {
                            loginControl.inserir(user.getId());
                        }else{
                            loginControl.alterar(1,user.getId());
                        }
                        Constantes.setUsuarioLogado(user);

                        Bundle parametros = new Bundle();
                        parametros.putBoolean("login", true);
                        mudarTela(NewMainActivity.class, parametros);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Usuario não cadastrado!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

       /* btnCadastrar = (Button) findViewById(R.id.login_btnCadastrar);
        btnCadastrar.setVisibility(View.GONE);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/
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

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
      /*  if (item.getItemId() == R.id.login_action_cadastrar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja cadastrar um novo Usuario?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //finish();
                    Bundle parametros = new Bundle();
                    parametros.putString("tipoAcesso", "write");
                    mudarTela(CadastroUsuarioActivity.class, parametros);
                }

            });
            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
