package br.com.sharkweb.fbv;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.UF;


public class CadastroLocalActivity extends ActionBarActivity {

    private EditText tvtNome;
    private EditText tvEndereco;
    private EditText tvNumero;
    private EditText tvCidade;
    private Spinner spnUF;
    private Button btnCadastrar;
    private Button btnCancelar;

    private String tipoAcesso;
    private Local local;

    private LocalController localControl = new LocalController(this);
    private UFController ufControl = new UFController(this);
    private Funcoes funcoes = new Funcoes(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_local);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvEndereco = (EditText) findViewById(R.id.cadastro_local_endereco);
        tvEndereco.setVisibility(EditText.VISIBLE);

        tvNumero = (EditText) findViewById(R.id.cadastro_local_numero);
        tvNumero.setVisibility(EditText.VISIBLE);

        tvCidade = (EditText) findViewById(R.id.cadastro_local_cidade);
        tvCidade.setVisibility(EditText.VISIBLE);

        tvtNome = (EditText) findViewById(R.id.cadastro_local_nomelocal);
        tvtNome.setVisibility(EditText.VISIBLE);

        spnUF = (Spinner) findViewById(R.id.cadastro_local_spinneruf);
        ArrayList<UF> est = ufControl.selectUF();
        ArrayList<String> estados = new ArrayList<>();

        for (int i = 0; i < est.size(); i++) {
            estados.add(est.get(i).getNome().trim());
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                estados);

        spnUF.setAdapter(arrayAdapter2);
        spnUF.setVisibility(View.VISIBLE);

        btnCadastrar = (Button) findViewById(R.id.cadastro_local_btncadastrar);
        btnCadastrar.setVisibility(View.VISIBLE);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastro_local_btncancelar);
        btnCancelar.setVisibility(View.VISIBLE);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle params = getIntent().getExtras();
        if (params != null) {
            tipoAcesso = params.getString("tipoAcesso");

            switch (tipoAcesso) {
                case "edit":
                    this.local = localControl.selectLocalPorId(params.getInt("id_local")).get(0);
                    carregarRegistro();

                case "write":
                    this.local = null;
                    spnUF.setSelection(22);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        if (this.local != null)
            it.putExtra("id_local", this.local.getId());
        else it.putExtra("id_local", 0);
        setResult(1, it);
        super.onBackPressed();
    }

    private void carregarRegistro() {
        tvtNome.setText(this.local.getNome().trim());
        tvEndereco.setText(this.local.getEndereco().trim());
        tvCidade.setText(this.local.getCidade().trim().toUpperCase());
        tvNumero.setText(this.local.getNumero());

        int if_uf = this.local.getId_uf();
        if_uf = if_uf + 1;
        spnUF.setSelection(if_uf);

        //tvEnderecoLocal.setText(this.);
    }

    private void salvar() {
        String ret = validar();
        if (ret.isEmpty()) {
            int id_uf = (int) spnUF.getSelectedItemId();
            id_uf = id_uf - 1;

            int numero = 0;
            if (!tvNumero.getText().toString().isEmpty()) {
                numero = Integer.valueOf(tvNumero.getText().toString().trim());
            }
            
            Local localinsert = new Local(tvtNome.getText().toString().trim(),
                    tvEndereco.getText().toString().trim(),
                    numero,
                    tvCidade.getText().toString().trim(),
                    id_uf);

            Long retorno = null;
            switch (tipoAcesso) {
                case "edit":
                    localinsert.setId(this.local.getId());
                    retorno = localControl.alterar(localinsert);
                case "write":
                    retorno = localControl.inserir(localinsert);
            }

            if (retorno > 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                toast.show();
                this.local = localinsert;
                onBackPressed();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Erro ao cadastrar o local", Toast.LENGTH_LONG);
                toast.show();
            }

        } else {
            funcoes.mostrarDialogAlert(1,ret);
        }
    }

    private String validar() {
        if (tvtNome.toString().trim().isEmpty()) {
            return "Ops... Faltou informar o nome do local!";
        }

        if (tvEndereco.toString().trim().isEmpty()) {
            return "Ops... Faltou informar o endereço do local!";
        }

        if (tvCidade.toString().trim().isEmpty()) {
            return "Ops... Faltou informar o munucípio do local!";
        }

        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_local, menu);
        return true;
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
