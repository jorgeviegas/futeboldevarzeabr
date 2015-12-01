package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.DAOParse.TimeDAOParse;
import br.com.sharkweb.fbv.model.Time;

public class TimeController {

    private TimeDAO timeDAO;
    private TimeDAOParse timeDAOParse;

    public TimeController(Context context) {
        timeDAO = new TimeDAO(context);
        timeDAOParse = new TimeDAOParse(context);
    }

    public long inserir(Time time, boolean parse) {
        return timeDAO.inserir(time);
    }

    public long alterar(Time time, boolean parse) {
        return timeDAO.alterar(time);
    }

    public long excluirTimePorId(int id) {
        return timeDAO.excluirTimePorId(id);
    }

    public ArrayList<Time> selectTimes(boolean parse) {
        if (parse) {
            return timeDAOParse.buscarTimes("", "", 0);
        } else {
            return timeDAO.selectTime();
        }
    }

    public ArrayList<Time> selectTimePorId(int id, String id_parse) {
        return timeDAO.selectTimePorId(id);
    }

    public ArrayList<Time> selectTimePorIdUsuario(int id_usuario, boolean parse) {
        if (parse) {
            return timeDAOParse.buscarTimes("", "", 0);
        } else {
            return timeDAO.selectTimePorIdUsuario(id_usuario);
        }
    }

    public void excluirTodosTimes() {
        timeDAO.excluirTodosTimes();
    }


}