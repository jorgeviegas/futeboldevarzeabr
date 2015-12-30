package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.UsuarioPosJogoListAdapter;
import br.com.sharkweb.fbv.controller.JogoController;
import br.com.sharkweb.fbv.controller.PosJogoController;
import br.com.sharkweb.fbv.controller.PosJogoUsuariosController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;

public class PosJogoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView usuarios;
    private ParseObject jogo;
    private UsuarioPosJogoListAdapter adapterUsuarios;
    private Button btnCancelar;
    private Button btnSalvar;
    private TextView tvNomeTime1;
    private TextView tvNomeTime2;
    private TextView tvVersus;
    private List<ParseObject> listaDePosJogo;
    final Context context = this;

    private PosJogoController posJogoControl = new PosJogoController(this);
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_jogo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        usuarios = (ListView) findViewById(R.id.pos_jogo_usuarioslist);
        usuarios.setOnItemClickListener(this);


        if (Constantes.getSessao() != null) {
            this.jogo = Constantes.getSessao().getObjeto();
            Constantes.setSessao(null);
        } else {
            this.jogo = null;
        }
        tvNomeTime1 = (TextView) findViewById(R.id.pos_jogo_nometime1);
        tvNomeTime1.setVisibility(View.VISIBLE);
        tvNomeTime1.setText("TM1");

        tvNomeTime2 = (TextView) findViewById(R.id.pos_jogo_nometime2);
        tvNomeTime2.setVisibility(View.VISIBLE);
        tvNomeTime2.setText("TM2");

        tvVersus = (TextView) findViewById(R.id.pos_jogo_versus);
        tvVersus.setVisibility(View.VISIBLE);
        tvVersus.setText("0 X 0");
        tvVersus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                posJogoControl.exibirPlacarJogo(jogo, context, tvVersus);

            }
        });
        btnSalvar = (Button) findViewById(R.id.pos_jogo_btncadastrar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.pos_jogo_btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        carregarRegistro();
        atualizarLista();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                ParseObject posJogo = new ParseObject("posJogoUser");
                posJogo.put("jogo", jogo);
                posJogo.put("usuario", Constantes.getSessao().getObjeto());
                posJogo.put("nomeUsuario", Constantes.getSessao().getObjeto().getString("nome").trim());
                Constantes.setSessao(null);

                listaDePosJogo.add(posJogo);
                adapterUsuarios = new UsuarioPosJogoListAdapter(context, listaDePosJogo, false);
                usuarios.setAdapter(adapterUsuarios);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void carregarRegistro() {
        if (this.jogo != null) {
            tvNomeTime1.setText(this.jogo.getString("nomeTime").trim().toUpperCase().substring(0, 3));
            tvNomeTime2.setText(this.jogo.getString("nomeTime2").trim().toUpperCase().substring(0, 3));

            String placar = String.valueOf(this.jogo.getInt("qtdGolsTime1")).trim() +
                    " X " + String.valueOf(this.jogo.getInt("qtdGolsTime2")).trim();
            tvVersus.setText(placar);
        }
    }

    private void atualizarLista() {
        final Dialog progrsso = FuncoesParse.showProgressBar(context, "Carregando...");
        this.jogo.getRelation("posJogoUser").getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                FuncoesParse.dismissProgressBar(progrsso);
                if (e == null) {
                    listaDePosJogo = list;
                    adapterUsuarios = new UsuarioPosJogoListAdapter(context, list, false);
                    usuarios.setAdapter(adapterUsuarios);
                    if (list.size() == 0) {
                        perguntarSobreAvaliacao();
                    }
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    private void perguntarSobreAvaliacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pergunta");
        builder.setIcon(R.drawable.questionmark_64);
        builder.setMessage("Deseja avaliar os jogadores do seu time?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                carregarUsuarios();
            }
        });
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void carregarUsuarios() {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Buscando jogadores...");
        Constantes.getTimeSelecionado().getRelation("usuarios").getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    adapterUsuarios = new UsuarioPosJogoListAdapter(context, list, true);
                    usuarios.setAdapter(adapterUsuarios);
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    //INUTILIZADO POR ENQUANTO.
    private void salvar(List<ParseObject> lista) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");
        ParseObject.saveAllInBackground(lista, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    funcoes.mostrarToast(1);
                } else {
                    funcoes.mostrarToast(2);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pos_jogo, menu);
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
            // NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_adicionar_avaliacao) {
            mudarTelaComRetorno(UsuariosTimeActivity.class, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        posJogoControl.exibirFeedBackPosJogoJogador(adapterUsuarios.getItem(position), jogo, context);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }
}
