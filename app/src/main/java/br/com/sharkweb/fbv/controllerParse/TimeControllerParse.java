package br.com.sharkweb.fbv.controllerParse;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.sharkweb.fbv.DAOParse.BuscarArrayObjetosParse;
import br.com.sharkweb.fbv.DAOParse.BuscarObjetoParse;
import br.com.sharkweb.fbv.DAOParse.TimeDAOParse;
import br.com.sharkweb.fbv.model.Time;

public class TimeControllerParse {

    private TimeDAOParse timeDAOParse;
    private BuscarObjetoParse buscarDados;
    private BuscarArrayObjetosParse buscarArrayDados;
    private Context context;

    public TimeControllerParse(Context context) {
        timeDAOParse = new TimeDAOParse(context);
        this.context = context;
    }

    public ParseObject salvar(Time time) {
        ParseObject timeParse = new ParseObject("time");
        if (time.getId_parse() != null && !time.getId_parse().isEmpty()) {
            timeParse.setObjectId(time.getId_parse().trim());
        }
        timeParse.put("nome", time.getNome().trim());
        timeParse.put("cidade", time.getCidade().trim());
        timeParse.put("id_uf", time.getId_uf());
        buscarDados = new BuscarObjetoParse(context, timeParse, null);
        ParseObject retorno = buscarDados.salvar();
        try {
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ParseObject> buscarTimes(String... params) {
        buscarArrayDados = new BuscarArrayObjetosParse(context, "time");
        try {
            return buscarArrayDados.buscarTimes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ParseObject buscarTimePorId(String id_parse) {
        buscarDados = new BuscarObjetoParse(context, null, "time");
        try {
            return buscarDados.buscar(id_parse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Time ParseObjectToTimeObject(ParseObject p) {
        String objectId = p.getObjectId();
        String nome = p.getString("nome").trim();
        String cidade = p.getString("cidade").trim();
        int id_uf = p.getInt("id_uf");

        Time time = new Time(nome, cidade, id_uf, objectId);
        return time;
    }
}