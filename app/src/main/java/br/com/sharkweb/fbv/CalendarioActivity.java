package br.com.sharkweb.fbv;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.JogoListAdapter;
import br.com.sharkweb.fbv.controller.JogoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Time;

import android.widget.CalendarView.OnDateChangeListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import static android.graphics.Color.WHITE;

public class CalendarioActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView jogos;
    private CalendarView calendario;
    private Date dataSelecionada;
    private Button btnHoje;
    private Button btnNovoJogo;
    private ParseObject time;
    private ListView listaDeJogos;
    private List<ParseObject> listaDeJogosDoTime;
    private JogoListAdapter adapterJogos;

    final Context context = this;
    private Funcoes funcoes = new Funcoes(this);
    private TimeUsuarioController timeUsuarioControl = new TimeUsuarioController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        jogos = (ListView) findViewById(R.id.calendario_listaJogos);
        this.time = Constantes.getTimeSelecionado();

        listaDeJogos = (ListView) findViewById(R.id.calendario_listaJogos);
        listaDeJogos.setBackgroundColor(Color.WHITE);
        listaDeJogos.setOnItemClickListener(this);


        btnHoje = (Button) findViewById(R.id.calendario_botaoHoje);
        btnHoje.setVisibility(View.VISIBLE);
        btnHoje.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(View v) {
                //String datas = "08/09/2015";
                long data = funcoes.StringDataParaLong(funcoes.getDataDia());
                //long data = funcoes.StringDataParaLong(datas);
                calendario.setDate(data);
            }
        });

        btnNovoJogo = (Button) findViewById(R.id.calendario_botaoNovoJogo);
        btnNovoJogo.setVisibility(View.VISIBLE);
        btnNovoJogo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FuncoesParse.isAdmin()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Pergunta");
                    builder.setIcon(R.drawable.questionmark_64);
                    builder.setMessage("Tem certeza que deseja criar um novo jogo?");

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Bundle parametros = new Bundle();
                            //parametros.putInt("id_time", time.getId());
                            parametros.putString("data", funcoes.transformarDataEmString(dataSelecionada));
                            parametros.putString("tipoAcesso", "write");
                            mudarTelaComRetorno(CadastroJogoActivity.class, parametros, 1);
                        }
                    });
                    builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    funcoes.mostrarDialogAlert(1, "Somente usuários administradores podem criar jogos!");
                }
            }
        });

        initializeCalendar();
        buscarJogos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                atualizarLista();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void buscarJogos() {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Carregando...");

        this.time.getRelation("jogos").getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listJogos, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    listaDeJogosDoTime = listJogos;
                } else {
                    funcoes.mostrarToast(4);
                    listaDeJogosDoTime = null;
                }
                atualizarLista();
            }
        });
    }

    public void atualizarLista() {
        List<ParseObject> lista = null;
        for (int i = 0; i < listaDeJogosDoTime.size(); i++) {
            if (listaDeJogosDoTime.get(i).getDate("data").equals(funcoes.getDate())) {
                lista.add(listaDeJogosDoTime.get(i));
            }
        }
        if (lista != null) {
            adapterJogos = new JogoListAdapter(this, lista);
            listaDeJogos.setAdapter(adapterJogos);
        }
    }

    @SuppressLint("NewApi")
    public void initializeCalendar() {
        calendario = (CalendarView) findViewById(R.id.calendario_calendarioView);
        calendario.setShowWeekNumber(false);
        calendario.setFirstDayOfWeek(2);
        calendario.setBackgroundColor(WHITE);

        //sets the listener to be notified upon selected date change.
        calendario.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month = month + 1;
                String data = day + "/" + month + "/" + year;
                try {
                    dataSelecionada = funcoes.transformarStringEmData(data);
                    atualizarLista();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            dataSelecionada = funcoes.transformarStringEmData(funcoes.getDataDia());
            calendario.setDate(funcoes.StringDataParaLong(funcoes.getDataDia()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendario, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
        if (id == R.id.action_settings) {
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

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, Bundle parametros, int key) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivityForResult(intent, key);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject jogo = adapterJogos.getItem(position);
        if (jogo != null && !jogo.getObjectId().isEmpty()) {
            String tipoAcesso = "read";
            //Somente usuarios administradores podem usar o menu cadastrar
            if (timeUsuarioControl.isAdmin(Constantes.getUsuarioLogado().getId(), 0)) {
                tipoAcesso = "edit";
            }
            Bundle parametros = new Bundle();
            parametros.putString("tipoAcesso", tipoAcesso);
            //parametros.putInt("id_jogo", jogo.getId());
            mudarTelaComRetorno(CadastroJogoActivity.class, parametros, 1);
        }
    }
}
