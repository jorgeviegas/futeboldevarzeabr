package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.Util.Mask;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Posicao;
import br.com.sharkweb.fbv.model.TipoUsuario;

public class CadastroUsuarioActivity extends ActionBarActivity {

    final Context context = this;
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfirmarSenha;
    private EditText txtCelular;
    private EditText txtApelido;
    private EditText txtPosicao;
    private Spinner spnTipoUsuario;
    private Spinner spnPosicao;
    private Button btnCadastrar;
    private Button btnCancelar;
    private Button btnPesquisarPosicao;
    private TextWatcher celularMask;
    private String tipoAcesso;
    private Posicao posicao;

    private UsuarioController usuarioControl = new UsuarioController(this);
    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);
    private Funcoes funcoes = new Funcoes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtNome = (EditText) findViewById(R.id.cadastro_usuario_edtNome);
        txtNome.setVisibility(EditText.VISIBLE);

        txtEmail = (EditText) findViewById(R.id.cadastro_usuario_edtEmail);
        txtEmail.setVisibility(EditText.VISIBLE);

        txtSenha = (EditText) findViewById(R.id.cadastro_usuario_edtSenha);
        txtSenha.setVisibility(EditText.VISIBLE);

        txtConfirmarSenha = (EditText) findViewById(R.id.cadastro_usuario_edtConfirmarSenha);
        txtConfirmarSenha.setVisibility(EditText.VISIBLE);

        txtApelido = (EditText) findViewById(R.id.cadastro_usuario_edtApelido);
        txtApelido.setVisibility(EditText.VISIBLE);

        txtPosicao = (EditText) findViewById(R.id.cadastroUsuario_posicao);
        txtPosicao.setVisibility(EditText.VISIBLE);

        txtCelular = (EditText) findViewById(R.id.cadastro_usuario_edtCelular);
        txtCelular.setVisibility(EditText.VISIBLE);
        celularMask = Mask.insert("(##)####-####", txtCelular);
        txtCelular.addTextChangedListener(celularMask);

        spnTipoUsuario = (Spinner) findViewById((R.id.cadastro_usuario_spinner));
        ArrayList<TipoUsuario> tipo_usuario = tipoUsuarioControl.selectTiposUsuarios();
        ArrayList<String> tipo_usuarios = new ArrayList<>();

        for (int i = 0; i < tipo_usuario.size(); i++) {
            tipo_usuarios.add(tipo_usuario.get(i).getTipo().trim());
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                tipo_usuarios);

        spnTipoUsuario.setAdapter(arrayAdapter2);
        spnTipoUsuario.setSelection(1);
        spnTipoUsuario.setVisibility(View.VISIBLE);

       /*  spnPosicao = (Spinner) findViewById((R.id.cadastro_usuario_posicao));
        ArrayList<Posicao> posicao = posicaoControl.selectPosicoes();
        ArrayAdapter<Posicao> arrayAdapterPosicao = new ArrayAdapter<Posicao>(this, android.R.layout.simple_list_item_activated_1,
                posicao);
        spnPosicao.setAdapter(arrayAdapterPosicao);
        spnPosicao.setSelection(0);
        spnPosicao.setVisibility(View.VISIBLE);*/

        btnCadastrar = (Button) findViewById(R.id.cadastroUsuario_btnRegistrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnPesquisarPosicao = (Button) findViewById(R.id.cadastro_usuario_btnPesquisarPosicao);
        btnPesquisarPosicao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.questionmark_64);
                final ArrayList<Posicao> posicoes = posicaoControl.selectPosicoes();
                String[] arrayOpcoes = new String[posicoes.size()];
                for (int i = 0; i < posicoes.size(); i++) {
                    arrayOpcoes[i] = posicoes.get(i).getAbreviatura() + " - " +
                            posicoes.get(i).getNome();
                }
                builder.setItems(arrayOpcoes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        posicao = posicoes.get(arg1);
                        txtPosicao.setText(posicao.getNome().trim());
                    }
                });
                AlertDialog dialogExportar = builder.create();
                dialogExportar.show();
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastro_usuario_btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnCancelar.setVisibility(View.VISIBLE);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela de cadastro de usuario
            tipoAcesso = params.getString("tipoAcesso");
            if (tipoAcesso.equals("edit") || tipoAcesso.equals("read"))
                carregarRegistro();
            else
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String teste = tm.getLine1Number();
                    txtCelular.setText(tm.getLine1Number());
                } catch (Exception e) {
                    // funcoes.mostrarDialogAlert(3, e.getMessage());
                }
        }
    }

    private void carregarRegistro() {

        txtNome.setText(ParseUser.getCurrentUser().get("nome").toString().trim());
        txtSenha.setText("");
        txtConfirmarSenha.setText("");
        txtSenha.setVisibility(View.GONE);
        txtConfirmarSenha.setVisibility(View.GONE);
        txtEmail.setText(ParseUser.getCurrentUser().getEmail().trim());
        spnTipoUsuario.setSelection(Integer.valueOf(ParseUser.getCurrentUser().get("id_tipo").toString()) - 1);
        txtApelido.setText(ParseUser.getCurrentUser().getUsername().trim());
        txtCelular.setText(ParseUser.getCurrentUser().get("celular").toString().trim());
        if (ParseUser.getCurrentUser().getString("posicao") != null
                && !ParseUser.getCurrentUser().getString("posicao").isEmpty()) {
            txtPosicao.setText(
                    posicaoControl.selectPosicaoPorCodigo(
                            ParseUser.getCurrentUser().getString("posicao").trim()).get(0).getNome());
        }

        txtSenha.setEnabled(false);
        txtConfirmarSenha.setEnabled(false);

        if (tipoAcesso.equals("read")) {
            txtNome.setEnabled(false);
            txtApelido.setEnabled(false);
            txtEmail.setEnabled(false);
            txtCelular.setEnabled(false);
            spnTipoUsuario.setEnabled(false);
            btnCadastrar.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
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

    private void salvar() {
        String ret = validarCampos();
        if (ret.isEmpty()) {
            final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando cadastro...");
            ParseUser user = new ParseUser();
            if (ParseUser.getCurrentUser() != null) {
                ParseUser.getCurrentUser().setUsername(txtApelido.getText().toString().toLowerCase());
                ParseUser.getCurrentUser().setEmail(txtEmail.getText().toString().toLowerCase());
                ParseUser.getCurrentUser().put("celular", Mask.unmask(txtCelular.getText().toString()));
                ParseUser.getCurrentUser().put("id_tipo", (spnTipoUsuario.getSelectedItemPosition() + 1));
                ParseUser.getCurrentUser().put("nome", txtNome.getText().toString());
                if (posicao != null) {
                    ParseUser.getCurrentUser().put("posicao", posicao.getAbreviatura());
                }
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            cadastroEfetuado();
                        } else {
                            falhaNoCadastro(e);
                        }
                        FuncoesParse.dismissProgressBar(progresso);
                    }
                });
            } else {
                user.setPassword(txtSenha.getText().toString());
                user.setUsername(txtApelido.getText().toString().toLowerCase());
                user.setEmail(txtEmail.getText().toString().toLowerCase());
                if (posicao != null) {
                    user.put("posicao", posicao.getAbreviatura());
                }
                user.put("celular", Mask.unmask(txtCelular.getText().toString()));
                user.put("id_tipo", (spnTipoUsuario.getSelectedItemPosition() + 1));
                user.put("nome", txtNome.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            cadastroEfetuado();
                        } else {
                            falhaNoCadastro(e);
                        }
                        FuncoesParse.dismissProgressBar(progresso);
                    }
                });
            }
        } else {
            funcoes.mostrarDialogAlert(1, ret);
        }
    }

    private void cadastroEfetuado() {
        funcoes.mostrarToast(1);
        if (ParseUser.getCurrentUser() == null) {
            funcoes.mostrarDialogAlert(1, "Por favor, confirme seu endereço de e-mail.");
            mudarTela(LoginActivity.class);
        } else {
            mudarTela(NewMainActivity.class);
        }
    }

    private void falhaNoCadastro(com.parse.ParseException e) {
        if (e.getCode() == 202) {
            funcoes.mostrarDialogAlert(1, "Ops... Esse nome de usuario já está em uso.");
        } else if (e.getCode() == 203) {
            funcoes.mostrarDialogAlert(1, "Ops... Esse endereço de e-mail já está em uso.");
        } else {
            funcoes.mostrarToast(2);
        }
    }

    private String validarCampos() {

        if (txtNome.getText().toString().isEmpty()) {
            return "Nome não informado.";
        }
        if (txtEmail.getText().toString().isEmpty()) {
            return "E-mail não informado.";
        } else {
            if (!usuarioControl.validarEmail(txtEmail.getText().toString().trim())) {
                return "Endereço de e-mail inválido.";
            }
        }
        if (txtApelido.getText().toString().isEmpty()) {
            return "Nome de usuário não informado.";
        }

        //VERIFICA SE AS SENHAS DIGITADAS SÃO IGUAIS.
        String validacaoSenhas = usuarioControl.validarSenha(txtSenha.getText().toString(),
                txtConfirmarSenha.getText().toString());

        if (!validacaoSenhas.isEmpty()) {
            return validacaoSenhas.trim();
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
