package br.com.sharkweb.fbv.controllerParse;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.DAOParse.TimeDAOParse;
import br.com.sharkweb.fbv.model.Time;

public class TimeControllerParse {

    private TimeDAOParse timeDAOParse;

    public TimeControllerParse(Context context) {
        timeDAOParse = new TimeDAOParse(context);
    }

    public ParseObject salvar(Time time) {
        return timeDAOParse.salvar(time);
    }

    public ArrayList<Time> selectTimes() {
        return timeDAOParse.buscarTimes("", "", 0);
    }

    public Time selectTimePorId(String id_parse) {
        return timeDAOParse.selectUsuarioPorId(id_parse);
    }

}