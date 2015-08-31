package br.com.sharkweb.fbv;

import android.app.AlertDialog;
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

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.adapter.LocalListAdapter;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class LocalActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView locais;
    private ArrayList<Local> listaLocais;
    private LocalListAdapter adapterLocal;
    private TimeController timesControl = new TimeController(this);
    private LocalController localControl = new LocalController(this);
    private TipoUsuarioController tipoUserControl = new TipoUsuarioController(this);
    private TimeUsuarioController timeUsuarioControl = new TimeUsuarioController(this);
    private Local localSelecionado;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        locais = (ListView) findViewById(R.id.locallist_listview);
        locais.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {

        } else {
        }

        atualizarLista();
        locais.setCacheColorHint(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        if (localSelecionado != null)
            it.putExtra("id_local", localSelecionado.getId());
        else it.putExtra("id_local", 0);
        setResult(1, it);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                atualizarLista();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.local_action_cadastrar);

        //Somente usuarios administradores podem usar o menu cadastrar
       if (tipoUserControl.selectTiposUsuariosPorId(Constantes.getUsuarioLogado().
                getId_tipo()).get(0).getTipo().equals("Administrador"))
            m1.setVisible(true);
        else
            m1.setVisible(false);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local, menu);
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

        if (item.getItemId() == R.id.local_action_cadastrar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setIcon(R.drawable.questionmark_64);
            builder.setMessage("Tem certeza que deseja cadastrar um novo local?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Bundle parametros = new Bundle();
                    parametros.putString("tipoAcesso", "write");
                    mudarTelaComRetorno(CadastroLocalActivity.class,parametros, 1);
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

    public void atualizarLista() {

        listaLocais = localControl.selectLocais();

        if (listaLocais.size() == 0) {
            ArrayList<Local> listaVazia = new ArrayList<Local>();
            listaVazia.add(new Local(0, "Nenhum local encontrado.", "",0, "",0));
            adapterLocal = new LocalListAdapter(this, listaVazia);
        } else
            adapterLocal = new LocalListAdapter(this, listaLocais);
           locais.setAdapter(adapterLocal);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Local local = adapterLocal.getItem(position);
        if(local.getId() != 0){
                this.localSelecionado = local;
                onBackPressed();
        }
    }
}
