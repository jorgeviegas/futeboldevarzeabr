package br.com.sharkweb.fbv.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;

/**
 * Created by Tiago on 27/01/2016.
 */
public class NotificacaoController {

    private Context context;
    private TimeController timeControl;
    private UsuarioController userControl;
    private Funcoes funcoes;

    public NotificacaoController(Context context) {
        this.context = context;
        this.timeControl = new TimeController(context);
        this.userControl = new UsuarioController(context);
        this.funcoes = new Funcoes(context);
    }

    public void enviarConvite(final ParseObject usuario, final ParseObject time, String tipo) {
        if (tipo.equals("conviteTime")) {
            enviarConvite(usuario, time, "Olá! O " + time.getString("nome").trim().toUpperCase()
                    + " quer você no time! Clique aqui para responder." +
                    " Clique para visualizar", tipo, "FBA - " + time.getString("nome").toUpperCase());
        } else if (tipo.equals("conviteAdmin")) {
            enviarConvite(usuario, time, "Olá! O " + time.getString("nome").trim().toUpperCase()
                    + " quer você como administrador do time! Clique aqui para responder." +
                    " Clique para visualizar", tipo, "FBA - " + time.getString("nome").toUpperCase());
        }
    }

    public void receberNotificacaoConvite(Bundle mBundle) {
        String mData = mBundle.getString("com.parse.Data");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(mData);
            String tipo = jsonObject.getString("tipo");
            if (tipo != null && tipo.contains("convite")) {
                final String time = jsonObject.getString("time");
                String nomeTime = jsonObject.getString("nomeTime");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.questionmark_64);
                builder.setTitle("Convite");
                builder.setCancelable(false);
                if (tipo.equals("conviteTime")) {
                    builder.setMessage("O " + nomeTime.trim() +
                            "quer você no time. Deseja aceitar?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            timeControl.atualizarTime(time.trim());
                            dialog.dismiss();
                        }
                    });
                } else if (tipo.equals("conviteAdmin")) {
                    builder.setMessage("O " + nomeTime.trim() +
                            "quer você como administrador do time. Deseja aceitar?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            userControl.alterarConfigTime(time.trim(), "tipoUusario", "1");
                            dialog.dismiss();
                        }
                    });
                }
                builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void enviarConvite(final ParseObject usuario, final ParseObject time, String mensagem, String tipo, String title) {
        final Dialog progresso = FuncoesParse.showProgressBar(this.context, "Enviando convite...");
        ParsePush parsePush = new ParsePush();
        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("User", usuario);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("alert", mensagem.trim());
            jsonObj.put("title", title.trim());
            jsonObj.put("tipo", tipo);
            jsonObj.put("time", time.getObjectId().trim());
            jsonObj.put("nomeTime", time.getString("nome").trim());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        parsePush.setQuery(pQuery);
        parsePush.setData(jsonObj);
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    funcoes.mostrarToast(8);
                } else {
                    funcoes.mostrarToast(9);
                }
            }
        });
    }

    public void enviarNotificacaoDeJogoParaUsuariosDoTime(final ParseQuery query, ParseObject jogo) {
        final Dialog progresso = FuncoesParse.showProgressBar(this.context, "Enviando notificação...");
        ParsePush parsePush = new ParsePush();
        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereMatchesQuery("User", query);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("alert", "Olá! Seu time está com jogo marcado!\n" +
                    "Data: " + funcoes.transformarDataEmString(jogo.getDate("data")).trim() + " - " +
                    jogo.getString("hora").trim());
            jsonObj.put("title", "FBA - " + jogo.getString("nomeTime").trim());
            jsonObj.put("tipo", "jogo");

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        parsePush.setQuery(pQuery);
        parsePush.setData(jsonObj);
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    funcoes.mostrarToast(9);
                } else {
                    funcoes.mostrarToast(10);
                }
            }
        });
    }
}
