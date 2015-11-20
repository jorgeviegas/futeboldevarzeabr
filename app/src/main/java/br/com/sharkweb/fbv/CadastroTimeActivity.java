package br.com.sharkweb.fbv;

import android.app.AlertDialog;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
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
    private Time time;
    private String tipoAcesso;
    private Button btnCadastrar;
    private Button btnCancelar;

    private TimeController timeControl = new TimeController(this);
    private TimeControllerParse timeControlParse = new TimeControllerParse(this);
    private TimeUsuarioController timeuserControl = new TimeUsuarioController(this);
    private TimeUsuarioControllerParse timeuserControlParse = new TimeUsuarioControllerParse(this);
    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
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
        //txtEmail = (EditText) findViewById(R.id.cadastro_time_edtEmail);
        //txtEmail.setVisibility(EditText.VISIBLE);

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
                ParseObject retorno = timeControlParse.buscarTimePorId(params.getString("id_time"));
                if (retorno != null) {
                    this.time = timeControlParse.ParseObjectToTimeObject(retorno);
                }
            }
        } else {
            tipoAcesso = "write";
            this.time = null;
            spnUF.setSelection(22);
        }

        if (this.time != null) {
            carregarRegistro();
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

    private boolean salvar() {
        if (validarCampos().isEmpty()) {
            String nome = txtNome.getText().toString().trim();
            String cidade = txtCidade.getText().toString().trim().toUpperCase();
            int id_uf = (int) spnUF.getSelectedItemId();
            id_uf = id_uf - 1;

            Time timeInsert;
            if (this.time == null) {
                timeInsert = new Time(nome, cidade, id_uf);
            } else {
                timeInsert = new Time(this.time.getId(), nome, cidade, id_uf);
            }

            if (tipoAcesso.equals("edit")) {
                timeInsert.setId_parse(this.time.getId_parse());
            }

            //ParseObject ret = timeControlParse.salvar(timeInsert);
            Salvar salvar = new Salvar(this.context, timeInsert);
            salvar.execute();
            return true;
        } else {
            return false;
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
        txtNome.setText(this.time.getNome());
        txtCidade.setText(this.time.getCidade());
        int if_uf = this.time.getId_uf();
        if_uf = if_uf + 1;
        spnUF.setSelection(if_uf);

        if (this.tipoAcesso.equals("read")) {
            txtNome.setEnabled(false);
            txtCidade.setEnabled(false);
            spnUF.setEnabled(false);
        }

        if (this.tipoAcesso.equals("edit")) {
            btnCadastrar.setText("Atualizar");
        }
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

    private class Salvar extends AsyncTask<String, Void, ParseObject> {

        private ProgressDialog progress;
        private Context context;
        private Time timeSalvar;

        public Salvar(Context context, Time timeInsert) {
            this.context = context;
            this.timeSalvar = timeInsert;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle("Salvando...");
            progress.setMessage("Aguarde");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected ParseObject doInBackground(String... params) {
            final ParseObject timeParse = new ParseObject("time");

            if (timeSalvar.getId_parse() != null && !timeSalvar.getId_parse().isEmpty()) {
                timeParse.setObjectId(timeSalvar.getId_parse().trim());
            }
            timeParse.put("nome", timeSalvar.getNome().trim());
            timeParse.put("cidade", timeSalvar.getCidade().trim());
            timeParse.put("id_uf", timeSalvar.getId_uf());
            try {
                timeParse.save();
                // if (time != null && !tipoAcesso.equals("edit")) {
                int tipo_usuario = tipoUsuarioControl.selectTiposUsuariosPorTipo("Administrador").get(0).getId();
                ParseObject timeUsuarioParse = new ParseObject("time_usuario");
                timeUsuarioParse.put("id_tipo_usuario", tipo_usuario);

                timeUsuarioParse.put("usuario", ParseObject.createWithoutData("usuario",
                        Constantes.getUsuarioLogado().getIdParse().trim()));
                timeUsuarioParse.put("time", ParseObject.createWithoutData("time",
                        timeParse.getObjectId().trim()));

                timeUsuarioParse.put("inativo", 0);
                timeUsuarioParse.put("posicao", "");
                timeUsuarioParse.save();

                //TENTAR USAR RELATION AO INVÉS DE POINTER NA TABELA

                ParseQuery teste = new ParseQuery("time_usuario");
                teste.whereEqualTo("time", ParseObject.createWithoutData("time",
                        timeParse.getObjectId().trim()));
                teste.find();
                ParseObject retorno = teste.getFirst();
                retorno.getObjectId();
                //}
                timeParse.put("salvo", true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return timeParse;
        }

        @Override
        protected void onPostExecute(ParseObject ret) {
            if (ret != null && ret.getBoolean("salvo")) {
                time = timeControlParse.ParseObjectToTimeObject(ret);
                progress.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                toast.show();
                //Bundle parametros = new Bundle();
                //parametros.putString("id_time", time.getId_parse());
                //mudarTela(TimeDetalheActivity.class, parametros);

            } else {
                progress.dismiss();
                funcoes.mostrarDialogAlert(1, "Não foi possível salvar. Por favor, tente mais tarde.");
            }
        }
    }
}
