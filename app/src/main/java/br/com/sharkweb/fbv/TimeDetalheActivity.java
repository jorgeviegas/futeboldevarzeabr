package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.Util.RoundedImageView;
import br.com.sharkweb.fbv.adapter.UsuarioListAdapter;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.model.Sessao;


public class TimeDetalheActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView tvNomeTime;
    private TextView tvEndereco;
    private CheckBox chkInativo;
    private ImageView imgPerfilTime;
    private Funcoes funcoes = new Funcoes(this);
    private List<ParseObject> listaUsuarios;

    private ParseObject time;
    private ListView listaJogadores;
    private UsuarioListAdapter adapterUsuarios;
    private UFController ufControl = new UFController(this);
    final Context context = this;
    private String m_Text = "";
    static final int IMAGE_VIEW_ACTIVITY_REQUEST_CODE = 101;
    static final int USUARIO_ACTIVITY_REQUEST_CODE = 102;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_detalhe);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvNomeTime = (TextView) findViewById(R.id.timeDetalhe_tvNomeTime);
        tvNomeTime.setVisibility(TextView.VISIBLE);

        tvEndereco = (TextView) findViewById(R.id.time_detalhe_endereco);
        tvEndereco.setVisibility(TextView.VISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.time_detalhe_progressBar);
        progressBar.setVisibility(View.GONE);

        chkInativo = (CheckBox) findViewById(R.id.time_detalhe_chkInativo);
        chkInativo.setVisibility(View.VISIBLE);
        chkInativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarUsuarios();
            }
        });

        imgPerfilTime = (ImageView) findViewById(R.id.time_detalhe_imgPerfil);
        imgPerfilTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FuncoesParse.isAdmin()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.questionmark_64);
                    builder.setCancelable(true);
                    String[] arrayOpcoes = new String[1];
                    arrayOpcoes[0] = "Alterar Imagem";
                    builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            escolherImagemPerfil();
                        }
                    });
                    AlertDialog dialogExportar = builder.create();
                    dialogExportar.show();
                }
            }
        });

        //Setando o objeto ParseObject do time.
        this.time = Constantes.getTimeSelecionado();

        ParseFile imagemPerfil = time.getParseFile("ImageFile");
        if (imagemPerfil != null) {
            try {
                byte[] bitmapdata = imagemPerfil.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                imgPerfilTime.setImageBitmap(bitmap);
            } catch (ParseException e) {
                e.printStackTrace();
                imgPerfilTime.setImageResource(R.drawable.estadio);
            }
        } else {
            imgPerfilTime.setImageResource(R.drawable.estadio);
            imgPerfilTime.setMaxHeight(80);
        }

        tvNomeTime.setText(funcoes.PrimeiraLetraMaiuscula(this.time.getString("nome").trim()+"\n"));
        tvEndereco.setText(time.getString("cidade").trim() + " - " +
                ufControl.selectUFPorId(time.getInt("id_uf")).get(0).getNome().trim());
        listaJogadores = (ListView) findViewById(R.id.timeDetalhe_listJogadores);
        //listaJogadores.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        listaJogadores.setBackgroundColor(Color.WHITE);
        listaJogadores.setOnItemClickListener(this);
        listaJogadores.setOnItemLongClickListener(this);
        buscarUsuarios();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case IMAGE_VIEW_ACTIVITY_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                try {
                    Bitmap bitmap;
                    if (data.getData() == null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    RoundedImageView arredondarImagem = new RoundedImageView(context);
                    Bitmap bitArredondado = arredondarImagem.getCroppedBitmap(bitmap, 300);
                    imgPerfilTime.setImageBitmap(bitArredondado);
                    FuncoesParse.inserirImagemPerfil(context, time, bitArredondado);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case USUARIO_ACTIVITY_REQUEST_CODE:
                if (Constantes.getSessao() != null) {
                    inserirJogador(Constantes.getSessao().getObjeto());
                    Constantes.setSessao(null);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void buscarUsuarios() {
        //final Dialog progresso = FuncoesParse.showProgressBar(context, "Carregando....");
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery query = this.time.getRelation("usuarios").getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                //FuncoesParse.dismissProgressBar(progresso);
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
        adapterUsuarios = new UsuarioListAdapter(this.context, this.listaUsuarios, 1, chkInativo.isChecked(), true);
        listaJogadores.setAdapter(adapterUsuarios);
    }

    public void inserirJogador(final ParseObject usuario) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");

        boolean usuarioJaExistente = false;
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getObjectId().trim().equals(usuario.getObjectId().trim())) {
                usuarioJaExistente = true;
            }
        }
        if (!usuarioJaExistente) {
            time.getRelation("usuarios").add(usuario);
            time.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FuncoesParse.dismissProgressBar(progresso);
                        listaUsuarios.add(usuario);
                        atualizarLista();
                        FuncoesParse.enviarNotificacao(context, usuario, "O time " + time.getString("nome").trim() +
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
            funcoes.mostrarDialogAlert(1, "Este usuário já faz parte do time.");
        }
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
                    mudarTelaComRetorno(BuscaUsuarioActivity.class, USUARIO_ACTIVITY_REQUEST_CODE);
                    // pedirEmailApelido();
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
            final TextView tvTipoUsuario = ((TextView) view.findViewById(R.id.usuariolist_tipoUsuario));

            if (FuncoesParse.isAdmin()
                    && tvTipoUsuario.getText().toString().trim().equals("Pendente")) {
                arrayOpcoes = new String[3];
                arrayOpcoes[0] = "Informações de contato";
                arrayOpcoes[1] = "Excluir convite";
                arrayOpcoes[2] = "Enviar convite novamente";
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
                            time.getRelation("usuarios").remove(user);
                            time.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        FuncoesParse.excluirNotificacao(context, user, time.getObjectId().trim(), "Pergunta");
                                        funcoes.mostrarToast(1);
                                        buscarUsuarios();
                                    } else {
                                        funcoes.mostrarToast(2);
                                    }
                                }
                            });
                            //timeusuarioControl.tornarAdmin(time.getId(), user.getId());
                            //atualizarLista();
                            break;
                        case 2:
                            FuncoesParse.enviarNotificacao(context, user, "O time " + time.getString("nome").trim() +
                                            " convidou você para fazer parte do time. Deseja aceitar?",
                                    time.getObjectId().trim(), "Pergunta");
                            break;
                        case 3:
                            // if (FuncoesParse.isInativo(user)) {
                            //timeusuarioControl.ativarUsuario(time.getId(), user.getId());
                            // } else {
                            // timeusuarioControl.inativarUsuario(time.getId(), user.getId());
                            //  }
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

    private void escolherImagemPerfil() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = "Tire agora ou selecione uma imagem";
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        startActivityForResult(chooserIntent, IMAGE_VIEW_ACTIVITY_REQUEST_CODE);
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
