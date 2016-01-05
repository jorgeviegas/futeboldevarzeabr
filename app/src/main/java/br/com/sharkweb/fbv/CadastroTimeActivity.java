package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controllerParse.TimeControllerParse;
import br.com.sharkweb.fbv.controllerParse.TimeUsuarioControllerParse;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.UF;
import br.com.sharkweb.fbv.model.Usuario;

public class CadastroTimeActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtNome;
    private EditText txtCidade;
    private Spinner spnUF;
    private String tipoAcesso;
    private Button btnCadastrar;
    private Button btnCancelar;

    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private ParseObject time;
    private UFController ufControl = new UFController(this);
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_time);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.cadastro_time_edtNome);
        txtNome.setVisibility(EditText.VISIBLE);

        txtCidade = (EditText) findViewById(R.id.cadastro_time_edtCidade);
        txtCidade.setVisibility(EditText.VISIBLE);

        spnUF = (Spinner) findViewById(R.id.cadastro_time_ufspinner);
        spnUF.setVisibility(EditText.VISIBLE);
        ArrayList<UF> est = ufControl.selectUF();
        ArrayList<String> estados = new ArrayList<>();

        for (int i = 0; i < est.size(); i++) {
            estados.add(est.get(i).getNome().trim());
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                estados);

        spnUF.setAdapter(arrayAdapter2);
        spnUF.setVisibility(View.VISIBLE);

        btnCadastrar = (Button) findViewById(R.id.cadastro_time_btncadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastro_time_btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle params = getIntent().getExtras();
        if (params != null) {
            tipoAcesso = params.getString("tipoAcesso");
            if (!tipoAcesso.equals("write")) {
                this.time = Constantes.getSessao().getObjeto();
                Constantes.setSessao(null);
                carregarRegistro();
            }
        } else {
            time = new ParseObject("time");
            tipoAcesso = "write";
            spnUF.setSelection(22);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_time, menu);
        return true;
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }

    private void salvar() {
        String validacao = validarCampos().trim();
        if (validacao.isEmpty()) {
            final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando time...");
            final int tipo_usuario = tipoUsuarioControl.selectTiposUsuariosPorTipo("Administrador").get(0).getId();
            time.put("nome", txtNome.getText().toString().trim());
            time.put("cidade", txtCidade.getText().toString().trim());
            int id_uf = (int) spnUF.getSelectedItemId();
            id_uf = id_uf - 1;
            time.put("id_uf", id_uf);
            if (time.getObjectId() == null || time.getObjectId().isEmpty()) {
                time.getRelation("usuarios").add(ParseUser.getCurrentUser());
                time.saveInBackground(new SaveCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            ParseUser.getCurrentUser().getRelation("times").add(time);
                            ArrayList<String> configs = new ArrayList<String>();
                            configs.add(time.getObjectId());
                            configs.add("0");
                            configs.add(String.valueOf(tipo_usuario));
                            ParseUser.getCurrentUser().add("configTimes", configs);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                public void done(com.parse.ParseException e) {
                                    FuncoesParse.dismissProgressBar(progresso);
                                    Constantes.setTimeSelecionado(time);
                                    if (e == null) {
                                        cadastroEfetuado();
                                    } else {
                                        falhaNoCadastro(e);
                                    }
                                }
                            });
                        } else {
                            FuncoesParse.dismissProgressBar(progresso);
                            falhaNoCadastro(e);
                        }
                    }
                });
            } else {
                time.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        FuncoesParse.dismissProgressBar(progresso);
                        Constantes.setTimeSelecionado(time);
                        if (e == null) {
                            cadastroEfetuado();
                        } else {
                            falhaNoCadastro(e);
                        }
                    }
                });
            }
        } else {
            funcoes.mostrarDialogAlert(1, validacao);
        }
    }

    private String validarCampos() {
        if (txtNome.getText().toString().isEmpty()) {
            return "Nome nao informado";
        }
        if (txtCidade.getText().toString().isEmpty()) {
            return "Cidade nao informado";
        }
        return "";
    }

    private void carregarRegistro() {
        txtNome.setText(time.getString("nome").trim());
        txtCidade.setText(time.getString("cidade").trim());
        int if_uf = time.getInt("id_uf");
        if_uf = if_uf + 1;
        spnUF.setSelection(if_uf);

        if (tipoAcesso.equals("read")) {
            txtNome.setEnabled(false);
            txtCidade.setEnabled(false);
            spnUF.setEnabled(false);
        }
    }

    private void cadastroEfetuado() {
        funcoes.mostrarToast(1);
        mudarTela(TimeDetalheActivity.class);
    }

    private void falhaNoCadastro(com.parse.ParseException e) {
        funcoes.mostrarToast(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.cadastro_time_settings) {
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
