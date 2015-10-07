package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import br.com.sharkweb.fbv.DAO.JogoDAO;
import br.com.sharkweb.fbv.DAO.MovimentoDAO;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Movimento;

public class MovimentoController {

    private MovimentoDAO movimentoDAO;
    private CaixaController caixaControl;
    private Funcoes funcoes;

    public MovimentoController(Context context) {
        movimentoDAO = new MovimentoDAO(context);
        caixaControl = new CaixaController(context);
        funcoes = new Funcoes(context);
    }

    public long inserir(Movimento movimento) {
        return movimentoDAO.inserir(movimento);
    }

    public long alterar(Movimento movimento) {
        return movimentoDAO.alterar(movimento);
    }

    public ArrayList<Movimento> selectMovimentos() {
        return movimentoDAO.selectMovimentos();
    }

    public ArrayList<Movimento> selectMovimentosPorId(int id) {
        return movimentoDAO.selectMovimentosPorId(id);
    }

    public ArrayList<Movimento> selectMovimentosPorIdCaixa(int id_caixa) {
        return movimentoDAO.selectMovimentosPorIdCaixa(id_caixa);
    }

    public void criarMovimento(String tipo, Caixa caixa, double valor) {
        String historico = "";
        if (tipo.equals("R")) {
            historico = "Retirada manual";
            caixa.setSaldo(caixa.getSaldo() - valor);
        } else if (tipo.equals("E")) {
            historico = "Entrada manual";
            caixa.setSaldo(caixa.getSaldo() + valor);
        } else if (tipo.equals("M")) {
            caixa.setSaldo(caixa.getSaldo() + valor);
            historico = "Pgto. Mensalidade";
        }
        Movimento mov = new Movimento(caixa.getId(), historico, funcoes.getDataDia(), valor,
                tipo, Constantes.getUsuarioLogado().getId());
        Long ret = this.inserir(mov);
        if (ret > 0) {
            Long ret2 = caixaControl.alterar(caixa);
        }
    }

    public void excluirTodosMovimentos() {
        movimentoDAO.excluirTodosMovimentos();
    }

    public long excluirMovimentoPorId(int id) {
        return movimentoDAO.excluirMovimentoPorId(id);
    }

}