package br.com.sharkweb.fbv;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.LoginController;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Usuario;

public class NewMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtEmailUsuario;
    private TextView txtNomeUsuario;
    private ImageView imgPerfilUusario;

    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private CaixaController caixaControl = new CaixaController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);
    private LoginController loginControl = new LoginController(this);
    private Funcoes funcoes = new Funcoes(this);
    private UsuarioController usuarioControl = new UsuarioController(this);
    private UFController ufControl = new UFController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //PARSE!
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "wmmuV3lyE7UQlyECrJwjJB22D03RD0gWZ3Ate89K", "7IegdKGnREcqge6xHTWW3YnRiRt7CJAiZOmpZTDm");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela principal
        }

        //INICIANDO DADOS FIXOS DO APLICATIVO E DADOS DE TESTE
        tipoUsuarioControl.IniciarTiposUsuarios();
        posicaoControl.IniciarPosicoes();
        usuarioControl.inicializarUsuarios();
        ufControl.inicializarUF();

        txtEmailUsuario = (TextView) findViewById(R.id.nav_header_main_email);
        txtNomeUsuario = (TextView) findViewById(R.id.nav_header_main_nome);
        imgPerfilUusario = (ImageView) findViewById(R.id.nav_header_main_imgperfil);

        //DEFININDO O USUARIO LOGADO NO SISTEMA.
        if (!loginControl.selecLogin().isEmpty()) {
            Usuario user = usuarioControl.selectUsuarioPorId(loginControl.selecLogin()
                    .get(0).getId_usuario()).get(0);
            Constantes.setUsuarioLogado(user);

            txtNomeUsuario.setText(Constantes.getUsuarioLogado().getNome().trim());
            txtEmailUsuario.setText(Constantes.getUsuarioLogado().getEmail().trim());
            imgPerfilUusario.setImageResource(R.drawable.profile4_68);
        } else {
            mudarTela(LoginActivity.class);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int id_time = data.getExtras().getInt("id_time");
        Bundle parametros = new Bundle();
        parametros.putInt("id_time", id_time);
        if (id_time > 0) {
            switch (requestCode) {
                case 1:
                    mudarTela(TimeDetalheActivity.class, parametros);
                    break;
                case 2:
                    mudarTela(CalendarioActivity.class, parametros);
                    break;
                case 3:
                    mudarTela(FinanceiroActivity.class, parametros);
                    break;
                case 4:
                    mudarTela(MovimentosActivity.class, parametros);
                    break;
                case 5:
                    mudarTela(MensalidadesActivity.class, parametros);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logoff) {
            loginControl.excluirTodosLogins();
            mudarTela(LoginActivity.class);
            return true;
        }

        if (id == R.id.action_sair) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle parametros = new Bundle();
        if (id == R.id.main_estatisticas) {
            // mTitle = "Inicial";
            if (loginControl.selecLogin().isEmpty()) {
                Bundle parametros2 = new Bundle();
                parametros2.putBoolean("salvo", true);
                mudarTela(LoginActivity.class, parametros2);
            }
            funcoes.mostrarDialogAlert(1, "Função ainda não implementada! Estará disponível nas próximas versões.");
        } else if (id == R.id.main_meuperfil) {
            parametros.putString("tipoAcesso", "edit");
            parametros.putInt("id_usuario", loginControl.selecLogin().get(0).getId_usuario());
            mudarTela(CadastroUsuarioActivity.class, parametros);

        } else if (id == R.id.main_config) {
            funcoes.mostrarDialogAlert(1, "Função ainda não implementada! Estará disponível nas próximas versões.");
        } else if (id == R.id.main_meustimes) {
            parametros.putBoolean("cadastrar", true);
            mudarTelaComRetorno(TeamActivity.class, parametros, 1);
        } else if (id == R.id.main_calendario) {
            parametros.putBoolean("cadastrar", false);
            mudarTelaComRetorno(TeamActivity.class, parametros, 2);
        } else if (id == R.id.main_caixa) {
            parametros.putBoolean("cadastrar", false);
            mudarTelaComRetorno(TeamActivity.class, parametros, 3);
        } else if (id == R.id.main_movimento) {
            parametros.putBoolean("cadastrar", false);
            mudarTelaComRetorno(TeamActivity.class, parametros, 4);
        } else if (id == R.id.main_mensalidade) {
            parametros.putBoolean("cadastrar", false);
            mudarTelaComRetorno(TeamActivity.class, parametros, 5);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, Bundle parametros, int key) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivityForResult(intent, key);
    }
}
