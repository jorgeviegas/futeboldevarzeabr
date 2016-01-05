package br.com.sharkweb.fbv;

import android.app.Dialog;
import android.content.Context;
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

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;

public class LoginActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtemail;
    private EditText txtSenha;
    private TextView txtCadastrar;
    private TextView txtEsqueceuSenha;
    private Button btnLogin;
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_pediremail);
                dialog.setTitle("Informe o E-mail:");
                final EditText txtEmail = (EditText) dialog.findViewById(R.id.dialog_pediremail_email);
                final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_pediremail_btnconfirmar);
                btnconfirmar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        if (!txtEmail.getText().toString().isEmpty()) {
                            funcoes.requestPasswordReset(txtEmail.getText().toString().trim());
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btnLogin = (Button) findViewById(R.id.login_btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean retV = validarCampos();
                if (retV) {
                    if (funcoes.verificaConexao(context)) {
                        final Dialog progresso = FuncoesParse.showProgressBar(context, "Fazendo login...");
                        ParseUser.logInInBackground(txtemail.getText().toString().trim(),
                                txtSenha.getText().toString().trim(), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                                        if (e == null && parseUser != null) {
                                            loginSuccessful();
                                        } else if (parseUser == null) {
                                            usernameOrPasswordIsInvalid();
                                        } else {
                                            somethingWentWrong(e);
                                        }
                                        FuncoesParse.dismissProgressBar(progresso);
                                    }
                                });
                    } else {
                        funcoes.mostrarDialogAlert(1, "Sem conexão com internet!");
                    }
                }
            }
        });
    }

    public void loginSuccessful() {
        Bundle parametros = new Bundle();
        parametros.putBoolean("login", true);
        mudarTela(NewMainActivity.class, parametros);
        Toast toast = Toast.makeText(getApplicationContext(), "Login feito com sucesso!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void usernameOrPasswordIsInvalid() {
        Toast toast2 = Toast.makeText(getApplicationContext(), "E-mail ou Senha incorreto(a).", Toast.LENGTH_SHORT);
        toast2.show();
    }

    public void somethingWentWrong(com.parse.ParseException e) {
        if (e.getCode() == 100) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ops... Houve algum problema de comunicação. Verifique sua conexão.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Falha ao realizar login. Por favor, tente novamente.", Toast.LENGTH_SHORT);
            toast.show();
        }

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
