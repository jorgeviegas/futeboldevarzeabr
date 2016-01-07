package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.MovimentoListAdapter;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.adapter.TimeListAdapterParse;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.ParseProxyObject;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class MovimentosActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView movimentos;
    private Spinner spnFiltro;
    private MovimentoListAdapter adapterMovimentos;
    private ParseObject time;
    private Funcoes funcoes;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentos);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        movimentos = (ListView) findViewById(R.id.movimentoslist_listviewmovimentos);
        movimentos.setOnItemClickListener(this);
        movimentos.setOnItemLongClickListener(this);
        funcoes = new Funcoes(this);

        spnFiltro = (Spinner) findViewById(R.id.movimentos_tipofiltro);
        ArrayList<String> opcoes = new ArrayList<>();
        opcoes.add("Últimos 15 dias");
        opcoes.add("Últimos 30 dias");
        opcoes.add("Últimos 45 dias");
        //opcoes.add("Mes anterior");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                opcoes);

        spnFiltro.setAdapter(arrayAdapter2);
        //spnFiltro.setVisibility(View.GONE);
        spnFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Date data = funcoes.getDate();
                if (position == 0) {
                    data.setDate(data.getDate() - 15);
                } else if (position == 1) {
                    data.setDate(data.getDate() - 30);
                } else if (position == 2) {
                    data.setDate(data.getDate() - 45);
                    //data = funcoes.getFirstDayOfTheMonth(funcoes.getDate());
                }
                atualizarLista(data);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        this.time = Constantes.getTimeSelecionado();
        movimentos.setCacheColorHint(Color.TRANSPARENT);
        if (!FuncoesParse.isAdmin()) {
            Toast toast = Toast.makeText(context, "Somente usuários com permissão de Administrador podem visualizar.", Toast.LENGTH_LONG);
            toast.show();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_team, menu);
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

    public void atualizarLista(Date dataFiltro) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Carregando....");
        ParseQuery query = this.time.getRelation("movimentos").getQuery();
        query.whereGreaterThanOrEqualTo("createdAt", dataFiltro);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> movimentoList, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    if (movimentoList.size() == 0) {
                        //funcoes.mostrarDialogAlert(1, "Não há movimentações até o momento.");
                    }
                    adapterMovimentos = new MovimentoListAdapter(context, movimentoList);
                    movimentos.setAdapter(adapterMovimentos);
                } else {
                    funcoes.mostrarToast(4);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ParseObject movimento = adapterMovimentos.getItem(position);
        if (!movimento.getObjectId().isEmpty()) {

            final double valorMov = movimento.getDouble("valor");
            final String tipo = movimento.getString("tipo");

            String[] arrayOpcoes = new String[1];
            arrayOpcoes[0] = "Excluir movimento";
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.questionmark_64);
            builder.setTitle("O que deseja fazer?");
            builder.setCancelable(true);
            builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    switch (arg1) {
                        case 0:
                            if (!tipo.equals("M")) {
                                final Dialog progresso = FuncoesParse.showProgressBar(context, "Excluindo movimento...");
                                movimento.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            if (tipo.equals("E")) {
                                                time.put("valorEmCaixa", time.getDouble("valorEmCaixa") - valorMov);
                                            } else if (tipo.equals("R")) {
                                                time.put("valorEmCaixa", time.getDouble("valorEmCaixa") + valorMov);
                                            }
                                            time.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    FuncoesParse.dismissProgressBar(progresso);
                                                    if (e == null) {
                                                        funcoes.mostrarToast(1);
                                                        Date data = funcoes.getDate();
                                                        data.setDate(data.getDate() - 15);
                                                        spnFiltro.setSelection(0);
                                                        atualizarLista(data);
                                                    } else {
                                                        time.saveEventually();
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
                                funcoes.mostrarDialogAlert(1, "Não é possível excluir movimentos referentes à pagamento de mensalidade.\n" +
                                        "Por favor, ajuste a mensalidade manualmente.");
                            }
                            break;

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

        }
        return true;
    }
}
