package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.MensalidadeDAO;
import br.com.sharkweb.fbv.DAO.MovimentoDAO;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Mensalidade;
import br.com.sharkweb.fbv.model.Movimento;

public class MensalidadeController {

    private MensalidadeDAO mensalidadeDAO;
    private Funcoes funcoes;

    public MensalidadeController(Context context) {
        mensalidadeDAO = new MensalidadeDAO(context);
        funcoes = new Funcoes(context);
    }

    public long inserir(Mensalidade mensalidade) {
        return mensalidadeDAO.inserir(mensalidade);
    }

    public long alterar(Mensalidade mensalidade) {
        return mensalidadeDAO.alterar(mensalidade);
    }

    public ArrayList<Mensalidade> selectMensalidades() {
        return mensalidadeDAO.selectMensalidades();
    }

    public ArrayList<Mensalidade> selectMensalidadePorId(int id) {
        return mensalidadeDAO.selectMensalidadePorId(id);
    }

    public ArrayList<Mensalidade> selectMensalidadesPorIdTime(int id_time) {
        return mensalidadeDAO.selectMensalidadesPorIdTime(id_time);
    }

    public ArrayList<Mensalidade> selectMensalidadesPorIdTimeUsuario(int id_time,int id_usuario) {
        return mensalidadeDAO.selectMensalidadesPorIdTimeUsuario(id_time, id_usuario);
    }

    public void excluirTodosMovimentos() {
        mensalidadeDAO.excluirTodasMensalidades();
    }
    public long excluirMensalidadePorId(int id) {
        return mensalidadeDAO.excluirMensalidadePorId(id);
    }
}