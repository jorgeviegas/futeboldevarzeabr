package br.com.sharkweb.fbv;

import android.app.AlertDialog;
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

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.Time;

/**
 * Created by Jorge on 30/09/2015.
 */
public class FinanceiroActivity extends AppCompatActivity {

    final Context context = this;
    private Funcoes funcoes = new Funcoes(this);
    private MovimentoController movimentoControl = new MovimentoController(this);
    private TimeController timeControl = new TimeController(this);
    private CaixaController caixaControl = new CaixaController(this);
    private double valor = 0;
    private Caixa caixa;
    private Time time;

    private TextView txtSaldo;
    private Button btnMovimentos;
    private Button btnMensalidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        btnMovimentos = (Button) findViewById(R.id.financeiro_movimentos);
        btnMensalidades = (Button) findViewById(R.id.financeiro_mensalidades);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            time = timeControl.selectTimePorId(params.getInt("id_time")).get(0);
        } else {
            time = null;
        }

        carregarRegistro();
        atualizarSaldo();

        btnMovimentos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle parametros = new Bundle();
                parametros.putInt("id_caixa", caixa.getId());
                mudarTela(MovimentosActivity.class, parametros);
            }
        });

        btnMensalidades.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle parametros = new Bundle();
                parametros.putInt("id_caixa", caixa.getId());
                mudarTela(MensalidadesActivity.class, parametros);
            }
        });
    }

    private void carregarRegistro() {
        ArrayList<Caixa> caixa = caixaControl.selectJogosPorIdTime(time.getId());
        if (caixa.size() > 0) {
            this.caixa = caixa.get(0);
        } else {
            Caixa caixa2 = new Caixa(time.getId(), 0, 0);
            Long ret = caixaControl.inserir(caixa2);
            if (ret > 0) {
                caixa2.setId(Integer.valueOf(ret.toString()));
                this.caixa = caixa2;
            } else {
                funcoes.mostrarDialogAlert(3, "");
                return;
            }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cadastrar_entrada) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Nova Entrada");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valor = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("E", caixa, valor);
                    atualizarSaldo();

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }
        if (id == R.id.action_cadastrar_retirada) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Nova Retirada");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valor = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("R", caixa, valor);
                    atualizarSaldo();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
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
        txtSaldo.setText("Saldo:\n R$" + funcoes.formatarNumeroComVirgula(caixa.getSaldo()));
    }
}
