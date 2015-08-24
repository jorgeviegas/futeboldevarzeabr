package br.com.sharkweb.fbv.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public void mostrarDialogAlert(int icone, String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setIcon(icone);
        builder.setMessage(mensagem).setTitle(titulo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String PrimeiraLetraMaiuscula(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    public Date transformarStringEmData(String data) throws Exception {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = (java.util.Date)formatter.parse(data);
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }

    public String transformarDataEmString (Date data){
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        String result = out.format(data);
        return result;
       // return data.getDay()+"/"+data.getMonth()+"/"+data.getYear();
    }

    public long StringDataParaLong (String date){

        String parts[] = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        //tem que testar
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTimeInMillis();
    }

    public String getDataDia (){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String formatarHoraMinuto(int h){
        if (h >=10){
            return String.valueOf(h);
        }else{
            return "0"+String.valueOf(h);
        }
    }
}
