package br.com.sharkweb.fbv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Caixa;

/**
 * Created by Jorge on 30/09/2015.
 */
public class FinanceiroActivity extends AppCompatActivity {

    final Context context = this;
    private Funcoes funcoes = new Funcoes(this);
    private double valor = 0;
    private Caixa caixa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        caixa = new Caixa();
        caixa.setSaldo(10);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final TextView txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        Button btnRetirada = (Button) findViewById(R.id.btnRetirada);
        Button btnEntrada = (Button) findViewById(R.id.btnEntrada);

        txtSaldo.setText("Saldo: R$ " + caixa.getSaldo());

        btnRetirada.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nova Retirada");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        valor = Double.valueOf(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                caixa.setSaldo(caixa.getSaldo() - valor);
                txtSaldo.invalidate();
            }
        });
        /*
        txtApelido = (EditText) findViewById(R.id.cadastro_usuario_edtApelido);
        txtApelido.setVisibility(EditText.VISIBLE);

        txtCelular = (EditText) findViewById(R.id.cadastro_usuario_edtCelular);
        txtCelular.setVisibility(EditText.VISIBLE);
        celularMask = Mask.insert("(##)####-####", txtCelular);
        txtCelular.addTextChangedListener(celularMask);

        try {
            TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String teste = tm.getLine1Number();
            txtCelular.setText(tm.getLine1Number());
        }catch (Exception e){
            System.out.println("Falha ao tentar pegar o telefone automaticamente");
            funcoes.mostrarDialogAlert(3,e.getMessage());
        }



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

       *//* spnPosicao = (Spinner) findViewById((R.id.cadastro_usuario_spinner_posicao));
        ArrayList<Posicao> posicao = posicaoControl.selectPosicoes();
        ArrayList<String> posicoes = new ArrayList<>();

        for (int i = 0; i < posicao.size(); i++) {
            posicoes.add(posicao.get(i).getNome().trim());
        }
        //abc_list_menu_item_checkbox
        ArrayAdapter<String> arrayAdapterPosicao = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                posicoes);
        spnPosicao.setAdapter(arrayAdapterPosicao);
        spnPosicao.setVisibility(View.VISIBLE);*//*

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
        btnCancelar.setVisibility(View.VISIBLE);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            //Aqui tratamos parametros enviados para a tela de cadastro de usuario
            tipoAcesso = params.getString("tipoAcesso");

            if (params.getInt("id_usuario") > 0) {
                carregarRegistro(params.getInt("id_usuario"));
            }
        }*/
    }

}
