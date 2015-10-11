package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.adapter.MensalidadeListAdapter;
import br.com.sharkweb.fbv.adapter.MovimentoListAdapter;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MensalidadeController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Jogo;
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mensalidade_botaoflutuante);
        fab.setColorFilter(Color.WHITE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarTelaComRetorno(UsuariosActivity.class, 1);
            }
        });

        mensalidades = (ListView) findViewById(R.id.mensalidadeslist_listviewmensalidades);
        mensalidades.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            time = timeControl.selectTimePorId(params.getInt("id_time")).get(0);
        } else {
            caixa = null;
            time = null;
        }

        carregarRegistro();
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
       // getMenuInflater().inflate(R.menu.menu_mensalidade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_cadastrar_mensalidade) {
            mudarTelaComRetorno(UsuariosActivity.class, 1);
        }

        return super.onOptionsItemSelected(item);
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

    public void atualizarLista() {

        listaMensalidades = mensalidadeControl.selectMensalidadesPorIdTime(this.time.getId());

        if (listaMensalidades.size() == 0) {
            ArrayList<Mensalidade> listaVazia = new ArrayList<Mensalidade>();
            listaVazia.add(new Mensalidade(0, 0, "", 0, 0, 0, 0));
            adapterMensalidade = new MensalidadeListAdapter(this, listaVazia);
        } else
            adapterMensalidade = new MensalidadeListAdapter(this, listaMensalidades);
        mensalidades.setAdapter(adapterMensalidade);
    }

    private void inserirUsuario(final int id_usuario) {
        //int id_time, int id_usuario, String data, int pagamento, double valor, double valor_pago

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_mensalidade);

        dialog.setTitle("Dados da mensalidade:");
        final EditText txtDiaVencimento = (EditText) dialog.findViewById(R.id.dialog_mensalidade_data);
        final TextView input = (TextView) dialog.findViewById(R.id.dialog_mensalidade_valor);
        input.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        txtDiaVencimento.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        final Spinner frequencia = (Spinner) dialog.findViewById(R.id.dialog_mensalidade_frequencia);
        ArrayList<String> opcoes = new ArrayList<>();
        opcoes.add("Mensal");
        opcoes.add("Trimestral");
        opcoes.add("Semestral");
        opcoes.add("Anual");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                opcoes);

        frequencia.setAdapter(arrayAdapter2);

        final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_mensalidade_btnConfirmar);
        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (input.getText().toString().isEmpty()) {
                    input.setText("0.00");
                }

                double valor = Double.valueOf(input.getText().toString());
                int diaVencimento = Integer.valueOf(txtDiaVencimento.getText().toString().trim());
                if (diaVencimento > 0) {
                    if (valor > 0) {
                        String data = "";
                        if (diaVencimento < funcoes.getDate().getDate()) {
                            data = funcoes.transformarDataEmString(
                                    funcoes.dataPorPeriodo(funcoes.getDate(), diaVencimento, frequencia.getSelectedItemPosition())).trim();
                        } else {
                            Date ret = funcoes.getDate();
                            ret.setDate(diaVencimento);
                            data = funcoes.transformarDataEmString(ret);
                        }

                        Mensalidade mensalidade = new Mensalidade(time.getId(), id_usuario, data,
                                frequencia.getSelectedItemPosition(), valor, 0, diaVencimento);
                        mensalidadeControl.inserir(mensalidade);
                        atualizarLista();
                        dialog.dismiss();
                    } else {
                        funcoes.mostrarDialogAlert(1, "O valor não pode ser menor ou igual à zero.");
                    }
                } else {
                    funcoes.mostrarDialogAlert(1, "Deve ser informado o dia de vecimento");
                }
            }
        });

        dialog.show();
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

            input.setText(String.valueOf(funcoes.formatarNumeroComVirgula(valorFaltante)).replace(",","."));
            builder.setView(input);
            input.setHint("Valor:");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    double valorMov = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("M", caixa, valorMov, mensalidade.getId_usuario());

                    mensalidade.setValor_pago(mensalidade.getValor_pago() +
                            Double.valueOf(input.getText().toString()));

                    //Ao pagar a mensalidade, atualiza a data da próxima e zera o valor pago.
                    if (mensalidade.getValor_pago() == mensalidade.getValor()) {
                        try {
                            Date novaData = funcoes.transformarStringEmData(mensalidade.getData());
                            novaData = funcoes.dataPorPeriodo(novaData, mensalidade.getDiaVencimento(),
                                    mensalidade.getPagamento());
                            mensalidade.setData(funcoes.transformarDataEmString(novaData));
                            mensalidade.setValor_pago(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
