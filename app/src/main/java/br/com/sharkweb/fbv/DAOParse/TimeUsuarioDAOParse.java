package br.com.sharkweb.fbv.DAOParse;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.sharkweb.fbv.DAO.FBVDAO;
import br.com.sharkweb.fbv.controller.TipoUsuarioController;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;

public class TimeUsuarioDAOParse {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "time_usuario";
    private static final String ID = "_id";
    private static final String ID_TIME = "id_time";
    private static final String ID_USUARIO = "id_usuario";
    private static final String INATIVO = "inativo";
    private static final String POSICAO = "posicao";
    private static final String ID_TIPO_USUARIO = "id_tipo_usuario";
    private TipoUsuarioController tipoUsuarioControl;
    ParseObject timeUsuarioParse;
    ParseQuery timeUsuarioParsePesquisa;
    private Context context;

    public TimeUsuarioDAOParse(Context context) {
        this.tipoUsuarioControl = new TipoUsuarioController(context);
        this.timeUsuarioParse = new ParseObject("time_usuario");
        this.timeUsuarioParsePesquisa = new ParseQuery("time_usuario");
        this.context = context;
    }

    public ParseObject salvar(TimeUsuario timeUsuario) {
        timeUsuarioParse.put(ID_TIME, timeUsuario.getId_time());
        timeUsuarioParse.put(ID_USUARIO, timeUsuario.getId_usuario());
        timeUsuarioParse.put(INATIVO, timeUsuario.getInativo());
        timeUsuarioParse.put(POSICAO, timeUsuario.getPosicao());
        timeUsuarioParse.put(ID_TIPO_USUARIO, timeUsuario.getId_tipo_usuario());

        try {
            timeUsuarioParse.save();
        } catch (ParseException e) {
            e.printStackTrace();
            timeUsuarioParse.revert();
        }
        return timeUsuarioParse;
    }

    public ArrayList<TimeUsuario> buscarTimesUsuario(String id_usario) {
        ArrayList<TimeUsuario> retorno = new ArrayList<>();
        BuscarTimesUsuarios busca = new BuscarTimesUsuarios(context);
        AsyncTask<String, Void, ArrayList<TimeUsuario>> ret = busca.execute("id_usuario", id_usario, String.valueOf(20));
        try {
            retorno = ret.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private ArrayList<TimeUsuario> listToArray(List listTimes) {
        ArrayList<TimeUsuario> times = new ArrayList<TimeUsuario>();
        for (int i = 0; i < listTimes.size(); i++) {
            ParseObject p = (ParseObject) listTimes.get(i);
            times.add(ParseObjectToTimeObject(p));
        }
        return times;
    }

    private TimeUsuario ParseObjectToTimeObject(ParseObject p) {
        String objectId = p.getObjectId();
        String id_time = p.getString("id_time").trim();
        String id_usuario = p.getString("id_usuario").trim();
        int inativo = p.getInt("inativo");
        String posicao = p.getString("posicao").trim();
        int id_tipo_usuario = p.getInt("id_tipo_usuario");

        TimeUsuario timeUsuario = new TimeUsuario(id_time,id_usuario,inativo,posicao,id_tipo_usuario,objectId);
        return timeUsuario;
    }

    /**
     * @author Tiago Klein
     *         Classe privada de AsyncTask expecífica para busca de TimeUsuario.
     */
    private class BuscarTimesUsuarios extends AsyncTask<String, Void, ArrayList<TimeUsuario>> {

        private ProgressDialog progress;
        private Context context;

        public BuscarTimesUsuarios(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle("Buscando Time...");
            progress.setMessage("Aguarde...");
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected ArrayList<TimeUsuario> doInBackground(String... params) {
            final ArrayList<TimeUsuario> timeUsuarios = new ArrayList<TimeUsuario>();
            if (params[2] != null && Integer.valueOf(params[2]) > 0) {
                timeUsuarioParsePesquisa.setLimit(Integer.valueOf(params[2]));
            }
            if (!params[0].isEmpty() || !params[1].isEmpty()) {
                timeUsuarioParsePesquisa.whereEqualTo(params[0].trim(), params[1].trim());
            }
            List<ParseObject> results = null;
            try {
                results = timeUsuarioParsePesquisa.find();
                if (results.size() > 0)
                    timeUsuarios.add(ParseObjectToTimeObject(results.get(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return timeUsuarios;
        }

        @Override
        protected void onPostExecute(ArrayList<TimeUsuario> lista) {
            progress.dismiss();
        }
    }
}
