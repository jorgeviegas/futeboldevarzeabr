package br.com.sharkweb.fbv.controller;

import android.app.Dialog;
import android.content.Context;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.DAOParse.TimeDAOParse;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.model.Time;

public class TimeController {

    private Context context;
    private Funcoes funcoes;

    public TimeController(Context context) {
        this.context = context;
        funcoes = new Funcoes(context);
    }

    public void atualizarTime(String objectId) {
        final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");
        ParseQuery queryTime = new ParseQuery("time");
        queryTime.getInBackground(objectId.trim(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ParseUser.getCurrentUser().getRelation("times").add(parseObject);
                    ArrayList<String> configs = new ArrayList<String>();
                    configs.add(parseObject.getObjectId().trim());
                    configs.add("0");
                    configs.add("2");
                    ParseUser.getCurrentUser().add("configTimes", configs);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                funcoes.mostrarToast(1);
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
    }
}