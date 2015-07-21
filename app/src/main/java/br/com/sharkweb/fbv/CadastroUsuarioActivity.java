package br.com.sharkweb.fbv;

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

import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Posicao;
import br.com.sharkweb.fbv.model.Tipo_usuario;


public class CadastroUsuarioActivity extends ActionBarActivity {

    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private Spinner spnTipoUsuario;
    private Spinner spnPosicao;

    private Button btnCadastrar;
    private UsuarioController usuarioControl = new UsuarioController(this);
    private TipoUsuarioController tipoUsuarioControl = new TipoUsuarioController(this);
    private PosicaoController posicaoControl = new PosicaoController(this);

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

        spnTipoUsuario = (Spinner) findViewById((R.id.cadastro_usuario_spinner));
        ArrayList<Tipo_usuario> tipo_usuario = tipoUsuarioControl.selectTiposUsuarios();
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
        ArrayAdapter<String> arrayAdapterPosicao = new ArrayAdapter<String>(this, R.layout.abc_list_menu_item_checkbox,
                posicoes);
        spnPosicao.setAdapter(arrayAdapterPosicao);
        spnPosicao.setVisibility(View.VISIBLE);


        //final String spinVal = String.valueOf(spin.getSelectedItem());

        btnCadastrar = (Button) findViewById(R.id.cadastroUsuario_btnRegistrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inserir()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_LONG);
                    mudarTela(LoginActivity.class);
                }

            }
        });
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
        if (validarCampos().isEmpty()) {
            String nome = txtNome.getText().toString();
            String email = txtEmail.getText().toString();
            String senha = txtSenha.getText().toString();
            String codigo = "";
            int id_tipo = spnTipoUsuario.getSelectedItemPosition() + 1;
            int id_posicao = spnTipoUsuario.getSelectedItemPosition() + 1;
            int id_time = 0;

            usuarioControl.inserir(nome, codigo, email, senha, id_tipo, id_posicao, id_time);
            return true;
        } else {
            return false;
        }
    }

    private String validarCampos() {
        String retorno = "";
        if (txtNome.getText().toString().isEmpty()) {
            retorno = retorno + "Nome nao informado \n";
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
