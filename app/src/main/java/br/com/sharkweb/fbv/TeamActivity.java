package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.parse.ParseUser;

import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.TimeListAdapterParse;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Sessao;

public class TeamActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView times;
    private TimeListAdapterParse adapterTimes;
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);
    private boolean esperaRetorno;
    private ParseObject timeSelecionado;
    private boolean podeCadastrar = true;
    private boolean buscarTodosTimes = false;
    final Context context = this;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        times = (ListView) findViewById(R.id.timelist_listviewTimes);
        times.setOnItemClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.timelist_progressBar);
        progressBar.setVisibility(View.GONE);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            esperaRetorno = params.getBoolean("esperaRetorno");
            podeCadastrar = params.getBoolean("cadastrar");
            buscarTodosTimes = params.getBoolean("buscarTodosTimes");
        }

        //Agora essa tela sempre retorna
        esperaRetorno = true;
        buscarTimes();
        times.setCacheColorHint(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        if (this.esperaRetorno) {
            if (timeSelecionado == null) {
                Constantes.setTimeSelecionado(null);
            }
            Intent it = new Intent();
            setResult(1, it);
        }
        super.onBackPressed();
    }

    private void buscarTimes() {
        // final Dialog progresso = FuncoesParse.showProgressBar(context, "Carregando....");
        progressBar.setVisibility(View.VISIBLE);
        //Pega a relação "times" que está dentro da tabela user e então traz os times que estão nessa relação.
        // O comando getQuery retorna um objeto Query que é usado para fazer consultas. Como eu não quero adicionar
        //nenuhum outro filtro adicional, chamei o findInBackgroud direto.
        if (buscarTodosTimes) {
            ParseQuery queryTimes = new ParseQuery("time");
            queryTimes.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    progressBar.setVisibility(View.GONE);
                    if (e == null) {
                        //FuncoesParse.dismissProgressBar(progresso);
                        adapterTimes = new TimeListAdapterParse(context, list);
                        times.setAdapter(adapterTimes);
                    }
                }
            });
        } else {
            ParseUser.getCurrentUser().getRelation("times").getQuery()
                    .findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> timeList, ParseException e) {
                            //FuncoesParse.dismissProgressBar(progresso);
                            progressBar.setVisibility(View.GONE);
                            if (e == null) {
                                adapterTimes = new TimeListAdapterParse(context, timeList);
                                times.setAdapter(adapterTimes);
                            } else {

                            }
                        }
                    });
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.time_action_cadastrar);

        //Somente usuarios administradores podem usar o menu cadastrar
        if (tipouserControl.selectTiposUsuariosPorId(ParseUser.getCurrentUser().getInt("id_tipo")).
                get(0).getTipo().equals("Administrador") && this.podeCadastrar)
            m1.setVisible(true);
        else
            m1.setVisible(false);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team, menu);
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

        if (item.getItemId() == R.id.time_action_cadastrar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setIcon(R.drawable.questionmark_64);
            builder.setMessage("Tem certeza que deseja cadastrar um novo Time?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Bundle parametros = new Bundle();
                    parametros.putString("tipoAcesso", "write");
                    mudarTela(CadastroTimeActivity.class, parametros);
                }

            });
            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
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

    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject time = adapterTimes.getItem(position);
        //Setando o time selecionado na pesquisa.
        if (!buscarTodosTimes) {
            Constantes.setTimeSelecionado(time);
        } else {
            Sessao sessao = new Sessao(3, time, "time");
            Constantes.setSessao(sessao);
        }
        if (!time.getObjectId().isEmpty()) {
            if (esperaRetorno) {
                this.timeSelecionado = time;
                onBackPressed();
            }
        }
    }
}
