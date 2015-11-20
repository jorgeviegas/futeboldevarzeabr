package br.com.sharkweb.fbv.DAOParse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.util.concurrent.ExecutionException;

/**
 * Created by Tiago on 28/10/2015.
 */

public class BuscarObjetoParse {

    private Context context;
    private ParseObject objeto;
    private ParseQuery objetoPesquisa;

    public BuscarObjetoParse(Context context, ParseObject obj, String tabela) {
        this.context = context;
        this.objeto = obj;
        this.objetoPesquisa = new ParseQuery(tabela);
    }

    public ParseObject salvar() {
        //Cria/Alterar um objeto
        //objeto.saveInBackground();
        objeto.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //Ok!
                } else {

                }
            }
        });
        return objeto;
    }

    public ParseObject buscar(String id) {
        //Busca o objeto pelo id_parse
        try {
            ParseObject objret = objetoPesquisa.get(id);
            return objret;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

