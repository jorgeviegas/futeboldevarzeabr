package br.com.sharkweb.fbv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.LoginController;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Usuario;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private CharSequence mTitle;

    private boolean loginFeito;
    private TextView tvNomeUser;

    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);
    private LoginController loginControl = new LoginController(this);
    private Funcoes funcoes = new Funcoes(this);
    private UsuarioController usuarioControl = new UsuarioController(this);
    private UFController ufControl = new UFController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = "FBV";
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela principal
            this.loginFeito = params.getBoolean("login") == true;
        } else {
            this.loginFeito = false;
        }

        //INICIANDO DADOS FIXOS DO APLICATIVO E DADOS DE TESTE
        tipoUsuarioControl.IniciarTiposUsuarios();
        posicaoControl.IniciarPosicoes();
        usuarioControl.inicializarUsuarios();
        ufControl.inicializarUF();

        //DEFININDO O USUARIO LOGADO NO SISTEMA.
        if (!loginControl.selecLogin().isEmpty()) {
            Usuario user = usuarioControl.selectUsuarioPorId(loginControl.selecLogin()
                    .get(0).getId_usuario()).get(0);
            Constantes.setUsuarioLogado(user);
        }

       /* tvNomeUser = (TextView) findViewById(R.id.nagivation_edtNomeUsuario);
        tvNomeUser.setVisibility(TextView.VISIBLE);
        String nomeUser = "";
        if (Constantes.getUsuarioLogado() != null) {
            nomeUser = Constantes.getUsuarioLogado().getNome();
        }*/

       /* tvNomeUser.setText(nomeUser.toUpperCase());
        tvNomeUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle parametros = new Bundle();
                parametros.putString("tipoAcesso", "edit");
                parametros.putInt("id_usuario", loginControl.selecLogin().get(0).getId_usuario());
                //mudarTelaComRetorno(CadastroUsuarioActivity.class, parametros, 3);
                mudarTela(CadastroUsuarioActivity.class, parametros);
            }
        });*/
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
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                // mTitle = "Inicial";
                if (loginControl.selecLogin().isEmpty()) {
                    Bundle parametros2 = new Bundle();
                    parametros2.putBoolean("salvo", true);
                    mudarTela(LoginActivity.class, parametros2);
                }
                break;
            case 2:
                Bundle parametros = new Bundle();
                parametros.putString("tipoAcesso", "edit");
                parametros.putInt("id_usuario", loginControl.selecLogin().get(0).getId_usuario());
                mudarTela(CadastroUsuarioActivity.class, parametros);
                break;
            case 3:
                // mTitle = "Meus times";
                mudarTelaComRetorno(TeamActivity.class, 1);
                break;
            case 4:
                //mTitle = "Calendario";
                mudarTelaComRetorno(TeamActivity.class, 2);
                break;
            case 5:
                //mTitle = "Calendario";
                mudarTelaComRetorno(FinanceiroActivity.class, 2);
                //funcoes.mostrarDialogAlert(1,"Está quase pronto! Estamos com essa função no forno!");
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
