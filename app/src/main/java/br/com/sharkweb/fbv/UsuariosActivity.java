package br.com.sharkweb.fbv;

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

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class UsuariosActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView usuarios;
    private ArrayList<Usuario> listaUsuarios;
    private UsuarioListAdapter adapterUsuarios;
    private TimeController timesControl = new TimeController(this);
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);
    private Usuario user;
    private UsuarioController userControl = new UsuarioController(this);
    private Usuario usuarioSelecionado;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        usuarios = (ListView) findViewById(R.id.usuarioslist_listviewusuarios);
        usuarios.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {

        } else {
            this.user = null;
        }

        this.user = userControl.selectUsuarioPorId(Constantes.getUsuarioLogado().getId(),"").get(0);

        atualizarLista();
        usuarios.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onBackPressed() {
            Intent it = new Intent();
            if (usuarioSelecionado != null)
                it.putExtra("id_usuario",usuarioSelecionado.getId());
            else it.putExtra("id_usuario",0);
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

    public void atualizarLista(){

       listaUsuarios = userControl.selectUsuarios(false);

        if(listaUsuarios.size() == 0){
            ArrayList<Usuario> listaVazia = new ArrayList<Usuario>();
            listaVazia.add(new Usuario(0, "Nenhum usuário encontrado.", "", "","", 0, 0, 0, "", "",""));
            adapterUsuarios = new UsuarioListAdapter(this, listaVazia,null,2);
        }
        else
            adapterUsuarios = new UsuarioListAdapter(this, listaUsuarios,null,2);
            usuarios.setAdapter(adapterUsuarios);
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
        Usuario user = adapterUsuarios.getItem(position);
        if(user.getId() != 0){
             this.usuarioSelecionado = user;
                onBackPressed();
        }
    }
}
