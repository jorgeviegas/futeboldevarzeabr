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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

public class MovimentosActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView movimentos;
    private Spinner spnFiltro;
    private MovimentoListAdapter adapterMovimentos;
    private ParseObject time;
    private Funcoes funcoes = new Funcoes();
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentos);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        movimentos = (ListView) findViewById(R.id.movimentoslist_listviewmovimentos);
        movimentos.setOnItemClickListener(this);

        spnFiltro = (Spinner) findViewById(R.id.movimentos_tipofiltro);
        ArrayList<String> opcoes = new ArrayList<>();
        opcoes.add("Últimos 15 dias");
        opcoes.add("Últimos 30 dias");
        opcoes.add("Últimos 45 dias");
        opcoes.add("Mes anterior");
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

        //atualizarLista(null);
        movimentos.setCacheColorHint(Color.TRANSPARENT);
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
        ParseObject movimento = adapterMovimentos.getItem(position);
        if (!movimento.getObjectId().isEmpty()) {

        }
    }
}
