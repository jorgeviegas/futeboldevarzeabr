package br.com.sharkweb.fbv.DAOParse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.Util.FuncoesParse;

/**
 * Created by Tiago on 29/10/2015.
 */
public class BuscarArrayObjetosParse {

    private Context context;
    private ParseQuery objetoPesquisa;
    private ProgressDialog progress;

    public BuscarArrayObjetosParse(Context context, String tabela) {
        this.context = context;
        this.objetoPesquisa = new ParseQuery(tabela);

        progress = new ProgressDialog(context);
        progress.setTitle("Aguarde");
        progress.setMessage("Buscando...");
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public ArrayList<ParseObject> buscarTimes(String... params) {
        //Retorna um arrayList de ParseObject
        ArrayList<ParseObject> retorno = new ArrayList<ParseObject>();
        if (params[3] != null && Integer.valueOf(params[3]) > 0) {
            objetoPesquisa.setLimit(Integer.valueOf(params[3]));
        }
        if (!params[1].isEmpty() || !params[2].isEmpty()) {
            objetoPesquisa.whereEqualTo(params[1].trim(), params[2].trim());
        }
        List<ParseObject> results = null;
        try {
            results = objetoPesquisa.find();
            if (results.size() > 0)
                retorno = FuncoesParse.listToArray(results);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retorno;
    }

}
