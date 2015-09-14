package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import br.com.sharkweb.fbv.DAO.JogoDAO;
import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.Time;

public class JogoController {

    private JogoDAO jogoDAO;
    private Funcoes funcoes;
    //private PosJogoController posJogoControl;

    public JogoController(Context context) {
        jogoDAO = new JogoDAO(context);
        funcoes = new Funcoes(context);
        //posJogoControl = new PosJogoController(context);
    }

    public long inserir(Jogo jogo) {
        return jogoDAO.inserir(jogo);
    }

    public long alterar(Jogo jogo) {
        return jogoDAO.alterar(jogo);
    }

    public long inativarJogo(Jogo jogo) {
        return jogoDAO.inativarJogo(jogo);
    }

    public ArrayList<Jogo> selectJogos() {
        return jogoDAO.selectJogos();
    }

    public ArrayList<Jogo> selectJogoPorId(int id) {
        return jogoDAO.selectJogoPorId(id);
    }

    public ArrayList<Jogo> selectJogosPorIdTime(int id_time) {
        return jogoDAO.selectJogosPorIdTime(id_time);
    }

    public ArrayList<Jogo> selectJogosPorIdTimeEData(int id_time, String data) {
        return jogoDAO.selectJogosPorIdTimeEData(id_time, data);
    }

    public ArrayList<Jogo> selectJogosPorIdTime2(int id_time2) {
        return jogoDAO.selectJogosPorIdTime2(id_time2);
    }

    public boolean isOpen(Jogo jogo) {
        Date datajogo = null;
        try {
            datajogo = funcoes.setDateTime(jogo.getData().trim()+" "+jogo.getHoraFinal().trim()+":00");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        Date dataSistema = funcoes.getDateTime();
        if (datajogo.after(dataSistema)){
            return true;
        }else {
            return false;
        }
    }

    public void excluirTodosJogos() {
        jogoDAO.excluirTodosJogos();
    }
    public long excluirJogoPorId(int id) {
        //posJogoControl.excluirPosJogoPorIdJogo(id);
        return jogoDAO.excluirJogoPorId(id);
    }

}