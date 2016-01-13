package br.com.sharkweb.fbv;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.model.Sessao;

public class UsuariosTimeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView usuarios;
    private UsuarioListAdapter adapterUsuarios;
    private ParseObject time;
    private Funcoes funcoes = new Funcoes(this);
    private ParseObject usuarioSelecionado;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        usuarios = (ListView) findViewById(R.id.usuarioslist_listviewusuarios);
        usuarios.setOnItemClickListener(this);

        this.time = Constantes.getTimeSelecionado();

        atualizarLista();
        usuarios.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        if (usuarioSelecionado != null)
            it.putExtra("usuario", usuarioSelecionado.getObjectId());
        else it.putExtra("usuario", "");
        setResult(1, it);
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // MenuItem m1 = menu.findItem(R.id.time_action_cadastrar);
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

        //noinspection SimplifiableIfStatement
        // if (id == R.id.time_action_cancelar) {
        //    onBackPressed();
        //     return true;
        // }

        if (id == android.R.id.home) {
            onBackPressed();
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void atualizarLista() {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Buscando Jogadores...");
        this.time.getRelation("usuarios").getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    adapterUsuarios = new UsuarioListAdapter(context, list, 2,false,true);
                    usuarios.setAdapter(adapterUsuarios);
                } else {
                    funcoes.mostrarToast(3);
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
        ParseObject user = adapterUsuarios.getItem(position);
        if (!user.getObjectId().isEmpty()) {
            this.usuarioSelecionado = user;
            Sessao sessao = new Sessao(1, user, "User");
            Constantes.setSessao(sessao);
            onBackPressed();
        }
    }
}
