package br.com.sharkweb.fbv.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    public static void enviarConviteTime(String username) {
       /* ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", username);
        installation.saveInBackground();

        ParsePush parsePush = new ParsePush();
        ParseQuery pQuery = ParseIntallation.query(); // <-- Installation query
        pQuery.whereEqualTo("username", currentUser.getUsername()); // <-- you'll probably want to target someone that's not the current user, so modify accordingly
        parsePush.sendMessageInBackground("Only for special people", pQuery);*/
    }
}
