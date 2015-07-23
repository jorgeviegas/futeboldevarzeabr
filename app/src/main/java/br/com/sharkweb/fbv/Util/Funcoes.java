package br.com.sharkweb.fbv.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;

/**
 * Created by Tiago on 22/07/2015.
 */
public class Funcoes {

    private static Context context = null;

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
}
