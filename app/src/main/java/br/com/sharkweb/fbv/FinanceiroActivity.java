package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.ParseProxyObject;
import br.com.sharkweb.fbv.model.Time;

/**
 * Created by Jorge on 30/09/2015.
 */
public class FinanceiroActivity extends AppCompatActivity {

    final Context context = this;
    private Funcoes funcoes = new Funcoes(this);
    private MovimentoController movimentoControl = new MovimentoController(this);
    private double valor = 0;
    private ParseObject caixa;
    private ParseProxyObject time;

    private TextView txtSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtSaldo = (TextView) findViewById(R.id.txtSaldo);

        Intent intent = getIntent();
        ParseProxyObject ppo = (ParseProxyObject) intent.getSerializableExtra("parseObject");
        if (ppo != null) {
            this.time = ppo;
        }

        carregarRegistro();
    }

    private void carregarRegistro() {
        final Dialog progresso = FuncoesParse.showProgressBar(this.context, "Carregando...");
        ParseQuery query = new ParseQuery("caixa");
        query.whereEqualTo("time", ParseObject.createWithoutData("time", time.getObjectId().trim()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    caixa = parseObject;
                    atualizarSaldo();
                } else {
                    if (e.getCode() == 101) {
                        criarCaixa();
                    } else {
                        funcoes.mostrarToast(4);
                    }
                }
            }
        });
    }

    private void criarCaixa() {
        final Dialog progresso = FuncoesParse.showProgressBar(this.context, "Criando novo caixa...");
        final ParseObject caixaParse = new ParseObject("caixa");
        caixaParse.put("time", ParseObject.createWithoutData("time", time.getObjectId().trim()));
        caixaParse.put("saldo", 0.00);
        caixaParse.put("visivel", true);
        caixaParse.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    caixa = caixaParse;
                    cadastroEfetuado();
                    atualizarSaldo();
                } else {
                    falhaNoCadastro(e);
                }
            }
        });

    }

    private void cadastroEfetuado() {
        funcoes.mostrarToast(1);
    }

    private void falhaNoCadastro(com.parse.ParseException e) {
        funcoes.mostrarToast(2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_financeiro, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.action_cadastrar_entrada);
        m1.setVisible(true);
        MenuItem m2 = menu.findItem(R.id.action_cadastrar_retirada);
        m1.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cadastrar_entrada) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_financeiro);
            dialog.setTitle("Nova Entrada");

            final TextView tvObs = (TextView) dialog.findViewById(R.id.dialog_financeiro_obs);
            final TextView tvValor = (TextView) dialog.findViewById(R.id.dialog_financeiro_valor);

            final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_financeiro_btnConfirmar);
            btnconfirmar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    valor = Double.valueOf(tvValor.getText().toString());
                    dialog.dismiss();
                    movimentoControl.criarMovimento("E", caixa, valor, 0, tvObs.getText().toString().trim());
                    atualizarSaldo();
                }
            });

            dialog.show();
            return true;
        }
        if (id == R.id.action_cadastrar_retirada) {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_financeiro);
            dialog.setTitle("Nova Retirada");

            final TextView tvObs = (TextView) dialog.findViewById(R.id.dialog_financeiro_obs);
            final TextView tvValor = (TextView) dialog.findViewById(R.id.dialog_financeiro_valor);

            final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_financeiro_btnConfirmar);
            btnconfirmar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    valor = Double.valueOf(tvValor.getText().toString());
                    dialog.dismiss();
                    movimentoControl.criarMovimento("R", caixa, valor, 0, tvObs.getText().toString().trim());
                    atualizarSaldo();
                }
            });

            dialog.show();
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

    private void atualizarSaldo() {
        txtSaldo.setText("R$ " + funcoes.formatarNumeroComVirgula(caixa.getDouble("saldo")));
    }
}
