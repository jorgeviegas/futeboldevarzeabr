package br.com.sharkweb.fbv.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;

import java.text.ParseException;
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

    public static boolean isAdmin() {
        boolean retorno = false;
        ArrayList<String> configsTimes = (ArrayList<String>) ParseUser.getCurrentUser().get("configTimes");
        if (configsTimes != null && configsTimes.size() > 0) {
            for (int i = 0; i < configsTimes.size(); i++) {
                Object object = (Object) configsTimes.get(i);
                String time = ((ArrayList<String>) object).get(0);
                if (Constantes.getTimeSelecionado().getObjectId().equals(time)
                        && ((ArrayList<String>) object).get(2).equals("1")) {
                    retorno = true;
                }
            }
        } else {
            return false;
        }
        return retorno;
    }

    public static boolean isInativo(ParseObject user) {
        boolean retorno = false;
        ArrayList<String> configsTimes = (ArrayList<String>) user.get("configTimes");
        if (configsTimes != null && configsTimes.size() > 0) {
            for (int i = 0; i < configsTimes.size(); i++) {
                Object object = (Object) configsTimes.get(i);
                String inativo = ((ArrayList<String>) object).get(1);
                if (Integer.valueOf(inativo) == 1) {
                    retorno = true;
                }
            }
        } else {
            return false;
        }
        return retorno;
    }

    public static void enviarNotificacao(final Context context, ParseObject usuario, String mensagem, String objectId, String tipo) {
        // final Dialog progresso = FuncoesParse.showProgressBar(context, "Enviando notificação...");
        final ParseObject notific = new ParseObject("notificacao");
        notific.put("mensagem", mensagem);
        notific.put("objectIdParam", objectId.trim());
        notific.put("usuario", usuario);
        notific.put("tipo", tipo.trim());
        notific.put("lida", false);
        notific.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                // FuncoesParse.dismissProgressBar(progresso);
                if (e == null) {
                    Toast toast = Toast.makeText(context, "Notificação enviada ao usuário.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    notific.saveEventually();
                    Toast toast = Toast.makeText(context, "Falha ao enviar a notificação ao usuário.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public static void excluirNotificacao(final Context context, ParseObject usuario, String objectId, String tipo) {
        // final Dialog progresso = FuncoesParse.showProgressBar(context, "Enviando notificação...");
        ParseQuery query = new ParseQuery("notificacao");
        query.whereEqualTo("usuario", usuario);
        query.whereEqualTo("tipo", tipo);
        query.whereEqualTo("objectIdParam", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (e==null){
                    parseObject.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Toast toast = Toast.makeText(context, "Notificação excluída com sucesso.", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                parseObject.saveEventually();
                            }
                        }
                    });
                }else {

                }

            }
        });
    }
}
