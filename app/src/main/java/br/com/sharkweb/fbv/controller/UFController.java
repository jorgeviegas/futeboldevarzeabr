package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.UFDAO;
import br.com.sharkweb.fbv.model.UF;

public class UFController {

    private UFDAO ufDAO;

    public UFController(Context context) {
        ufDAO = new UFDAO(context);
    }

    public long inserir(UF uf) {
        return ufDAO.inserir(uf);
    }

    public long alterar(UF uf) {
        return ufDAO.alterar(uf);
    }

    public long excluirUFPorId(int id) {
        return ufDAO.excluirUFPorId(id);
    }

    public ArrayList<UF> selectUF() {
        return ufDAO.selectUF();
    }

    public ArrayList<UF> selectUFPorId(int id) {
        return ufDAO.selectUFPorId(id);
    }

    public ArrayList<UF> selectUFPorDescricao(String uf) {
        return ufDAO.selectUFPorDescricao(uf);
    }

    public void excluirTodosUF() {
        ufDAO.excluirTodosUF();
    }

    public void inicializarUF() {
        if (this.selectUF().isEmpty()) {
            String[] estados = new String[]{"AC", "AL", "AP", "AM", "BA", "CE",
                    "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PR", "PB", "PA", "PE",
                    "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SE", "SP", "TO"};

            for (int i = 0; i < estados.length; i++) {
                UF uf = new UF(estados[i].trim());
                this.inserir(uf);
            }
        }
    }
}