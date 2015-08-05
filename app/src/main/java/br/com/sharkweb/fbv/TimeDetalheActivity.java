package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;


public class TimeDetalheActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView tvNomeTime;
    private CheckBox chkInativo;
    private Funcoes funcoes = new Funcoes(this);
    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<TimeUsuario> listaTimesUsuario;

    private TimeController timeControl = new TimeController(this);
    private Time time;
    private ListView listaJogadores;
    private UsuarioListAdapter adapterUsuarios;
    final Context context = this;
    private UsuarioController usuarioControl = new UsuarioController(this);
    private TimeUsuarioController timeusuarioControl = new TimeUsuarioController(this);
    private TipoUsuarioController tipouserControl = new TipoUsuarioController(this);
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_detalhe);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela principal
            this.time = timeControl.selectTimePorId(params.getInt("id_time")).get(0);
        }else{
            this.time = new Time("Time nao encontrado","","");
        }

        tvNomeTime = (TextView) findViewById(R.id.timeDetalhe_tvNomeTime);
        tvNomeTime.setVisibility(TextView.VISIBLE);

        chkInativo = (CheckBox) findViewById(R.id.time_detalhe_chkInativo);
        chkInativo.setVisibility(View.VISIBLE);
        chkInativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarLista();
            }
        });

        tvNomeTime.setText(" " + funcoes.PrimeiraLetraMaiuscula(this.time.getNome()));
        listaJogadores = (ListView) findViewById(R.id.timeDetalhe_listJogadores);
        //listaJogadores.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        listaJogadores.setBackgroundColor(Color.WHITE);
        listaJogadores.setOnItemClickListener(this);
        listaJogadores.setOnItemLongClickListener(this);
        //listaJogadores.setCacheColorHint(Color.TRANSPARENT);


        atualizarLista();
        //timeusuarioControl.excluirTodosTimesUsuarios();

    }

    public void atualizarLista(){
        listaUsuarios = new ArrayList<Usuario>();

        if (!chkInativo.isChecked()){
            listaTimesUsuario = timeusuarioControl.selectTimeUsuarioPorIdTime(this.time.getId());
        }else{
            listaTimesUsuario = timeusuarioControl.selectTimeUsuarioPorIdTimeComInativos(this.time.getId());
        }

        for (int i = 0; i < listaTimesUsuario.size(); i++){
            listaUsuarios.add(usuarioControl.selectUsuarioPorId(listaTimesUsuario.get(i).getId_usuario()).get(0));
        }

        if(listaUsuarios.size() == 0){
            ArrayList<Usuario> listaVazia = new ArrayList<Usuario>();
            listaVazia.add(new Usuario(0, "Nenhum jogador encontrado.", "", "", "", 0, 0, 0, "", ""));
            adapterUsuarios = new UsuarioListAdapter(this, listaVazia, time);
        }
        else
            adapterUsuarios = new UsuarioListAdapter(this, listaUsuarios, time);
            listaJogadores.setAdapter(adapterUsuarios);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.timedetalhe_action_cadastrarJogador);
        MenuItem m2 = menu.findItem(R.id.timedetalhe_action_editar);

        //Somente usuarios administradores podem usar o menu inserir jogador
        if (tipouserControl.selectTiposUsuariosPorId(Constantes.getUsuarioLogado().
                getId_tipo()).get(0).getTipo().equals("Administrador")) {
            m1.setVisible(true);
            m2.setVisible(true);
        }else {
            m2.setVisible(false);
            m1.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_detalhe, menu);
        return true;
    }

    public String PedirEmailApelido(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe o E-mail ou Nome de usuario do jogador");

// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return m_Text;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.timedetalhe_action_voltar) {
            onBackPressed();
            return true;
        }

        if (id == R.id.timedetalhe_action_editar) {
            Bundle parametros = new Bundle();
            parametros.putString("tipoAcesso", "edit");
            parametros.putInt("id_time",time.getId());
            mudarTela(CadastroTimeActivity.class, parametros);
            return true;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.timedetalhe_action_cadastrarJogador) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja inserir um novo Jogador?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //ISSO AQUI É TESTE MAROTAO
                    ArrayList<Usuario> users = usuarioControl.selectUsuarios();

                    for (int i = 0; i < users.size(); i++) {
                        TimeUsuario timeUser = new TimeUsuario(time.getId(), users.get(i).getId(),0,"");
                        timeusuarioControl.inserir(timeUser);
                    }
                    atualizarLista();

                    //String ret = PedirEmailApelido();
                   // if (!ret.isEmpty())
                    //funcoes.mostrarDialogAlert(0,"alo",ret);
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
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
       final Usuario user = adapterUsuarios.getItem(position);
        if (user.getId() > 0) {
            final TimeUsuario tipoUser = timeusuarioControl.selectTimeUsuarioPorIdTimeeIdUsuario(time.getId(), user.getId()).get(0);
            //Menu de opções que o usuário pode fazer com os usuarios.
            String[] arrayOpcoes = new String[2];
            arrayOpcoes[0] = "Visualizar";
            if (tipoUser.getInativo() > 0) {
                arrayOpcoes[1] = "Ativar usuario";
            } else {
                arrayOpcoes[1] = "Inativar usuario";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("O que deseja fazer?");
            builder.setCancelable(true);
            builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    if (arg1 == 0) {
                        Bundle parametros = new Bundle();
                        parametros.putString("tipoAcesso", "read");
                        parametros.putInt("id_usuario", user.getId());
                        mudarTela(CadastroUsuarioActivity.class, parametros);
                    } else {
                        if (tipoUser.getInativo() > 0) {
                            timeusuarioControl.ativarUsuario(time.getId(), user.getId());
                        } else {
                            timeusuarioControl.inativarUsuario(time.getId(), user.getId());
                        }
                        atualizarLista();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialogExportar = builder.create();
            dialogExportar.show();

        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}