package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Posicao;
import br.com.sharkweb.fbv.model.TipoUsuario;
import br.com.sharkweb.fbv.model.Usuario;

public class CadastroUsuarioActivity extends ActionBarActivity {

    final Context context = this;

    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private EditText txtConfirmarSenha;

    private Spinner spnTipoUsuario;
    private Spinner spnPosicao;

    private Button btnCadastrar;
    private Button btnCancelar;

    private String tipoAcesso;

    private UsuarioController usuarioControl = new UsuarioController(this);
    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);

    private Funcoes funcoes = new Funcoes(this);
    Usuario user = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        txtNome = (EditText) findViewById(R.id.cadastro_usuario_edtNome);
        txtNome.setVisibility(EditText.VISIBLE);

        txtEmail = (EditText) findViewById(R.id.cadastro_usuario_edtEmail);
        txtEmail.setVisibility(EditText.VISIBLE);

        txtSenha = (EditText) findViewById(R.id.cadastro_usuario_edtSenha);
        txtSenha.setVisibility(EditText.VISIBLE);

        txtConfirmarSenha = (EditText) findViewById(R.id.cadastro_usuario_edtConfirmarSenha);
        txtConfirmarSenha.setVisibility(EditText.VISIBLE);

        spnTipoUsuario = (Spinner) findViewById((R.id.cadastro_usuario_spinner));
        ArrayList<TipoUsuario> tipo_usuario = tipoUsuarioControl.selectTiposUsuarios();
        ArrayList<String> tipo_usuarios = new ArrayList<>();

        for (int i = 0; i < tipo_usuario.size(); i++) {
            tipo_usuarios.add(tipo_usuario.get(i).getTipo().trim());
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.abc_list_menu_item_checkbox,
                tipo_usuarios);

        spnTipoUsuario.setAdapter(arrayAdapter2);
        spnTipoUsuario.setVisibility(View.VISIBLE);

        spnPosicao = (Spinner) findViewById((R.id.cadastro_usuario_spinner_posicao));
        ArrayList<Posicao> posicao = posicaoControl.selectPosicoes();
        ArrayList<String> posicoes = new ArrayList<>();

        for (int i = 0; i < posicao.size(); i++) {
            posicoes.add(posicao.get(i).getNome().trim());
        }
        //abc_list_menu_item_checkbox
        ArrayAdapter<String> arrayAdapterPosicao = new ArrayAdapter<String>(this, R.layout.abc_list_menu_item_checkbox,
                posicoes);
        spnPosicao.setAdapter(arrayAdapterPosicao);
        spnPosicao.setVisibility(View.VISIBLE);


        //final String spinVal = String.valueOf(spin.getSelectedItem());

        btnCadastrar = (Button) findViewById(R.id.cadastroUsuario_btnRegistrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inserir()) {
                    if (tipoAcesso.equals("edit")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cadastro atualizado com sucesso!", Toast.LENGTH_LONG);
                        toast.show();
                        mudarTela(MainActivity.class);
                    }else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                        toast.show();
                        mudarTela(LoginActivity.class);
                    }
                }
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastro_usuario_btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //mudarTela(LoginActivity.class);
                //funcoes.mostrarDialogAlert(0,"TESTE MAROTAO",String.valueOf(user.getId_tipo()));
                onBackPressed();
            }
        });
        btnCancelar.setVisibility(View.GONE);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela de cadastro de usuario
            tipoAcesso = params.getString("tipoAcesso");

            if (params.getInt("id_usuario") > 0) {
                carregarRegistro(params.getInt("id_usuario"));
            }
        }
    }

    private void carregarRegistro (int id_usuario){
       // ArrayList<Usuario> teste = usuarioControl.selectUsuarioPorId(id_usuario);
        this.user = usuarioControl.selectUsuarioPorId(id_usuario).get(0);

        txtNome.setText(this.user.getNome());
        txtSenha.setText(this.user.getSenha());
        txtConfirmarSenha.setText(this.user.getSenha());
        txtEmail.setText(this.user.getEmail());
        spnPosicao.setSelection(this.user.getId_posicao()-1);
        spnTipoUsuario.setSelection(this.user.getId_tipo() - 1);

        if (tipoAcesso.equals("edit")){
            btnCadastrar.setText("Atualizar");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
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

    private Boolean inserir() {
        String ret = validarCampos();
        if (ret.isEmpty()) {
            String nome = txtNome.getText().toString();
            String email = txtEmail.getText().toString();
            String senha = txtSenha.getText().toString();
            String codigo = "";
            int id_tipo = spnTipoUsuario.getSelectedItemPosition() + 1;
            int id_posicao = spnPosicao.getSelectedItemPosition() + 1;
            int id_time = 0;

            if (tipoAcesso.equals("edit")){
                usuarioControl.alterar(this.user.getId(), nome, codigo, email, senha, id_tipo, id_posicao, id_time);
            }else {
                usuarioControl.inserir(nome, codigo, email, senha, id_tipo, id_posicao, id_time);
            }
            return true;
        } else {
            funcoes.mostrarDialogAlert(0,"Informativo",ret);
            return false;
        }
    }

    private String validarCampos() {
        String retorno = "";
        if (txtNome.getText().toString().isEmpty()) {
            retorno = retorno + "Nome nao informado \n";
        }
        if (txtEmail.getText().toString().isEmpty()) {
            retorno = retorno + "E-mail nao informado \n";
        }

        //VERIFICA SE AS SENHAS DIGITADAS SÃO IGUAIS.
        String validacaoSenhas = usuarioControl.validarSenha(txtSenha.getText().toString(),
                txtConfirmarSenha.getText().toString());

        if (!validacaoSenhas.isEmpty()){
            retorno = retorno + validacaoSenhas.trim()+" \n";
        }

        if (!usuarioControl.selectUsuarioPorEmail(txtEmail.getText().
                toString()).isEmpty() && !tipoAcesso.equals("edit")){
            retorno = retorno + "Ja existe um usuario cadastrado com este endereco de e-mail. \n";
        }
        return retorno;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cadastro_usuario_action_cancelar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Pergunta");
            builder.setMessage("Tem certeza que deseja cancelar?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onBackPressed();
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
}
