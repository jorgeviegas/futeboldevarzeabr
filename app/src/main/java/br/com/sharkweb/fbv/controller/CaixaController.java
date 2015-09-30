package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.CaixaDAO;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Caixa;

public class CaixaController {

    private CaixaDAO caixaDAO;
    private Funcoes funcoes;

    public CaixaController(Context context) {
        caixaDAO = new CaixaDAO(context);
        funcoes = new Funcoes(context);
    }

    public long inserir(Caixa caixa) {
        return caixaDAO.inserir(caixa);
    }

    public long alterar(Caixa caixa) {
        return caixaDAO.alterar(caixa);
    }

    public ArrayList<Caixa> selectJogos() {
        return caixaDAO.selectCaixas();
    }

    public ArrayList<Caixa> selectCaixaPorId(int id) {
        return caixaDAO.selectCaixaPorId(id);
    }

    public ArrayList<Caixa> selectJogosPorIdTime(int id_time) {
        return caixaDAO.selectCaixasPorIdTime(id_time);
    }


    public void excluirTodosJogos() {
        caixaDAO.excluirTodosCaixas();
    }

    public long excluirCaixaPorId(int id) {
        return caixaDAO.excluirCaixaPorId(id);
    }

}