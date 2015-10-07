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
import br.com.sharkweb.fbv.adapter.MovimentoListAdapter;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

public class MovimentosActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView movimentos;
    private ArrayList<Movimento> listaMovimentos;
    private MovimentoListAdapter adapterMovimentos;
    private MovimentoController movimentosControl = new MovimentoController(this);
    private CaixaController caixaControl = new CaixaController(this);
    private Caixa caixa;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentos);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        movimentos = (ListView) findViewById(R.id.movimentoslist_listviewmovimentos);
        movimentos.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            // this.caixa = caixaControl.selectCaixaPorId(params.getInt("id_caixa")).get(0);
        } else {
            this.caixa = null;
        }

        atualizarLista();
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
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void atualizarLista() {
    /*    if (caixa!=null){
            listaMovimentos = movimentosControl.selectMovimentosPorIdCaixa(caixa.getId());
        }else {
            listaMovimentos = movimentosControl.selectMovimentos();
        }*/
        listaMovimentos = movimentosControl.selectMovimentos();

        if (listaMovimentos.size() == 0) {
            ArrayList<Movimento> listaVazia = new ArrayList<Movimento>();
            listaVazia.add(new Movimento(0, 0, "Nenhum movimento encontrado.", "", 0, "", 0));
            adapterMovimentos = new MovimentoListAdapter(this, listaVazia);
        } else
            adapterMovimentos = new MovimentoListAdapter(this, listaMovimentos);
        movimentos.setAdapter(adapterMovimentos);
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
        Movimento movimento = adapterMovimentos.getItem(position);
        if (movimento.getId() != 0) {

        }
    }
}
