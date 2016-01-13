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
import android.widget.Toast;

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
    private ParseObject time;

    private TextView txtSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        this.time = Constantes.getTimeSelecionado();
        if (FuncoesParse.isAdmin()) {
            atualizarSaldo();
        } else {
            Toast toast = Toast.makeText(context, "Somente usuários com permissão de Administrador podem visualizar.", Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }
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
                    movimentoControl.criarMovimento("E", time, valor, "", tvObs.getText().toString().trim());
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
                    movimentoControl.criarMovimento("R", time, valor, "", tvObs.getText().toString().trim());
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
        txtSaldo.setText("R$ " + funcoes.formatarNumeroComVirgula(this.time.getDouble("valorEmCaixa")));
    }
}
