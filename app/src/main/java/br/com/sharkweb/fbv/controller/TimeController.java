package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.model.Time;

public class TimeController {

    private TimeDAO timeDAO;

    public TimeController(Context context) {
        timeDAO = new TimeDAO(context);
    }

    public long inserir(Time time) {
        return timeDAO.inserir(time);
    }

    public long alterar(int id, Time time) {
        return timeDAO.alterar(id, time);
    }

    public long excluirTimePorId(int id) {
        return timeDAO.excluirTimePorId(id);
    }

    public ArrayList<Time> selectTimes() {
        return timeDAO.selectTime();
    }

    public ArrayList<Time> selectTimePorId(int id) {
        return timeDAO.selectTimePorId(id);
    }

    public void excluirTodosTimes() {
        timeDAO.excluirTodosTimes();
    }


}