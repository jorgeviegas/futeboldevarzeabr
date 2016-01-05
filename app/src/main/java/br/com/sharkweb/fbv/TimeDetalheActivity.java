package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;
import org.xml.sax.helpers.ParserAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapterParse;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TimeUsuarioController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.controllerParse.TimeControllerParse;
import br.com.sharkweb.fbv.model.ParseProxyObject;
import br.com.sharkweb.fbv.model.Sessao;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;


public class TimeDetalheActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView tvNomeTime;
    private CheckBox chkInativo;
    private Funcoes funcoes = new Funcoes(this);
    private List<ParseObject> listaUsuarios;

    private ParseObject time;
    private ListView listaJogadores;
    private UsuarioListAdapterParse adapterUsuarios;
    final Context context = this;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_detalhe);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvNomeTime = (TextView) findViewById(R.id.timeDetalhe_tvNomeTime);
        tvNomeTime.setVisibility(TextView.VISIBLE);

        chkInativo = (CheckBox) findViewById(R.id.time_detalhe_chkInativo);
        chkInativo.setVisibility(View.VISIBLE);
        chkInativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarUsuarios();
            }
        });

        //Setando o objeto ParseObject do time.
        this.time = Constantes.getTimeSelecionado();

        tvNomeTime.setText(" " + funcoes.PrimeiraLetraMaiuscula(this.time.getString("nome").trim()));
        listaJogadores = (ListView) findViewById(R.id.timeDetalhe_listJogadores);
        //listaJogadores.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        listaJogadores.setBackgroundColor(Color.WHITE);
        listaJogadores.setOnItemClickListener(this);
        listaJogadores.setOnItemLongClickListener(this);
        //listaJogadores.setCacheColorHint(Color.TRANSPARENT);

        buscarUsuarios();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                String objectIdUsuario = data.getExtras().getString("usuario");
                if (!objectIdUsuario.isEmpty()) {
                    inserirJogador(objectIdUsuario);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void buscarUsuarios() {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Carregando....");
        ParseQuery query = this.time.getRelation("usuarios").getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    listaUsuarios = list;
                    atualizarLista();
                } else {
                    funcoes.mostrarToast(3);
                }
            }
        });
    }

    public void atualizarLista() {
        adapterUsuarios = new UsuarioListAdapterParse(this.context, this.listaUsuarios, 2, chkInativo.isChecked());
        listaJogadores.setAdapter(adapterUsuarios);
    }

    public void inserirJogador(String username) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (e == null) {
                    time.getRelation("usuarios").add(parseUser);
                    time.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FuncoesParse.dismissProgressBar(progresso);
                                listaUsuarios.add(parseUser);
                                atualizarLista();
                                FuncoesParse.enviarNotificacao(context, parseUser, "O time " + time.getString("nome").trim() +
                                                " convidou você para fazer parte do time. Deseja aceitar?",
                                        time.getObjectId().trim(), "Pergunta");
                            } else {
                                FuncoesParse.dismissProgressBar(progresso);
                                funcoes.mostrarToast(2);
                            }
                        }
                    });
                } else {
                    FuncoesParse.dismissProgressBar(progresso);
                    if (e.getCode() == 101) {
                        funcoes.mostrarDialogAlert(1, "Nenhum usuário encontrado com esse nome.");
                    } else {
                        funcoes.mostrarToast(2);
                    }
                }
            }
        });
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

        //ArrayList<String> configsTimes = (ArrayList<String>) ParseUser.getCurrentUser().get("configTimes");

        //Somente usuarios administradores do time podem usar o menu inserir jogador
        if (FuncoesParse.isAdmin()) {
            m1.setVisible(true);
            m2.setVisible(true);
        } else {
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

    public void pedirEmailApelido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe o Nome de usuario FBV do jogador");

// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                inserirJogador(m_Text);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void favoritarTime() {
        ParseUser.getCurrentUser().put("timeFavorito", this.time.getObjectId());
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    funcoes.mostrarToast(1);
                } else {
                    funcoes.mostrarToast(2);
                    ParseUser.getCurrentUser().saveEventually();
                }
            }
        });
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

        if (id == R.id.timedetalhe_action_favoritar) {
            favoritarTime();
            return true;
        }

        if (id == R.id.timedetalhe_action_editar) {
            Bundle parametros = new Bundle();
            parametros.putString("tipoAcesso", "edit");
            Sessao sessao = new Sessao(1, this.time, "time");
            Constantes.setSessao(sessao);
            mudarTela(CadastroTimeActivity.class, parametros);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.timedetalhe_action_cadastrarJogador) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.questionmark_64);
            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja inserir um novo Jogador?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    pedirEmailApelido();
                    //mudarTelaComRetorno(UsuariosActivity.class, 1);

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
        final ParseObject user = adapterUsuarios.getItem(position);
        if (!user.getObjectId().isEmpty()) {
            //Menu de opções que o usuário pode fazer com os usuarios.
            String[] arrayOpcoes = new String[1];
            arrayOpcoes[0] = "Informações de contato";

            TextView tvTipoUsuario = ((TextView) view.findViewById(R.id.usuariolist_tipoUsuario));

            if (tvTipoUsuario.getText().toString().trim().equals("Pendente")
                    || FuncoesParse.isAdmin()) {
                arrayOpcoes = new String[2];
                arrayOpcoes[0] = "Informações de contato";
                arrayOpcoes[1] = "Enviar convite novamente";
            }
            //Diponível somente para usuarios administradores do time.
           /* if (FuncoesParse.isAdmin()) {
                arrayOpcoes = new String[3];
                arrayOpcoes[0] = "Informações de contato";
                //arrayOpcoes[1] = "Tornar Admin do time";
                if (FuncoesParse.isInativo(user)) {
                    //arrayOpcoes[2] = "Ativar usuario";
                } else {
                    //arrayOpcoes[2] = "Inativar usuario";
                }
            }*/

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.questionmark_64);
            builder.setTitle("O que deseja fazer?");
            builder.setCancelable(true);
            builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    switch (arg1) {
                        case 0:
                            funcoes.exibirDetalheUsuario(user, context);
                            break;
                        case 1:
                            FuncoesParse.enviarNotificacao(context, user, "O time " + time.getString("nome").trim() +
                                            " convidou você para fazer parte do time. Deseja aceitar?",
                                    time.getObjectId().trim(), "Pergunta");
                            // timeusuarioControl.tornarAdmin(time.getId(), user.getId());
                            //atualizarLista();
                            break;
                        case 2:
                            if (FuncoesParse.isInativo(user)) {
                                //timeusuarioControl.ativarUsuario(time.getId(), user.getId());
                            } else {
                                // timeusuarioControl.inativarUsuario(time.getId(), user.getId());
                            }
                            // atualizarLista();
                            break;
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

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, int key) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, key);
    }
}
