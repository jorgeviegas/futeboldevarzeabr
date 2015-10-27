package br.com.sharkweb.fbv;

import android.content.Context;
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

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.Util.Funcoes;
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
    private Jogo jogo;
    private Time time1;
    private Time time2;
    private PosJogo posJogo;
    private ArrayList<PosJogoUsuarios> listaUsuarios;
    private UsuarioPosJogoListAdapter adapterUsuarios;
    private Button btnCancelar;
    private Button btnSalvar;
    private TextView tvNomeTime1;
    private TextView tvNomeTime2;
    private TextView tvVersus;
    final Context context = this;

    private JogoController jogoControl = new JogoController(this);
    private PosJogoUsuariosController posJogoUsuariosControl = new PosJogoUsuariosController(this);
    private PosJogoController posJogoControl = new PosJogoController(this);
    private TimeUsuarioController timeUserControl = new TimeUsuarioController(this);
    private UsuarioController userControl = new UsuarioController(this);
    private TimeController timeControl = new TimeController(this);
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_jogo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        usuarios = (ListView) findViewById(R.id.pos_jogo_usuarioslist);
        usuarios.setOnItemClickListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            this.jogo = jogoControl.selectJogoPorId(params.getInt("id_jogo")).get(0);
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
                posJogoControl.exibirPlacarJogo(posJogo, context, tvVersus);

            }
        });
        btnSalvar = (Button) findViewById(R.id.pos_jogo_btncadastrar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.pos_jogo_btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //posJogoControl.excluirTodosPosJogo();
        //posJogoUsuariosControl.excluirTodosPosJogoUsuarios();
        carregarRegistro();
        atualizarLista();

    }

    private void carregarRegistro() {
        if (this.jogo != null) {
            this.time1 = timeControl.selectTimePorId(this.jogo.getId_time(),"").get(0);
            this.time2 = timeControl.selectTimePorId(this.jogo.getId_time2(),"").get(0);
            tvNomeTime1.setText(this.time1.getNome().trim().toUpperCase().substring(0, 3));
            tvNomeTime2.setText(this.time2.getNome().trim().toUpperCase().substring(0, 3));

            ArrayList<PosJogo> posJogo = posJogoControl.selectPosJogoPorIdJogo(this.jogo.getId());
            if (posJogo.isEmpty()) {
                this.posJogo = new PosJogo(this.jogo.getId(), 0, 0);
            } else {
                this.posJogo = posJogo.get(0);
                String placar = String.valueOf(this.posJogo.getQtd_gol_time1()).trim() +
                        " X " + String.valueOf(this.posJogo.getQtd_gol_time2()).trim();
                tvVersus.setText(placar);
            }
        } else {
            this.posJogo = new PosJogo(0, 0, 0);
        }
    }

    private void atualizarLista() {

        listaUsuarios = posJogoUsuariosControl.selectPosJogoUsuariosPorIdPosJogo(this.posJogo.getId());

        if (listaUsuarios.size() > 0) {
            adapterUsuarios = new UsuarioPosJogoListAdapter(this, listaUsuarios);
            usuarios.setAdapter(adapterUsuarios);
        } else {
            ArrayList<TimeUsuario> usertime = timeUserControl.selectTimeUsuarioPorIdTime(this.jogo.getId_time());
            ArrayList<PosJogoUsuarios> users = new ArrayList<>();
            for (int i = 0; i < usertime.size(); i++) {
                Usuario user = userControl.selectUsuarioPorId(usertime.get(i).getId(),"").get(0);
                PosJogoUsuarios posjogo = new PosJogoUsuarios(0, user.getId(), 0, 0, 0, 0);
                users.add(posjogo);
            }
            listaUsuarios = users;
            adapterUsuarios = new UsuarioPosJogoListAdapter(this, users);
            usuarios.setAdapter(adapterUsuarios);
        }
    }

    private void salvar() {
        boolean isOk = true;
        if (this.posJogo != null) {
            Long ret;
            if (this.posJogo.getId() > 0) {
                ret = posJogoControl.alterar(this.posJogo);
            } else {
                ret = posJogoControl.inserir(this.posJogo);
            }

            if (ret > 0) {
                for (int i = 0; i < listaUsuarios.size(); i++) {
                    int id_pos_jogo = Integer.valueOf(ret.toString());
                    listaUsuarios.get(i).setId_pos_jogo(id_pos_jogo);
                    Long ret2 = posJogoUsuariosControl.veriricarEinserir(listaUsuarios.get(i));
                    if (ret2 <= 0) {
                        isOk = false;
                        funcoes.mostrarDialogAlert(3, "");
                        break;
                    }
                }
                if (isOk) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    onBackPressed();
                }
            } else {
                isOk = false;
                funcoes.mostrarDialogAlert(3, "");
            }
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        posJogoControl.exibirFeedBackPosJogoJogador(adapterUsuarios.getItem(position),context);
    }
}
