package br.com.sharkweb.fbv.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tiago on 28/10/2015.
 */
public class FuncoesParse {

    public FuncoesParse() {

    }

    public static ArrayList<ParseObject> listToArray(List<ParseObject> listParse) {
        ArrayList<ParseObject> retorno = new ArrayList<ParseObject>();
        for (int i = 0; i < listParse.size(); i++) {
            ParseObject p = listParse.get(i);
            retorno.add(p);
        }
        return retorno;
    }

    public static Dialog showProgressBar(Context context, String msg) {
        Dialog progressDialog;
        progressDialog = ProgressDialog.show(context, "", msg, true);
        return progressDialog;
    }

    public static void dismissProgressBar(Dialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
