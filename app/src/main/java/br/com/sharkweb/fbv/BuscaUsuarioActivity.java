package br.com.sharkweb.fbv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Sessao;

public class BuscaUsuarioActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView usuarios;
    private UsuarioListAdapter adapterUsuario;
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);
    private ParseQuery queryUsuario = new ParseUser().getQuery();
    private TextView txtPesquisaUsuario;
    private TextView txtEditarPesquisa;
    final private Funcoes funcoes = new Funcoes(this);
    final Context context = this;
    private static ProgressBar progressBar;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_usuarios);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.busca_usuarios_progressBar);
        progressBar.setVisibility(View.GONE);
        progressBar.setMax(200);

        usuarios = (ListView) findViewById(R.id.busca_usuarios_listview);
        usuarios.setOnItemClickListener(this);
        usuarios.setCacheColorHint(Color.TRANSPARENT);

        txtPesquisaUsuario = (TextView) findViewById(R.id.busca_usuarios_pesquisar);
        txtPesquisaUsuario.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    buscarUsuarios();
                    return true;
                }
                return false;
            }
        });

        txtEditarPesquisa = (TextView) findViewById(R.id.busca_usuarios_editarPesquisa);
        txtEditarPesquisa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    private void buscarUsuarios() {
        progressBar.setVisibility(View.VISIBLE);
        queryUsuario.whereContains("username", txtPesquisaUsuario.getText().toString().trim());
        queryUsuario.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    adapterUsuario = new UsuarioListAdapter(context, list, 2, false, false);
                    usuarios.setAdapter(adapterUsuario);
                } else {
                    funcoes.mostrarToast(4);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_team, menu);
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
        ParseObject usuario = adapterUsuario.getItem(position);
        //Setando o usuario selecionado na pesquisa.
        Sessao sessao = new Sessao(3, usuario, "User");
        Constantes.setSessao(sessao);
        onBackPressed();
    }
}
