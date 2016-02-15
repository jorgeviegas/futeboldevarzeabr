package br.com.sharkweb.fbv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.Util.RoundedImageView;
import br.com.sharkweb.fbv.controller.NotificacaoController;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UFController;
import br.com.sharkweb.fbv.model.ParseProxyObject;

public class NewMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtEmailUsuario;
    private TextView txtEmailUsuarioNaoConfirmado;
    private TextView txtNomeUsuario;
    private ImageView imgPerfilUusario;
    private Context context = this;

    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);
    private NotificacaoController notificacaoControl = new NotificacaoController(this);
    private TimeController timeControl = new TimeController(this);
    private Funcoes funcoes = new Funcoes(this);
    private UFController ufControl = new UFController(this);
    static final int IMAGE_VIEW_ACTIVITY_REQUEST_CODE = 101;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            notificacaoControl.receberNotificacaoConvite(mBundle);
        }

        //INICIANDO DADOS FIXOS DO APLICATIVO E DADOS DE TESTE
        tipoUsuarioControl.IniciarTiposUsuarios();
        //posicaoControl.excluirTodasPosicoes();
        posicaoControl.IniciarPosicoes();
        ufControl.inicializarUF();

        txtEmailUsuario = (TextView) findViewById(R.id.nav_header_main_email);
        txtEmailUsuarioNaoConfirmado = (TextView) findViewById(R.id.nav_header_main_email_confirmado);
        txtEmailUsuarioNaoConfirmado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                funcoes.reEnviarEmailConfirmacao();
            }
        });

        txtEmailUsuarioNaoConfirmado.setTextColor(context.getResources().getColor(R.color.vermelhoEscuro));
        txtNomeUsuario = (TextView) findViewById(R.id.nav_header_main_nome);
        imgPerfilUusario = (ImageView) findViewById(R.id.nav_header_main_imgperfil);
        imgPerfilUusario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
        });
        verificarUsuario();
    }

    private void verificarUsuario() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (ParseInstallation.getCurrentInstallation().get("User") == null) {
                ParseInstallation.getCurrentInstallation().put("User", currentUser);
                ParseInstallation.getCurrentInstallation().saveEventually();
            }
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (currentUser.getBoolean("emailVerified")) {
                        txtEmailUsuarioNaoConfirmado.setVisibility(View.GONE);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Por favor, confirme seu endereço de e-mail.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

            txtNomeUsuario.setText(currentUser.get("nome").toString().trim());
            txtEmailUsuario.setText(currentUser.getEmail().trim());

            ParseFile imagemPerfil = ParseUser.getCurrentUser().getParseFile("ImageFile");
            if (imagemPerfil != null) {
                try {
                    byte[] bitmapdata = imagemPerfil.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    imgPerfilUusario.setImageBitmap(bitmap);
                } catch (ParseException e) {
                    e.printStackTrace();
                    imgPerfilUusario.setImageResource(R.drawable.profile4_68);
                }
            } else {
                imgPerfilUusario.setImageResource(R.drawable.profile4_68);
            }
            //Verifica a existência de notificações ao usuário.
            ParseQuery queryNotific = new ParseQuery("notificacao");
            queryNotific.whereEqualTo("usuario", currentUser);
            queryNotific.whereEqualTo("lida", false);
            queryNotific.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null && list.size() > 0) {
                        exibirNotificacoes(list, 0);
                    }
                }
            });
        } else {
            mudarTela(LoginActivity.class);
        }
    }

    private void exibirNotificacoes(final List<ParseObject> list, final int count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.questionmark_64);
        builder.setTitle(list.get(count).getString("tipo").trim());
        builder.setMessage(list.get(count).getString("mensagem").trim());
        builder.setCancelable(false);
        String opcao = "Ok";
        if (list.get(count).getString("tipo").equals("Pergunta")) {
            opcao = "Sim";
        }
        builder.setPositiveButton(opcao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                timeControl.atualizarTime(list.get(count).getString("objectIdParam").trim());
                if ((count + 1) <= (list.size() - 1)) {
                    exibirNotificacoes(list, (count + 1));
                }
            }
        });
        if (list.get(count).getString("tipo").equals("Pergunta")) {
            builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if ((count + 1) <= (list.size() - 1)) {
                        exibirNotificacoes(list, (count + 1));
                    }
                }
            });
        }
        builder.create().show();
        list.get(count).put("lida", true);
        list.get(count).saveInBackground();
    }

    private void confirmarTrocaDeSenha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.questionmark_64);
        builder.setTitle("Pergunta");
        builder.setMessage("Tem certeza que deseja trocar sua senha?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final Dialog progresso = FuncoesParse.showProgressBar(context, "Enviando E-mail...");
                ParseUser.getCurrentUser().requestPasswordResetInBackground(ParseUser.getCurrentUser().getEmail().trim(),
                        new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                FuncoesParse.dismissProgressBar(progresso);
                                if (e == null) {
                                    funcoes.mostrarDialogAlert(1, "Um e-mail com as instruções para a troca da senha foi enviado.");
                                } else {
                                    funcoes.mostrarToast(2);
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //O objeto ParseObject referente ao time está salvo em uma variável global (Constantes.timeSelecionado).
        //Não é necessário enviar o time selecionado para as classes por parâmetro.
        if (Constantes.getTimeSelecionado() != null) {
            switch (requestCode) {
                case 1:
                    mudarTela(TimeDetalheActivity.class);
                    break;
                case 2:
                    mudarTela(CalendarioActivity.class);
                    break;
                case 3:
                    mudarTela(FinanceiroActivity.class);
                    break;
                case 4:
                    mudarTela(MovimentosActivity.class);
                    break;
                case 5:
                    mudarTela(MensalidadesActivity.class);
                    break;
            }
        } else if (requestCode == IMAGE_VIEW_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
                RoundedImageView arredondarImagem = new RoundedImageView(context);
                Bitmap bitArredondado = arredondarImagem.getCroppedBitmap(bitmap, 150);
                imgPerfilUusario.setImageBitmap(bitArredondado);
                FuncoesParse.inserirImagemPerfil(context, ParseUser.getCurrentUser(), bitArredondado);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
            final Dialog progresso = FuncoesParse.showProgressBar(this, "Fazendo logoff...");

            ParseInstallation.getCurrentInstallation().remove("User");
            ParseInstallation.getCurrentInstallation().saveEventually();

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    FuncoesParse.dismissProgressBar(progresso);
                    if (e == null) {
                        mudarTela(LoginActivity.class);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Sem conexão com internet. Você será desconectado automaticamente.", Toast.LENGTH_SHORT);
                        toast.show();
                        mudarTela(LoginActivity.class);
                    }
                }
            });
            return true;
        }

        if (id == R.id.action_sair) {
            this.finish();
            finish();
            return true;
        }
        return super.
                onOptionsItemSelected(item);

    }

    private void escolherImagemPerfil() {
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = "Tire agora ou selecione uma foto";
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        startActivityForResult(chooserIntent, IMAGE_VIEW_ACTIVITY_REQUEST_CODE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle parametros = new Bundle();
        if (funcoes.verificaConexao(context)) {
            if (id == R.id.main_estatisticas) {
                funcoes.mostrarDialogAlert(1, "Função ainda não implementada! Estará disponível nas próximas versões.");
            } else if (id == R.id.main_meuperfil) {
                parametros.putString("tipoAcesso", "edit");
                mudarTela(CadastroUsuarioActivity.class, parametros);
            } else if (id == R.id.main_config) {
                funcoes.mostrarDialogAlert(1, "Função ainda não implementada! Estará disponível nas próximas versões.");
            } else if (id == R.id.main_alterarsenha) {
                confirmarTrocaDeSenha();
            } else if (id == R.id.main_meustimes) {
                // funcoes.mostrarDialogAlert(1, "Função desabilitada temporariamente!");
                parametros.putBoolean("cadastrar", true);
                mudarTelaComRetorno(TeamActivity.class, parametros, 1);
            } else if (id == R.id.main_calendario) {
                //funcoes.mostrarDialogAlert(1, "Função desabilitada temporariamente!");
                parametros.putBoolean("cadastrar", false);
                mudarTelaComRetorno(TeamActivity.class, parametros, 2);
            } else if (id == R.id.main_caixa) {
                //funcoes.mostrarDialogAlert(1, "Função desabilitada temporariamente!");
                parametros.putBoolean("cadastrar", false);
                mudarTelaComRetorno(TeamActivity.class, parametros, 3);
            } else if (id == R.id.main_movimento) {
                //funcoes.mostrarDialogAlert(1, "Função desabilitada temporariamente!");
                parametros.putBoolean("cadastrar", false);
                mudarTelaComRetorno(TeamActivity.class, parametros, 4);
            } else if (id == R.id.main_mensalidade) {
                //funcoes.mostrarDialogAlert(1, "Função desabilitada temporariamente!");
                parametros.putBoolean("cadastrar", false);
                mudarTelaComRetorno(TeamActivity.class, parametros, 5);

            }
        } else {
            funcoes.mostrarDialogAlert(1, "Sem conexão com internet!");
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
    private void mudarTela(Class cls, ParseProxyObject obj) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("parseObject", obj);
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
