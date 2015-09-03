package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.UF;

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
    private TimeUsuarioController timeuserControl = new TimeUsuarioController(this);
    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private UFController ufControl = new UFController(this);

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
                if (salvar()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    Bundle parametros = new Bundle();
                    parametros.putInt("id_time", time.getId());
                    mudarTela(TimeDetalheActivity.class, parametros);
                }
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
            if (!tipoAcesso.equals("write"))
                this.time = timeControl.selectTimePorId(params.getInt("id_time")).get(0);
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
                timeControl.alterar(timeInsert);
            } else {
                Long ret = timeControl.inserir(timeInsert);
                if (ret > 0) {
                    time = timeControl.selectTimePorId(Integer.valueOf(ret.toString())).get(0);

                    //int tipo_usuario = tipoUsuarioControl.selectTiposUsuariosPorTipo("Jogador").get(0).getId();
                    int tipo_usuario = tipoUsuarioControl.selectTiposUsuariosPorTipo("Administrador").get(0).getId();

                    TimeUsuario timeUser = new TimeUsuario(time.getId(),
                            Constantes.getUsuarioLogado().getId(), 0, "", tipo_usuario);

                    Long ret2 = timeuserControl.inserir(timeUser);
                }
            }
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
}
