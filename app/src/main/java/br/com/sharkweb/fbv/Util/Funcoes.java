package br.com.sharkweb.fbv.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.controller.PosicaoController;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * Created by Tiago on 22/07/2015.
 */
public class Funcoes {

    private Context context = null;
    private PosicaoController posicaoControl;

    public Funcoes(Context contex) {
        context = contex;
        posicaoControl = new PosicaoController(contex);
    }

    public Funcoes() {

    }

    public void mostrarDialogAlert(int tipo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        switch (tipo) {
            case 1:
                builder.setIcon(R.drawable.information_64);
                builder.setMessage(mensagem).setTitle("Informativo");
                break;
            case 2:
                builder.setIcon(R.drawable.attention_64);
                builder.setMessage(mensagem).setTitle("Atenção!");
                break;
            case 3:
                builder.setIcon(R.drawable.imoticon_sorry);
                builder.setMessage("Isso não deveria ter acontecido, vamos trabalhar duro para que isso não se repita!" +
                        " \n" + mensagem).setTitle("Opss..");
                break;
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mostrarToast(int tipo) {
        Toast toast = Toast.makeText(this.context, "", Toast.LENGTH_LONG);
        switch (tipo) {
            case 1:
                toast.setText("Cadastro salvo com sucesso!");
                break;
            case 2:
                toast.setText("Falha ao salvar. Por favor, tente novamente.");
                break;
            case 3:
                toast.setText("Isso não deveria ter acontecido, vamos trabalhar duro para que isso não se repita!");
                break;
            case 4:
                toast.setText("Falha ao carregar. Por favor, tente novamente.");
                break;
            case 5:
                toast.setText("Falha ao excluir. Por favor, tente novamente.");
                break;
            case 6:
                toast.setText("Cadastro excluído com sucesso!");
                break;
        }
        toast.show();
    }

    public String PrimeiraLetraMaiuscula(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public Date transformarStringEmData(String data) throws Exception {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = (java.util.Date) formatter.parse(data);
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }

    public String transformarDataEmString(Date data) {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        String result = out.format(data);
        return result;
        // return data.getDay()+"/"+data.getMonth()+"/"+data.getYear();
    }

    public Date getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String result = dateFormat.format(date);
        try {
            return (Date) dateFormat.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String result = dateFormat.format(date);
        try {
            return (Date) dateFormat.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getFirstDayOfTheMonth(Date data) {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public Date getLastDayOfTheMonth(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public Date setDateTime(String data) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(data);
    }

    public long StringDataParaLong(String date) {

        String parts[] = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        //tem que testar
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }

    public String getDataDia() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String formatarHoraMinuto(int h) {
        if (h >= 10) {
            return String.valueOf(h);
        } else {
            return "0" + String.valueOf(h);
        }
    }

    public void exibirDetalheUsuario(ParseObject user, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_usuario_detalhe);
        dialog.setTitle(user.getString("nome").trim().toUpperCase());
        final EditText tvNumeroTelefone = (EditText) dialog.findViewById(R.id.usuario_detalhe_numerotelefone);
        final EditText tvEmail = (EditText) dialog.findViewById(R.id.usuario_detalhe_email);
        final EditText tvPosicao = (EditText) dialog.findViewById(R.id.usuario_detalhe_posicao);
        final EditText tvUsername = (EditText) dialog.findViewById(R.id.usuario_detalhe_username);
        //tvNumeroTelefone.setEnabled(false);
        //tvEmail.setEnabled(false);
        //tvPosicao.setEnabled(false);
        if (user.getString("celular") != null && !user.getString("celular").isEmpty()) {
            TextWatcher celularMask;
            celularMask = Mask.insert("(##)####-####", tvNumeroTelefone);
            tvNumeroTelefone.addTextChangedListener(celularMask);
            tvNumeroTelefone.setText(user.getString("celular").trim());
        } else {
            tvNumeroTelefone.setText("");
        }
        if (user.getString("posicao") != null && !user.getString("posicao").isEmpty()) {
            tvPosicao.setText(posicaoControl.selectPosicaoPorCodigo(user.getString("posicao").trim()).get(0).getNome());
        } else {
            tvPosicao.setText("");
        }
        tvEmail.setText(user.getString("email").trim());
        tvUsername.setText(user.getString("username").trim());
        dialog.show();
    }

    public String formatarNumeroComVirgula(double valor) {
        return String.valueOf(valor).format("%.2f", valor);
    }

    public String formatarNumeroComPonto(double valor) {
        return String.valueOf(valor).format("%.2f", valor).replace(",", ".");
    }

    public Date dataPorPeriodo(Date dat, int diaVencimento, int frequencia) {
        dat.setDate(diaVencimento);
        switch (frequencia) {
            case 0:
                //Mensal
                dat.setMonth(dat.getMonth() + 1);
                break;
            case 1:
                //Trimestral
                dat.setMonth(dat.getMonth() + 3);
                break;
            case 2:
                //Semestral
                dat.setMonth(dat.getMonth() + 6);
                break;
            case 3:
                //Anual
                dat.setMonth(dat.getMonth() + 12);
                break;
        }
        return dat;
    }

    /**
     * Verifica a disponibilidade da rede  de dados<br>
     * Tanto WIFI quanto 3G
     *
     * @return true ou false
     * @see android.net.ConnectivityManager
     */
    public static boolean verificaConexao(Context cont) {
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            conectado = true;
        }
        //Verifica o 3G
        else if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public void requestPasswordReset(String email) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Enviando e-mail...");
        ParseUser.requestPasswordResetInBackground(email.trim(), new RequestPasswordResetCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    mostrarDialogAlert(1, "Um e-mail com as instruções para a troca da senha foi enviado.");
                    // An email was successfully sent with reset instructions.
                } else {
                    if (e.getCode() == 125) {
                        mostrarDialogAlert(1, "Endereço de E-mail Inválido.");
                    } else if (e.getCode() == 205) {
                        mostrarDialogAlert(1, "Nenhum usuário cadastrado com este endereço de E-mail.");
                    } else {
                        mostrarToast(2);
                    }
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });
    }

    public String validarSenha(String senha, String confirmarSenha) {
        if (senha.equals("") || confirmarSenha.equals("")) {
            return "Senha inválida";
        }
        if (!senha.equals(confirmarSenha)) {
            return "Senha e Confirmar senha devem ser iguais.";
        }
        return "";
    }

    public void reEnviarEmailConfirmacao() {
        if (ParseUser.getCurrentUser().getEmail() == null ||
                ParseUser.getCurrentUser().getEmail().isEmpty()) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_pediremail);
            dialog.setTitle("Informe o E-mail:");
            final EditText txtEmail = (EditText) dialog.findViewById(R.id.dialog_pediremail_email);
            final Button btnconfirmar = (Button) dialog.findViewById(R.id.dialog_pediremail_btnconfirmar);
            btnconfirmar.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    if (!txtEmail.getText().toString().isEmpty()) {
                        enviarEmailConfirmacao(txtEmail.getText().toString().trim());
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            enviarEmailConfirmacao(ParseUser.getCurrentUser().getEmail().trim());
        }
    }

    public void enviarEmailConfirmacao(final String email) {
        //É necessário forçar uma mudança no email do usuário para re-enviar o email de confirmação.
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Enviando E-mail...");
        ParseUser.getCurrentUser().setEmail("");
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    ParseUser.getCurrentUser().setEmail(email.trim());
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                mostrarDialogAlert(1, "Um e-mail foi enviado para que você possa confirmar o mesmo.");
                            } else {
                                if (e.getCode() == 125) {
                                    mostrarDialogAlert(1, "Endereço de E-mail Inválido.");
                                } else if (e.getCode() == 205) {
                                    mostrarDialogAlert(1, "Nenhum usuário cadastrado com este endereço de E-mail.");
                                } else {
                                    mostrarToast(2);
                                }
                            }
                        }
                    });
                } else {
                    FuncoesParse.dismissProgressBar(progresso);
                    mostrarToast(2);
                }
            }
        });
    }
}
