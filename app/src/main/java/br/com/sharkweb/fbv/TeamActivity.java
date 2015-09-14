package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;

public class TeamActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView times;
    private ArrayList<Time> listaTimes;
    private TimeListAdapter adapterTimes;
    private TimeController timesControl = new TimeController(this);
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);
    private TimeUsuarioController timeuserControl = new TimeUsuarioController(this);
    private Usuario user;
    private UsuarioController userControl = new UsuarioController(this);
    private boolean esperaRetorno;
    private Time timeSelecionado;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        times = (ListView) findViewById(R.id.timelist_listviewTimes);
        times.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            esperaRetorno = params.getBoolean("esperaRetorno");
        } else {
            this.user = null;
        }

        //Agora essa tela sempre retorna
        esperaRetorno = true;

        this.user = userControl.selectUsuarioPorId(Constantes.getUsuarioLogado().getId()).get(0);
        //this.user = null;

       /* if (Constantes.getUsuarioLogado().getApelido().equals("kleintiago")
                && timesControl.selectTimes().isEmpty()){
            Time time = new Time("GREMIO","PORTO ALEGRE",22);
            Time time2 = new Time("INTERNACIONAL","PORTO ALEGRE",22);
            timesControl.inserir(time);
            timesControl.inserir(time2);
            int tipo_usuario = tipouserControl.selectTiposUsuariosPorTipo("Administrador").get(0).getId();
            TimeUsuario timeUser = new TimeUsuario(time.getId(),
                    Constantes.getUsuarioLogado().getId(), 0, "", tipo_usuario);
            Long ret2 = timeuserControl.inserir(timeUser);

        }*/

        atualizarLista();
        times.setCacheColorHint(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        if (this.esperaRetorno) {
            Intent it = new Intent();
            if (timeSelecionado != null)
                it.putExtra("id_time", timeSelecionado.getId());
            else it.putExtra("id_time", 0);
            setResult(1, it);
        }
        super.onBackPressed();
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
        // if (id == R.id.time_action_cancelar) {
        //    onBackPressed();
        //     return true;
        // }

        if (id == android.R.id.home) {
            onBackPressed();
            //NavUtils.navigateUpFromSameTask(this);
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

    public void atualizarLista() {

        if (this.user != null) {
            listaTimes = timesControl.selectTimePorIdUsuario(this.user.getId());
        } else {
            listaTimes = timesControl.selectTimes();
        }
        if (listaTimes.size() == 0) {
            ArrayList<Time> listaVazia = new ArrayList<Time>();
            listaVazia.add(new Time(0, "Nenhum time encontrado.", "", 0));
            adapterTimes = new TimeListAdapter(this, listaVazia);
        } else
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
        if (time.getId() != 0) {

            if (esperaRetorno) {
                this.timeSelecionado = time;
                onBackPressed();
            }
          /*  else{
                Bundle parametros = new Bundle();
                parametros.putInt("id_time", time.getId());
                mudarTela(TimeDetalheActivity.class, parametros);
            }*/

        }
    }
}
