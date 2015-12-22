package br.com.sharkweb.fbv.controller;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.LocalDAO;
import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Local;
import br.com.sharkweb.fbv.model.Time;

public class LocalController {

    private LocalDAO localDAO;
    private Funcoes funcoes;

    public LocalController(Context context) {
        localDAO = new LocalDAO(context);
        funcoes = new Funcoes(context);
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

    public String getEnderecoCompleto(ParseObject local) {
        String endereco = "";
        if (!local.getString("endereco").isEmpty()) {
            endereco = endereco + funcoes.PrimeiraLetraMaiuscula(local.getString("endereco").trim()) + ", ";
        }
        if (local.getInt("numero") > 0) {
            endereco = endereco + String.valueOf(local.getInt("numero")).trim() + ", ";
        }
        if (!local.getString("municipio").isEmpty()) {
            endereco = endereco + local.getString("municipio").trim().toUpperCase();
        }

        return endereco;
    }
}