package br.com.sharkweb.fbv;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.adapter.TimeListAdapter;
import br.com.sharkweb.fbv.controller.JogoController;
import br.com.sharkweb.fbv.controller.LocalController;
import br.com.sharkweb.fbv.controller.TimeController;
import br.com.sharkweb.fbv.controller.UsuarioController;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.Usuario;

import static android.app.TimePickerDialog.*;

public class CadastroJogoActivity extends ActionBarActivity {

    private ParseObject time;
    private ParseObject time2;
    private ParseObject local;
    private ParseObject juiz;

    private String data;
    private Funcoes funcoes = new Funcoes(this);
    private String tipoAcesso;
    private ParseObject jogo;
    final Context context = this;
    private LocalController localControl = new LocalController(this);

    private EditText tvTime1;
    private EditText tvTime2;
    private EditText tvData;
    private EditText tvHora;
    private EditText tvHorafinal;
    private EditText tvJuiz;
    private EditText tvLocal;
    private TextView tvEnderecoLocal;

    private Button btnSearchTime1;
    private Button btnSearchTime2;
    private Button btnSearchJuiz;
    private Button btnSearchLocal;
    private Button btnExcluirJuiz;

    private Button btnSalvar;
    private Button btnCancelar;
    private TimePicker timePicker;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    static final int TIME_DIALOG_IDFINAL = 2;
    private int hour;
    private int minute;
    private int horaSelecionada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_jogo);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            tipoAcesso = params.getString("tipoAcesso");

            if (tipoAcesso.equals("write")) {
                time = Constantes.getTimeSelecionado();
                data = params.getString("data");
                jogo = new ParseObject("jogo");
            } else {
                this.jogo = Constantes.getSessao().getObjeto();
                Constantes.setSessao(null);
            }

        } else {
            data = null;
            local = null;
        }

        tvTime1 = (EditText) findViewById(R.id.cadastrojogo_time1);
        tvTime1.setVisibility(EditText.VISIBLE);

        tvTime2 = (EditText) findViewById(R.id.cadastrojogo__time2);
        tvTime2.setVisibility(EditText.VISIBLE);

        tvEnderecoLocal = (TextView) findViewById(R.id.cadastro_jogo_enderecolocal);
        tvEnderecoLocal.setVisibility(EditText.VISIBLE);
        tvEnderecoLocal.setText("");

        tvData = (EditText) findViewById(R.id.cadastrojogo_data);
        tvData.setVisibility(EditText.VISIBLE);
        tvData.setFocusable(false);
        tvData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        tvHora = (EditText) findViewById(R.id.cadastrojogo_horaInicio);
        tvHora.setVisibility(EditText.VISIBLE);
        tvHora.setFocusable(false);
        tvHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                horaSelecionada = 1;
                showDialog(TIME_DIALOG_ID);
            }
        });

        tvHorafinal = (EditText) findViewById(R.id.cadastrojogo_horafinal);
        tvHorafinal.setVisibility(EditText.VISIBLE);
        tvHorafinal.setFocusable(false);
        tvHorafinal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                horaSelecionada = 2;
                showDialog(TIME_DIALOG_IDFINAL);
            }
        });

        tvJuiz = (EditText) findViewById(R.id.cadastro_jogo_juiz);
        tvJuiz.setVisibility(EditText.VISIBLE);
        tvJuiz.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && juiz != null) {
                    btnExcluirJuiz.setVisibility(View.VISIBLE);
                } else {
                    btnExcluirJuiz.setVisibility(View.GONE);
                }
            }
        });

        tvJuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (juiz != null)
                    btnExcluirJuiz.setVisibility(View.VISIBLE);
            }
        });

        tvLocal = (EditText) findViewById(R.id.cadastro_jogo_local);
        tvLocal.setVisibility(EditText.VISIBLE);

        btnSearchTime1 = (Button) findViewById(R.id.cadastrojogo_botaotime1);
        btnSearchTime1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheTime(1);
            }
        });

        btnSearchTime2 = (Button) findViewById(R.id.cadastrojogo_botaotime2);
        btnSearchTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheTime(2);
            }
        });

        btnSearchJuiz = (Button) findViewById(R.id.cadastro_jogo_btnjuiz);
        btnSearchJuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheUsuario(3);
            }
        });

        btnExcluirJuiz = (Button) findViewById(R.id.cadastro_jogo_btnexcluirjuiz);
        btnExcluirJuiz.setVisibility(View.GONE);
        btnExcluirJuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                limparJuiz();
            }
        });

        btnSearchLocal = (Button) findViewById(R.id.cadastro_jogo_btnlocal);
        btnSearchLocal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EscolheLocal(4);
            }
        });

        btnSalvar = (Button) findViewById(R.id.cadastrojogo_btnsalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvar();
            }
        });

        btnCancelar = (Button) findViewById(R.id.cadastrojogo_btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!tipoAcesso.equals("write")) {
            carregarRegistro();
        } else {
            tvData.setText(data);
            tvTime1.setText(time.getString("nome").trim().toUpperCase());
            btnSearchTime1.setEnabled(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (Constantes.getSessao() != null) {
                    this.time = Constantes.getSessao().getObjeto();
                    tvTime1.setText(this.time.getString("nome").trim().toUpperCase());
                    Constantes.setSessao(null);
                    tvTime1.setEnabled(false);
                } else {
                    tvTime1.setEnabled(true);
                    tvTime1.setText("");
                }
                break;
            case 2:
                if (Constantes.getSessao() != null) {
                    this.time2 = Constantes.getSessao().getObjeto();
                    tvTime2.setText(this.time2.getString("nome").trim().toUpperCase());
                    Constantes.setSessao(null);
                    tvTime2.setEnabled(false);
                } else {
                    tvTime2.setEnabled(true);
                    tvTime2.setText("");
                }
                break;
            case 3:
                //Usuario user = userControl.selectUsuarioPorId(id_usuario, "").get(0);
                //this.juiz = user;
                break;
            case 4:
                if (Constantes.getSessao() != null) {
                    this.local = Constantes.getSessao().getObjeto();
                    Constantes.setSessao(null);
                    tvLocal.setText(this.local.getString("nome"));
                    tvEnderecoLocal.setText(localControl.getEnderecoCompleto(this.local));
                } else {

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void EscolheTime(int key) {
        Bundle parametros = new Bundle();
        parametros.putBoolean("esperaRetorno", true);
        parametros.putBoolean("cadastrar", false);
        parametros.putBoolean("buscarTodosTimes", true);
        mudarTelaComRetorno(TeamActivity.class, parametros, key);
    }

    public void EscolheUsuario(int key) {
        Bundle parametros = new Bundle();
        parametros.putBoolean("esperaRetorno", true);
        mudarTelaComRetorno(UsuariosTimeActivity.class, parametros, key);
    }

    public void EscolheLocal(int key) {
        Bundle parametros = new Bundle();
        mudarTelaComRetorno(LocalActivity.class, parametros, key);
    }

    public void limparJuiz() {
        juiz = null;
        tvJuiz.setText("");
        btnExcluirJuiz.setVisibility(View.GONE);
    }

    private void carregarRegistro() {
        if (jogo != null) {
            if (jogo.getString("nomeJuiz") != null) {
                tvJuiz.setText(jogo.getString("nomeJuiz").trim().toUpperCase());
            }
            if (jogo.getString("enderecoCompletoLocal") != null) {
                tvEnderecoLocal.setText(jogo.getString("enderecoCompletoLocal").trim().toUpperCase());
            }
            if (jogo.getString("nomeLocal") != null){
                tvLocal.setText(jogo.getString("nomeLocal").trim().toUpperCase());
            }

            tvTime1.setText(jogo.getString("nomeTime").trim().toUpperCase());
            btnSearchTime1.setEnabled(false);
            tvTime2.setText(jogo.getString("nomeTime2").trim().toUpperCase());
            tvData.setText(funcoes.transformarDataEmString(jogo.getDate("data")).trim());
            tvHora.setText(jogo.getString("hora").trim());
            tvHorafinal.setText(jogo.getString("horaFinal").trim());
        }

        if (tipoAcesso.equals("read")) {
            tvHora.setEnabled(false);
            tvHorafinal.setEnabled(false);
            tvData.setEnabled(false);
            tvTime1.setEnabled(false);
            tvTime2.setEnabled(false);
            btnSearchTime1.setEnabled(false);
            btnSearchTime2.setEnabled(false);
            tvLocal.setEnabled(false);
            tvJuiz.setEnabled(false);
            btnSearchLocal.setEnabled(false);
            btnSearchJuiz.setEnabled(false);
            btnSalvar.setEnabled(false);
        }
    }

    private void salvar() {
        String validacao = validar();
        if (validacao.isEmpty()) {

            final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando jogo...");
            if (this.time != null) {
                this.jogo.put("time", this.time);
            }
            if (this.time2 != null) {
                this.jogo.put("time2", this.time2);
            }
            if (this.juiz != null) {
                this.jogo.put("juiz", this.juiz);
            }
            this.jogo.put("nomeTime", tvTime1.getText().toString().trim());
            this.jogo.put("nomeTime2", tvTime2.getText().toString().trim());
            this.jogo.put("nomeLocal", tvLocal.getText().toString().trim());
            this.jogo.put("nomeJuiz", tvJuiz.getText().toString().trim());
            this.jogo.put("enderecoCompletoLocal", tvEnderecoLocal.getText().toString().trim());
            try {
                this.jogo.put("data", funcoes.transformarStringEmData(tvData.getText().toString().trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.jogo.put("hora", tvHora.getText().toString().trim());
            this.jogo.put("horaFinal", tvHorafinal.getText().toString().trim());
            this.jogo.put("inativo", false);
            this.jogo.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Constantes.getTimeSelecionado().getRelation("jogos").add(jogo);
                        Constantes.getTimeSelecionado().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                FuncoesParse.dismissProgressBar(progresso);
                                if (e == null) {
                                    funcoes.mostrarToast(1);
                                    onBackPressed();
                                } else {
                                    funcoes.mostrarToast(2);
                                }
                            }
                        });
                    } else {
                        FuncoesParse.dismissProgressBar(progresso);
                        funcoes.mostrarToast(2);
                    }
                }
            });
        } else {
            funcoes.mostrarDialogAlert(1, validacao);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (id) {
            case DATE_DIALOG_ID:
                int ano = calendario.get(Calendar.YEAR);
                int mes = calendario.get(Calendar.MONTH);
                int dia = calendario.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);

            case TIME_DIALOG_ID:
                if (horaSelecionada == 1 && !tvHora.getText().toString().isEmpty()) {
                    minute = Integer.valueOf(tvHora.getText().subSequence(3, 5).toString());
                    hour = Integer.valueOf(tvHora.getText().subSequence(0, 2).toString());
                } else {
                    hour = sdf.getCalendar().getTime().getHours();
                    minute = sdf.getCalendar().getTime().getMinutes();
                }
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);

            case TIME_DIALOG_IDFINAL:
                if (horaSelecionada == 2 && !tvHorafinal.getText().toString().isEmpty()) {
                    minute = Integer.valueOf(tvHorafinal.getText().subSequence(3, 5).toString());
                    hour = Integer.valueOf(tvHorafinal.getText().subSequence(0, 2).toString());
                } else {
                    hour = sdf.getCalendar().getTime().getHours();
                    minute = sdf.getCalendar().getTime().getMinutes();
                }
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String data = String.valueOf(funcoes.formatarHoraMinuto(dayOfMonth)).trim()
                    + "/" + String.valueOf(funcoes.formatarHoraMinuto(monthOfYear)).trim() + "/" + String.valueOf(year);
            tvData.setText(data);
            tvData.clearFocus();
        }
    };

    private OnTimeSetListener timePickerListener = new OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            if (horaSelecionada == 1) {
                tvHora.setText(new StringBuilder().append(funcoes.formatarHoraMinuto(hour)).
                        append(":").append(funcoes.formatarHoraMinuto(minute)));
            } else if (horaSelecionada == 2) {
                tvHorafinal.setText(new StringBuilder().append(funcoes.formatarHoraMinuto(hour)).
                        append(":").append(funcoes.formatarHoraMinuto(minute)));
                tvHorafinal.clearFocus();
            }
            horaSelecionada = 0;
            //timePicker.setCurrentHour(hour);
            // timePicker.setCurrentMinute(minute);
        }
    };

    private String validar() {
        if (time == null && tvTime1.getText().toString().trim().isEmpty()) {
            return "Ops.. Faltou informar quem é o time da casa!";
        }
        if (time2 == null && tvTime2.getText().toString().trim().isEmpty()) {
            return "Ops.. Faltou informar quem é o time visitante!";
        }
        if (tvData.getText().toString().trim().equals("")) {
            return "Hummmm... Faltou informar a data do nosso jogo!";
        }
        if (tvHora.getText().toString().trim().equals("") ||
                tvHorafinal.getText().toString().trim().equals("")) {
            return "Hummmm... Faltou informar o horario do nosso jogo!";
        }
        if (local == null) {
            return "Ops... Faltou informar o local do nosso jogo!";
        }
        /*if (Double.valueOf(String.valueOf(tvHorafinal.getText()).replace(":","")) >
                Double.valueOf(String.valueOf(tvHora.getText()).replace(":",""))){
            return "Ops... A hora final não pode ser maior que a inicial!";
        }*/
        return "";
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.cadastro_jogo_action_excluir);
        MenuItem m2 = menu.findItem(R.id.cadastro_jogo_action_abrirposjogo);
        if (this.jogo != null) {
            m1.setVisible(true);
            m2.setVisible(true);
        } else {
            m1.setVisible(false);
            m2.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_jogo, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
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

        if (id == R.id.cadastro_jogo_action_excluir) {
            // jogoControl.excluirJogoPorId(this.jogo.getId());
            onBackPressed();
            return true;
        }

        //CHAMANDO TELA DE PÓS JOGO
        if (id == R.id.cadastro_jogo_action_abrirposjogo) {
            //funcoes.mostrarDialogAlert(1, "Está quase pronto! Estamos com essa função no forno!");
            //Bundle parametros = new Bundle();
            //parametros.putInt("id_jogo", jogo.getId());
            //mudarTela(PosJogoActivity.class, parametros);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTela(Class cls, Bundle parametros) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private void mudarTelaComRetorno(Class cls, Bundle parametros, int key) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(parametros);
        startActivityForResult(intent, key);
    }


    @SuppressWarnings("rawtypes")
    private void mudarTela(Class cls) {
        startActivity(new Intent(this, cls));
    }
}
