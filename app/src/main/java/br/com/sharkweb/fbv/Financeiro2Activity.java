package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.CaixaController;
import br.com.sharkweb.fbv.controller.MovimentoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Time;

public class Financeiro2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;
    private Funcoes funcoes = new Funcoes(this);
    private MovimentoController movimentoControl = new MovimentoController(this);
    private TimeController timeControl = new TimeController(this);
    private CaixaController caixaControl = new CaixaController(this);
    private Caixa caixa;
    private double valor = 0;
    private Time time;

    private TextView txtSaldo;
    private TextView txtEmailUsuario;
    private TextView txtNomeUsuario;
    private ImageView imgPerfilUusario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       // fab.setBackgroundColor(getResources().getColor(R.color.AzulPrincipal));
        fab.setColorFilter(getResources().getColor(R.color.AzulPrincipal));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtSaldo = (TextView) findViewById(R.id.financeiro2_saldo);
        txtEmailUsuario = (TextView) findViewById(R.id.nav_header_financ_email);
        txtNomeUsuario = (TextView) findViewById(R.id.nav_header_financ_nome);
        imgPerfilUusario = (ImageView) findViewById(R.id.nav_header_financ_imgperfil);

        txtNomeUsuario.setText(Constantes.getUsuarioLogado().getNome().trim());
        txtEmailUsuario.setText(Constantes.getUsuarioLogado().getEmail().trim());
        imgPerfilUusario.setImageResource(R.drawable.profile4_68);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            time = timeControl.selectTimePorId(params.getInt("id_time")).get(0);
        } else {
            time = null;
        }

        carregarRegistro();
        atualizarSaldo();

    }

    private void carregarRegistro() {
        ArrayList<Caixa> caixa = caixaControl.selectJogosPorIdTime(time.getId());
        if (caixa.size() > 0) {
            this.caixa = caixa.get(0);
        } else {
            Caixa caixa2 = new Caixa(time.getId(), 0, 0);
            Long ret = caixaControl.inserir(caixa2);
            if (ret > 0) {
                caixa2.setId(Integer.valueOf(ret.toString()));
                this.caixa = caixa2;
            } else {
                funcoes.mostrarDialogAlert(3, "");
                return;
            }
        }
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
        getMenuInflater().inflate(R.menu.menu_financeiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cadastrar_entrada) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Nova Entrada");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valor = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("E", caixa, valor,0);
                    atualizarSaldo();

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }
        if (id == R.id.action_cadastrar_retirada) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Nova Retirada");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                    InputType.TYPE_NUMBER_FLAG_SIGNED);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valor = Double.valueOf(input.getText().toString());
                    movimentoControl.criarMovimento("R", caixa, valor,0);
                    atualizarSaldo();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_saldo) {
            // Handle the camera action
        } else if (id == R.id.nav_movimentos) {
            Bundle parametros = new Bundle();
            parametros.putInt("id_caixa", caixa.getId());
            mudarTela(MovimentosActivity.class, parametros);
        } else if (id == R.id.nav_mensalidades) {
            Bundle parametros = new Bundle();
            parametros.putInt("id_caixa", caixa.getId());
            mudarTela(MensalidadesActivity.class, parametros);

        } else if(id == R.id.nav_telaPrincipal){
            mudarTela(MainActivity.class);

        } else if (id == R.id.nav_manage) {

        }  else if (id == R.id.nav_send) {

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
    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void atualizarSaldo() {
        txtSaldo.setText("Saldo:\n R$" + funcoes.formatarNumeroComVirgula(caixa.getSaldo()));
    }
}
