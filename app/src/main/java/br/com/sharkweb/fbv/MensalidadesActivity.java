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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
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

public class MensalidadesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mensalidades;
    private CheckBox chkSomenteEmAberto;
    private TextView txtFiltrarPorUusario;
    private MensalidadeListAdapter adapterMensalidade;
    private ParseObject time;
    private Funcoes funcoes = new Funcoes(this);
    private MovimentoController movimentoControl = new MovimentoController(this);
    final Context context = this;
    private static ProgressBar progressBar;

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
                if (funcoes.verificaConexao(context)) {
                    mudarTelaComRetorno(UsuariosTimeActivity.class, 1);
                } else {
                    funcoes.mostrarDialogAlert(1, "Sem conexão com internet!");
                }
            }
        });

        mensalidades = (ListView) findViewById(R.id.mensalidadeslist_listviewmensalidades);
        mensalidades.setOnItemClickListener(this);
        mensalidades.setOnItemLongClickListener(this);

        chkSomenteEmAberto = (CheckBox) findViewById(R.id.mensalidadeslist_somenteAbertos);
        chkSomenteEmAberto.setChecked(true);
        chkSomenteEmAberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarLista(null);
            }
        });

        txtFiltrarPorUusario = (TextView) findViewById(R.id.mensalidadeslist_filtrarPorUsuario);
        txtFiltrarPorUusario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarTelaComRetorno(UsuariosTimeActivity.class, 2);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.mensalidades_progressBar);
        progressBar.setVisibility(View.GONE);

        //Setando o objeto ParseObject do time.
        this.time = Constantes.getTimeSelecionado();

        if (FuncoesParse.isAdmin()) {
            atualizarLista(null);
        } else {
            Toast toast = Toast.makeText(context, "Somente usuários com permissão de Administrador podem visualizar.", Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }
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
                        informacoesMensalidade(Constantes.getSessao().getObjeto(), null);
                        Constantes.setSessao(null);
                    }
                }
                break;
            case 2:
                if (Constantes.getSessao() != null && Constantes.getSessao().getId() == 1) {
                    atualizarLista(Constantes.getSessao().getObjeto());
                    Constantes.setSessao(null);
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

    public void atualizarLista(ParseObject usuarioFiltrado) {
        //final Dialog progresso = FuncoesParse.showProgressBar(context, "Atualizando lista de mensalidades....");
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery query = this.time.getRelation("mensalidades").getQuery();
        if (chkSomenteEmAberto.isChecked()) {
            query.whereEqualTo("pago", false);
        }
        if (usuarioFiltrado != null) {
            query.whereEqualTo("usuario", usuarioFiltrado);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> mensalidadeList, ParseException e) {
                progressBar.setVisibility(View.GONE);
                //FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    adapterMensalidade = new MensalidadeListAdapter(context, mensalidadeList);
                    mensalidades.setAdapter(adapterMensalidade);
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    private void informacoesMensalidade(final ParseObject usuario, final ParseObject mensalidade) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_mensalidade);

        dialog.setTitle("Dados da mensalidade:");
        final EditText txtDiaVencimento = (EditText) dialog.findViewById(R.id.dialog_mensalidade_data);
        final TextView input = (TextView) dialog.findViewById(R.id.dialog_mensalidade_valor);
        final TextView valorPago = (TextView) dialog.findViewById(R.id.dialog_mensalidade_valor_pago);
        input.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        valorPago.setInputType(InputType.TYPE_CLASS_NUMBER |
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

        if (mensalidade != null) {
            input.setText(funcoes.formatarNumeroComPonto(mensalidade.getDouble("valor")));
            valorPago.setText(funcoes.formatarNumeroComPonto(mensalidade.getDouble("valorPago")));
            frequencia.setSelection(mensalidade.getInt("frequencia"));
            txtDiaVencimento.setText(String.valueOf(mensalidade.getInt("diaVencimento")));
        }

        final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_mensalidade_btnConfirmar);
        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if (input.getText().toString().isEmpty()) {
                    input.setText("0.00");
                }
                if (valorPago.getText().toString().isEmpty()) {
                    valorPago.setText("0.00");
                }

                double valor = Double.valueOf(input.getText().toString());
                double nvalorPago = Double.valueOf(valorPago.getText().toString());
                int diaVencimento = Integer.valueOf(txtDiaVencimento.getText().toString().trim());
                if (diaVencimento > 0) {
                    if (valor > 0) {
                        dialog.dismiss();
                        if (mensalidade == null) {
                            criarMensalidade(usuario, frequencia.getSelectedItemPosition(), valor, nvalorPago, diaVencimento);
                        } else {
                            atualizarObjetoMensalidade(mensalidade, valor, nvalorPago, diaVencimento, frequencia.getSelectedItemPosition());
                        }
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

    private void atualizarObjetoMensalidade(final ParseObject mensalidade, final double valor, final double valorPago, int diaVencimento, int pagamento) {
        final double valorPagoAntigo = mensalidade.getDouble("valorPago");
        final double valorDiferenca = valorPago - valorPagoAntigo;
        mensalidade.put("valor", valor);
        mensalidade.put("valorPago", valorPago);
        mensalidade.put("diaVencimento", diaVencimento);
        mensalidade.put("pagamento", pagamento);
        if (valor == valorPago) {
            mensalidade.put("pago", true);
        }
        mensalidade.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    if (valor == valorPago) {
                        final Dialog progresso = FuncoesParse.showProgressBar(context, "Criando movimento...");
                        String historico = "Pgto. Mensalidade \n" + mensalidade.getString("nomeUsuario").trim();
                        final ParseObject movimento = new ParseObject("movimento");
                        movimento.put("time", time);
                        movimento.put("historico", historico);
                        movimento.put("valor", valorDiferenca);
                        movimento.put("tipo", "M");
                        movimento.put("usuario", ParseUser.getCurrentUser());
                        movimento.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                FuncoesParse.dismissProgressBar(progresso);
                                if (e == null) {
                                    criarProximaMensalidade(mensalidade);
                                } else {
                                    funcoes.mostrarToast(2);
                                }
                            }
                        });
                    } else if (valorPago != valorPagoAntigo) {
                        movimentoControl.criarMovimento("M", time, valorDiferenca, mensalidade.getString("nomeUsuario"), "");
                    } else {
                        funcoes.mostrarToast(1);
                    }
                    atualizarLista(null);
                } else {
                    funcoes.mostrarToast(2);
                }
            }
        });
    }

    private void criarMensalidade(final ParseObject usuario, int pagamento, Double valor, Double valorPago, int diaVencimento) {

        Date data;
        if (diaVencimento < funcoes.getDate().getDate()) {
            data = funcoes.dataPorPeriodo(funcoes.getDate(), diaVencimento, pagamento);
        } else {
            Date ret = funcoes.getDate();
            ret.setDate(diaVencimento);
            data = ret;
        }

        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando mensalidade...");

        //Criando objeto mensalidade
        final ParseObject mensalidade = new ParseObject("mensalidade");
        mensalidade.put("time", time);
        mensalidade.put("usuario", usuario);
        mensalidade.put("data", data);
        mensalidade.put("pagamento", pagamento);
        mensalidade.put("valor", valor);
        mensalidade.put("valorPago", valorPago);
        mensalidade.put("diaVencimento", diaVencimento);
        mensalidade.put("nomeUsuario", usuario.getString("nome"));
        if (valorPago >= valor) {
            mensalidade.put("pago", true);
        } else {
            mensalidade.put("pago", false);
        }
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
                                if (mensalidade.getDouble("valorPago") > 0) {
                                    movimentoControl.criarMovimento("M", time, mensalidade.getDouble("valorPago"),
                                            usuario.getString("nome").trim(), "");
                                }
                                atualizarLista(null);
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

    private void criarProximaMensalidade(ParseObject mensalidade) {
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
                                atualizarLista(null);
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
                                time.put("valorEmCaixa", time.getDouble("valorEmCaixa") + valorMov);
                                time.saveInBackground(new SaveCallback() {
                                    public void done(com.parse.ParseException e) {
                                        FuncoesParse.dismissProgressBar(progresso);
                                        if (e == null) {
                                            funcoes.mostrarToast(1);
                                            //Se a mensalidade já foi paga, cria a nova.
                                            if (mensalidade.getDouble("valorPago") == mensalidade.getDouble("valor")) {
                                                criarProximaMensalidade(mensalidade);
                                            } else {
                                                atualizarLista(null);
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

    private void verificarAntesDeDeletar(final ParseObject mensalidade) {
        if (mensalidade.getDouble("valorPago") > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.questionmark_64);
            builder.setTitle("Atenção!");
            builder.setCancelable(false);
            final double valorPago = mensalidade.getDouble("valorPago");
            builder.setMessage("Essa Mensalidade possui valor pago.\n" +
                    "Deseja abater este valor do Caixa?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final Dialog progresso = FuncoesParse.showProgressBar(context, "Excluindo Mensalidade...");
                    mensalidade.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                movimentoControl.criarMovimento("A", time, (valorPago * -1), mensalidade.getString("nomeUsuario"), "");
                                funcoes.mostrarToast(6);
                                atualizarLista(null);
                            } else {
                                FuncoesParse.dismissProgressBar(progresso);
                                funcoes.mostrarToast(5);
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    deletarMensalidade(mensalidade);
                }
            });
            builder.create().show();
        } else {
            deletarMensalidade(mensalidade);
        }
    }

    private void deletarMensalidade(ParseObject mensalidade) {
        mensalidade.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    funcoes.mostrarToast(6);
                    atualizarLista(null);
                } else {
                    funcoes.mostrarToast(5);
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ParseObject mensalidade = adapterMensalidade.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.questionmark_64);
        builder.setTitle("O que deseja fazer?");
        String[] arrayOpcoes = new String[2];
        arrayOpcoes[0] = "Editar Mensalidade";
        arrayOpcoes[1] = "Excluir Mensalidade";
        builder.setCancelable(true);
        builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                if (funcoes.verificaConexao(context)) {
                    switch (arg1) {
                        case 0:
                            informacoesMensalidade(null, mensalidade);
                            break;
                        case 1:
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.questionmark_64);
                            builder.setTitle("Atenção!");
                            builder.setCancelable(false);
                            builder.setMessage("Tem certeza que deseja excluir?");
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    verificarAntesDeDeletar(mensalidade);
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.create().show();
                            break;
                    }
                } else {
                    funcoes.mostrarDialogAlert(1, "Sem conexão com internet!");
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        AlertDialog dialogExportar = builder.create();
        dialogExportar.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if (funcoes.verificaConexao(context)) {
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
        } else {
            funcoes.mostrarDialogAlert(1, "Sem conexão com internet!");
        }
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }


}
