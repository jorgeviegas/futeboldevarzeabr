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
import br.com.sharkweb.fbv.model.Time;

public class TimeDAOParse {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "time";
    private static final String NOME_TABELA_VINCULO = "time_usuario";

    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String CIDADE = "cidade";
    private static final String ID_UF = "id_uf";
    private static final String ID_PARSE = "objectId";
    ParseObject timeParse;
    ParseQuery timeParsePesquisa;
    private Context context;

    private FBVDAO fbvdao;

    public TimeDAOParse(Context context) {
        fbvdao = FBVDAO.getInstance(context);
        this.timeParse = new ParseObject("time");
        this.timeParsePesquisa = new ParseQuery("time");
        this.context = context;
    }

    public ParseObject salvar(Time time) {
        if (!time.getId_parse().trim().isEmpty()) {
            timeParse.setObjectId(time.getId_parse().trim());
        }
        timeParse.put(NOME, time.getNome().trim());
        timeParse.put(CIDADE, time.getCidade().trim());
        timeParse.put(ID_UF, time.getId_uf());

        try {
            timeParse.save();
        } catch (ParseException e) {
            e.printStackTrace();
            timeParse.revert();
        }
        return timeParse;
    }

    public ArrayList<Time> buscarTimes(String coluna, String valor, int limite) {
        ArrayList<Time> retorno = new ArrayList<>();
        BuscarUsuarios busca = new BuscarUsuarios(context);
        AsyncTask<String, Void, ArrayList<Time>> ret = busca.execute(coluna, valor, String.valueOf(limite));
        try {
            retorno = ret.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public Time selectUsuarioPorId(String id) {
        Time time = null;
        try {
            ParseObject obj = timeParsePesquisa.get(id.trim());
            time = ParseObjectToTimeObject(obj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    private ArrayList<Time> listToArray(List listTimes) {
        ArrayList<Time> times = new ArrayList<Time>();
        for (int i = 0; i < listTimes.size(); i++) {
            ParseObject p = (ParseObject) listTimes.get(i);
            times.add(ParseObjectToTimeObject(p));
        }
        return times;
    }

    private Time ParseObjectToTimeObject(ParseObject p) {
        String objectId = p.getObjectId();
        String nome = p.getString("nome").trim();
        String cidade = p.getString("cidade").trim();
        int id_uf = p.getInt("id_uf");

        Time time = new Time(nome, cidade, id_uf, objectId);
        return time;
    }

    /**
     * @author Tiago Klein
     *         Classe privada de AsyncTask expecífica para busca de Usuarios.
     */
    private class BuscarUsuarios extends AsyncTask<String, Void, ArrayList<Time>> {

        private ProgressDialog progress;
        private Context context;

        public BuscarUsuarios(Context context) {
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
        protected ArrayList<Time> doInBackground(String... params) {
            final ArrayList<Time> usuario = new ArrayList<Time>();
            if (params[2] != null && Integer.valueOf(params[2]) > 0) {
                timeParsePesquisa.setLimit(Integer.valueOf(params[2]));
            }
            if (!params[0].isEmpty() || !params[1].isEmpty()) {
                timeParsePesquisa.whereEqualTo(params[0].trim(), params[1].trim());
            }
            List<ParseObject> results = null;
            try {
                results = timeParsePesquisa.find();
                if (results.size() > 0)
                    usuario.add(ParseObjectToTimeObject(results.get(0)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return usuario;
        }

        @Override
        protected void onPostExecute(ArrayList<Time> lista) {
            progress.dismiss();
        }
    }
}
