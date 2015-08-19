package br.com.sharkweb.fbv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.JogoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Time;

public class CadastroJogoActivity extends ActionBarActivity {

    private Time time;
    private Time time2;
    private String data;
    private TimeController timecontrol = new TimeController(this);
    private JogoController jogoControl = new JogoController(this);
    private Funcoes funcoes = new Funcoes(this);
    private String tipoAcesso;
    private Jogo jogo;
    final Context context = this;
    private TimeListAdapter adapterTimeList;

    private TextView tvTime1;
    private TextView tvTime2;
    private TextView tvData;
    private TextView tvHora;
    private TextView tvHorafinal;
    private Button btnSearchTime1;
    private Button btnSearchTime2;
    private Button btnSalvar;
    private Button btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_jogo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.soccerplain);
        // actionBar.setDisplayShowHomeEnabled(true);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            tipoAcesso = params.getString("tipoAcesso");

            if (tipoAcesso.equals("write")) {
                time = timecontrol.selectTimePorId(params.getInt("id_time")).get(0);
                data = params.getString("data");
            } else {
                //Obviamente vai ser modo read ou edit.
                jogo = jogoControl.selectJogoPorId(params.getInt("id_jogo")).get(0);
            }
        } else {
            time = null;
            time2 = null;
            data = null;
            jogo = null;
        }

        tvTime1 = (EditText) findViewById(R.id.cadastrojogo_time1);
        tvTime1.setVisibility(EditText.VISIBLE);

        tvTime2 = (EditText) findViewById(R.id.cadastrojogo__time2);
        tvTime2.setVisibility(EditText.VISIBLE);

        tvData = (EditText) findViewById(R.id.cadastrojogo_data);
        tvData.setVisibility(EditText.VISIBLE);
        //tvData.setAutoLinkMask(dat);
        /*tvHora.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (tvHora.getText().toString().length() == 5)     //size as per your requirement
                {
                    tvHorafinal.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/
        tvHora = (EditText) findViewById(R.id.cadastrojogo_horaInicio);
        tvHora.setVisibility(EditText.VISIBLE);

        tvHorafinal = (EditText) findViewById(R.id.cadastrojogo_horafinal);
        tvHorafinal.setVisibility(EditText.VISIBLE);

        btnSearchTime1 = (Button) findViewById(R.id.cadastrojogo_botaotime1);
        btnSearchTime1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheTime(1);
            }
        });


        btnSearchTime2 = (Button) findViewById(R.id.cadastrojogo_botaotime2);
        btnSearchTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheTime(2);
            }
        });

        btnSalvar = (Button) findViewById(R.id.cadastrojogo_btnsalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastrojogo_btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!tipoAcesso.equals("write")) {
            carregarRegistro();
        } else {
            tvData.setText(data);
            tvTime1.setText(time.getNome().trim().toUpperCase());
            //btnSearchTime1.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int id_time = data.getExtras().getInt("id_time");
        if (id_time > 0) {
            Time timeret = timecontrol.selectTimePorId(id_time).get(0);
            switch (requestCode) {
                case 1:
                    this.time = timeret;
                    tvTime1.setText(this.time.getNome().trim().toUpperCase());
                    break;
                case 2:
                    this.time2 = timeret;
                    tvTime2.setText(this.time2.getNome().trim().toUpperCase());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void EscolheTime(int key) {
        Bundle parametros = new Bundle();
        parametros.putBoolean("esperaRetorno", true);
        mudarTelaComRetorno(TeamActivity.class, parametros, key);

    }

    private void carregarRegistro() {
        if (jogo != null) {
            time = timecontrol.selectTimePorId(jogo.getId_time()).get(0);
            time2 = timecontrol.selectTimePorId(jogo.getId_time2()).get(0);

            tvTime1.setText(time.getNome().trim().toUpperCase());
            tvTime2.setText(time2.getNome().trim().toUpperCase());
            tvData.setText(jogo.getData().trim());
            tvHora.setText(jogo.getHora().trim());
            tvHorafinal.setText(jogo.getHoraFinal().trim());
        }

        if (tipoAcesso.equals("read")) {
            tvHora.setEnabled(false);
            tvHorafinal.setEnabled(false);
            tvData.setEnabled(false);
            tvTime1.setEnabled(false);
            tvTime2.setEnabled(false);
            btnSearchTime1.setEnabled(false);
            btnSearchTime2.setEnabled(false);
        }
    }

    private void salvar() {
        String validacao = validar();
        if (validacao.isEmpty()) {
            Jogo jogo = new Jogo(time.getId(), time2.getId(), 1, tvData.getText().toString(),
                    tvHora.getText().toString().trim(), tvHorafinal.getText().toString().trim(), 0);

            if (tipoAcesso.equals("edit")) {
                jogo.setId(this.jogo.getId());
                jogoControl.alterar(jogo);
            } else {
                jogoControl.inserir(jogo);
            }
            Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        } else {
            funcoes.mostrarDialogAlert(0, "Informativo", validacao);
        }
    }

    private String validar() {
        if (time == null) {
            return "Ops.. Faltou informar quem é o time da casa!";
        }
        if (time2 == null) {
            return "Ops.. Faltou informar quem é o time visitante!";
        }
        if (tvData.getText().toString().trim().equals("")) {
            return "Hummmm... Faltou informar a data do nosso jogo!";
        }
        if (tvHora.getText().toString().trim().equals("") ||
                tvHorafinal.getText().toString().trim().equals("")) {
            return "Hummmm... Faltou informar o horario do nosso jogo!";
        }
        /*if (Double.valueOf(String.valueOf(tvHorafinal.getText()).replace(":","")) >
                Double.valueOf(String.valueOf(tvHora.getText()).replace(":",""))){
            return "Ops... A hora final não pode ser maior que a inicial!";
        }*/
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_jogo, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, Bundle parametros, int key) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivityForResult(intent, key);
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }
}
