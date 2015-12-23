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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
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
import br.com.sharkweb.fbv.model.ParseProxyObject;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class MensalidadesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView mensalidades;
    private MensalidadeListAdapter adapterMensalidade;
    private ParseObject time;
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
                //pedirEmailApelido();
                mudarTelaComRetorno(UsuariosTimeActivity.class, 1);
            }
        });

        mensalidades = (ListView) findViewById(R.id.mensalidadeslist_listviewmensalidades);
        mensalidades.setOnItemClickListener(this);

        //Setando o objeto ParseObject do time.
        this.time = Constantes.getTimeSelecionado();

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
                String usuario = data.getExtras().getString("usuario");
                if (!usuario.isEmpty()) {
                    if (Constantes.getSessao() != null && Constantes.getSessao().getId() == 1) {
                        informacoesMensalidade(Constantes.getSessao().getObjeto());
                        Constantes.setSessao(null);
                    }
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

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void atualizarLista() {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Atualizando lista de mensalidades....");
        ParseQuery query = this.time.getRelation("mensalidades").getQuery();
        query.whereEqualTo("pago", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mensalidadeList, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    adapterMensalidade = new MensalidadeListAdapter(context, mensalidadeList);
                    mensalidades.setAdapter(adapterMensalidade);
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    /*public void pedirEmailApelido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe o Nome de usuario FBV do jogador");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", input.getText().toString());
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(final ParseUser parseUser, ParseException e) {
                        FuncoesParse.dismissProgressBar(progresso);
                        if (e == null) {
                            informacoesMensalidade(parseUser);
                        } else {
                            funcoes.mostrarToast(2);
                        }
                    }
                });
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
*/
    private void informacoesMensalidade(final ParseObject usuario) {
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
            public void onClick(final View v) {

                if (input.getText().toString().isEmpty()) {
                    input.setText("0.00");
                }

                double valor = Double.valueOf(input.getText().toString());
                int diaVencimento = Integer.valueOf(txtDiaVencimento.getText().toString().trim());
                if (diaVencimento > 0) {
                    if (valor > 0) {
                        Date data;
                        if (diaVencimento < funcoes.getDate().getDate()) {
                            data = funcoes.dataPorPeriodo(funcoes.getDate(), diaVencimento, frequencia.getSelectedItemPosition());
                        } else {
                            Date ret = funcoes.getDate();
                            ret.setDate(diaVencimento);
                            data = ret;
                        }
                        dialog.dismiss();
                        criarMensalidade(usuario, data, frequencia.getSelectedItemPosition(), valor, diaVencimento);

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


    private void criarMensalidade(ParseObject usuario, Date data, int pagamento, Double valor, int diaVencimento) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando mensalidade...");

        //Criando objeto mensalidade
        final ParseObject mensalidade = new ParseObject("mensalidade");
        mensalidade.put("time", time);
        mensalidade.put("usuario", usuario);
        mensalidade.put("data", data);
        mensalidade.put("pagamento", pagamento);
        mensalidade.put("valor", valor);
        mensalidade.put("valorPago", 0);
        mensalidade.put("diaVencimento", diaVencimento);
        mensalidade.put("nomeUsuario", usuario.getString("nome"));
        mensalidade.put("pago", false);
        mensalidade.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    time.getRelation("mensalidades").add(mensalidade);
                    time.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                atualizarLista();
                                funcoes.mostrarToast(1);
                            } else {
                                funcoes.mostrarToast(2);
                            }
                        }
                    });
                } else {
                    FuncoesParse.dismissProgressBar(progresso);
                    funcoes.mostrarToast(2);
                }
            }
        });
    }

    private void criarNovaMensalidade(ParseObject mensalidade) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Criando próxima mensalidade...");

        Date novaData = mensalidade.getDate("data");
        novaData = funcoes.dataPorPeriodo(novaData, mensalidade.getInt("diaVencimento"),
                mensalidade.getInt("pagamento"));

        final ParseObject mensalidadeNova = new ParseObject("mensalidade");
        mensalidadeNova.put("valor", mensalidade.getDouble("valor"));
        mensalidadeNova.put("valorPago", 0);
        mensalidadeNova.put("pagamento", mensalidade.getInt("pagamento"));
        mensalidadeNova.put("diaVencimento", mensalidade.getInt("diaVencimento"));
        mensalidadeNova.put("data", novaData);
        mensalidadeNova.put("time", time);
        mensalidadeNova.put("usuario", mensalidade.get("usuario"));
        mensalidadeNova.put("nomeUsuario", mensalidade.getString("nomeUsuario").trim());
        mensalidadeNova.put("pago", false);
        mensalidadeNova.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    time.getRelation("mensalidades").add(mensalidadeNova);
                    time.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                funcoes.mostrarToast(1);
                                atualizarLista();
                            } else {
                                funcoes.mostrarToast(2);
                            }
                        }
                    });

                } else {
                    FuncoesParse.dismissProgressBar(progresso);
                    funcoes.mostrarToast(2);
                }
            }
        });
    }

    private void atualizarMensalidade(final ParseObject mensalidade, final Double valorMov) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando mensalidade...");
        mensalidade.put("valorPago", mensalidade.getDouble("valorPago")
                + valorMov);
        if (mensalidade.getDouble("valorPago") == mensalidade.getDouble("valor")) {
            mensalidade.put("pago", true);
        }
        mensalidade.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FuncoesParse.dismissProgressBar(progresso);
                    String historico = "Pgto. Mensalidade \n" + mensalidade.getString("nomeUsuario").trim();

                    final Dialog progresso = FuncoesParse.showProgressBar(context, "Criando movimento...");
                    final ParseObject movimento = new ParseObject("movimento");
                    movimento.put("time", time);
                    movimento.put("historico", historico);
                    movimento.put("valor", valorMov);
                    movimento.put("tipo", "M");
                    movimento.put("usuario", ParseUser.getCurrentUser());
                    movimento.saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                time.getRelation("movimentos").add(movimento);
                                time.saveInBackground(new SaveCallback() {
                                    public void done(com.parse.ParseException e) {
                                        FuncoesParse.dismissProgressBar(progresso);
                                        if (e == null) {
                                            funcoes.mostrarToast(1);
                                            //Se a mensalidade já foi paga, cria a nova.
                                            if (mensalidade.getDouble("valorPago") == mensalidade.getDouble("valor")) {
                                                criarNovaMensalidade(mensalidade);
                                            } else {
                                                atualizarLista();
                                            }
                                        } else {
                                            funcoes.mostrarToast(2);
                                        }
                                    }
                                });

                            } else {
                                FuncoesParse.dismissProgressBar(progresso);
                                funcoes.mostrarToast(2);
                            }
                        }
                    });
                } else {
                    funcoes.mostrarToast(2);
                }
            }
        });
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
        final ParseObject mensalidade = adapterMensalidade.getItem(position);

        if (!mensalidade.getObjectId().isEmpty() && mensalidade.getDouble("valor") != mensalidade.getDouble("valorPago")) {
            double valorFaltante = mensalidade.getDouble("valor") - mensalidade.getDouble("valorPago");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Informe o Valor pago:");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);

            input.setText(String.valueOf(funcoes.formatarNumeroComVirgula(valorFaltante)).replace(",", "."));
            builder.setView(input);
            input.setHint("Valor:");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    atualizarMensalidade(mensalidade, Double.valueOf(input.getText().toString()));
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
