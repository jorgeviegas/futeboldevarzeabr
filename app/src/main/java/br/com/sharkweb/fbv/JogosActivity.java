package br.com.sharkweb.fbv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.JogoListAdapter;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.model.Sessao;

public class JogosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView jogos;
    private JogoListAdapter jogoListAdapter;
    final Context context = this;
    private static ProgressBar progressBar;
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        jogos = (ListView) findViewById(R.id.jogos_listview);
        jogos.setOnItemClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.jogos_progressBar);
        progressBar.setVisibility(View.GONE);
        buscarJogos();
    }

    private void buscarJogos() {
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery query = Constantes.timeSelecionado.getRelation("jogos").getQuery();
        query.orderByAscending("data");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    jogoListAdapter = new JogoListAdapter(context, list);
                    jogos.setAdapter(jogoListAdapter);
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_calendario, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject jogo = jogoListAdapter.getItem(position);
        if (jogo != null && !jogo.getObjectId().isEmpty()) {
            String tipoAcesso = "read";
            if (FuncoesParse.isAdmin()) {
                tipoAcesso = "edit";
            }
            Bundle parametros = new Bundle();
            parametros.putString("tipoAcesso", tipoAcesso);
            Sessao sessao = new Sessao(2, jogo, "jogo");
            Constantes.setSessao(sessao);
            mudarTela(CadastroJogoActivity.class, parametros);
        }
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivity(intent);
    }
}
