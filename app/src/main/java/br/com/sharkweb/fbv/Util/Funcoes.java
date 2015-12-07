package br.com.sharkweb.fbv.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.net.ConnectivityManager;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * Created by Tiago on 22/07/2015.
 */
public class Funcoes {

    private Context context = null;

    public Funcoes(Context contex) {
        context = contex;
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

    public void exibirDetalheUsuario(Usuario user, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_usuario_detalhe);
        dialog.setTitle(user.getNome().trim().toUpperCase());
        final TextView tvNumeroTelefone = (TextView) dialog.findViewById(R.id.usuario_detalhe_numerotelefone);
        final TextView tvEmail = (TextView) dialog.findViewById(R.id.usuario_detalhe_infoemail);

        //celularMask = Mask.insert("(##)####-####", tvNumeroTelefone);

        tvNumeroTelefone.setText(user.getCelular().trim());
        tvEmail.setText(user.getEmail().trim());
        //exibe na tela o dialog
        dialog.show();
    }

    public String formatarNumeroComVirgula(double valor) {
        return String.valueOf(valor).format("%.2f", valor);
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

}
