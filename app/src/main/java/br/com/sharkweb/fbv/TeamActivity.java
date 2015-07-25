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
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Time;

public class TeamActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView times;
    private ArrayList<Time> listaTimes;
    private TimeListAdapter adapterTimes;
    private TimeController timesControl = new TimeController(this);
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);


        times = (ListView) findViewById(R.id.timelist_listviewTimes);
        times.setOnItemClickListener(this);

        atualizarLista();
        times.setCacheColorHint(Color.TRANSPARENT);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.time_action_cadastrar);

        //Somente usuarios administradores podem usar o menu cadastrar
        if (tipouserControl.selectTiposUsuariosPorId(Constantes.getUsuarioLogado().
                getId_tipo()).get(0).getTipo().equals("Administrador"))
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.time_action_cancelar) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.time_action_cadastrar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja cadastrar um novo Time?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //Bundle parametros = new Bundle();
                    // parametros.putString("tipoAcesso", "write");
                    mudarTela(CadastroTimeActivity.class);
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

    public void atualizarLista(){

        listaTimes = timesControl.selectTimes();
        if(listaTimes.size() == 0){
            ArrayList<Time> listaVazia = new ArrayList<Time>();
            listaVazia.add(new Time(0, "Nenhum time encontrado."));
            adapterTimes = new TimeListAdapter(this, listaVazia);
        }
        else
            adapterTimes = new TimeListAdapter(this, listaTimes);
            times.setAdapter(adapterTimes);
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
        Time time = adapterTimes.getItem(position);
        if(time.getId() != 0){
            Bundle parametros = new Bundle();
            parametros.putInt("id_time", time.getId());
            //mudarTela(EscolhaClientes.class, parametros);
        }
    }
}
