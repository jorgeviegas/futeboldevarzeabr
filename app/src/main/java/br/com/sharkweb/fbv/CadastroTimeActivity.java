package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Time;

public class CadastroTimeActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtNome;
    private EditText txtEmail;
    //private EditText txtSenha;

    private Button btnCadastrar;
    private Button btnCancelar;

    private TimeController timeControl = new TimeController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_time);

        txtNome = (EditText) findViewById(R.id.cadastro_time_edtNome);
        txtNome.setVisibility(EditText.VISIBLE);

        //txtEmail = (EditText) findViewById(R.id.cadastro_time_edtEmail);
        //txtEmail.setVisibility(EditText.VISIBLE);

        btnCadastrar = (Button) findViewById(R.id.cadastroTime_btnRegistrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inserir()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                    mudarTela(MainActivity.class);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
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

    private Boolean inserir() {
        if (validarCampos().isEmpty()) {
            String nome = txtNome.getText().toString();

            Time time = new Time(nome);
            timeControl.inserir(time);
            return true;
        } else {
            return false;
        }
    }

    private String validarCampos() {
        String retorno = "";
        if (txtNome.getText().toString().isEmpty()) {
            retorno = retorno + "Nome nao informado \n";
        }
        return retorno;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.time_action_cancelar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja cancelar?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onBackPressed();
                }

            });
            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
