package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.adapter.MensalidadeListAdapter;
import br.com.sharkweb.fbv.adapter.MovimentoListAdapter;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MensalidadeController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class MensalidadesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView mensalidades;
    private ArrayList<Mensalidade> listaMensalidades;
    private MensalidadeListAdapter adapterMensalidade;
    private MensalidadeController mensalidadeControl = new MensalidadeController(this);
    private MovimentoController movimentoControl = new MovimentoController(this);
    private CaixaController caixaControl = new CaixaController(this);
    private TimeController timeControl = new TimeController(this);
    private Usuario user;
    private Time time;
    private Caixa caixa;
    private Funcoes funcoes = new Funcoes(this);
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensalidades);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mensalidades = (ListView) findViewById(R.id.mensalidadeslist_listviewmensalidades);
        mensalidades.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            caixa = caixaControl.selectCaixaPorId(params.getInt("id_caixa")).get(0);
            time = timeControl.selectTimePorId(caixa.getId_time()).get(0);
        } else {
            caixa = null;
            time = null;
        }

        atualizarLista();
        mensalidades.setCacheColorHint(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                Integer id_usuario = data.getExtras().getInt("id_usuario");
                if (id_usuario != null && id_usuario > 0) {
                    inserirUsuario(id_usuario);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mensalidade, menu);
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
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_cadastrar_mensalidade) {
            mudarTelaComRetorno(UsuariosActivity.class, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void atualizarLista() {
    /*    if (caixa!=null){
            listaMovimentos = movimentosControl.selectMovimentosPorIdCaixa(caixa.getId());
        }else {
            listaMovimentos = movimentosControl.selectMovimentos();
        }*/
        // Mensalidade mens = new Mensalidade(1,1,funcoes.getDataDia(),0,200.00,100.00);
        // mensalidadeControl.inserir(mens);

        listaMensalidades = mensalidadeControl.selectMensalidadesPorIdTime(this.time.getId());

        if (listaMensalidades.size() == 0) {
            ArrayList<Mensalidade> listaVazia = new ArrayList<Mensalidade>();
            listaVazia.add(new Mensalidade(0, 0, "", 0, 0, 0));
            adapterMensalidade = new MensalidadeListAdapter(this, listaVazia);
        } else
            adapterMensalidade = new MensalidadeListAdapter(this, listaMensalidades);
        mensalidades.setAdapter(adapterMensalidade);
    }

    private void inserirUsuario(final int id_usuario) {
        //int id_time, int id_usuario, String data, int pagamento, double valor, double valor_pago

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Informe o valor da Mensalidade:");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        builder.setView(input);
        input.setText("0.00");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    input.setText("0.00");
                }
                double valor = Double.valueOf(input.getText().toString());
                if (valor > 0) {
                    Mensalidade mensalidade = new Mensalidade(time.getId(), id_usuario, funcoes.getDataDia(),
                            1, valor, 0);
                    mensalidadeControl.inserir(mensalidade);
                    atualizarLista();
                } else {
                    funcoes.mostrarDialogAlert(1, "O valor não pode ser menor ou igual à zero.");
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final Mensalidade mensalidade = adapterMensalidade.getItem(position);

        if (mensalidade.getId() != 0 && mensalidade.getValor() != mensalidade.getValor_pago()) {
            double valorFaltante = mensalidade.getValor() - mensalidade.getValor_pago();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Informe o Valor pago:");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);

            input.setText(String.valueOf(valorFaltante));
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    double valorMov = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("M", caixa, valorMov);

                    mensalidade.setValor_pago(mensalidade.getValor_pago() +
                            Double.valueOf(input.getText().toString()));
                    mensalidadeControl.alterar(mensalidade);
                    atualizarLista();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }
}
