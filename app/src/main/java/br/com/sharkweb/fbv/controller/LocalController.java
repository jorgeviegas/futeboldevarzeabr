package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.LocalDAO;
import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;

public class LocalController {

    private LocalDAO localDAO;

    public LocalController(Context context) {
        localDAO = new LocalDAO(context);
    }

    public long inserir(Local local) {
        return localDAO.inserir(local);
    }

    public long alterar(Local local) {
        return localDAO.alterar(local);
    }

    public long excluirLocalPorId(int id) {
        return localDAO.excluirLocalPorId(id);
    }

    public ArrayList<Local> selectLocais() {
        return localDAO.selectLocais();
    }

    public ArrayList<Local> selectLocalPorId(int id) {
        return localDAO.selectLocalPorId(id);
    }

    public void excluirTodosLocais() {
        localDAO.excluirTodosLocais();
    }
}