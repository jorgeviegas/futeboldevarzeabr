package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.PosicaoDAO;
import br.com.sharkweb.fbv.model.Posicao;

/**
 * @author Tiago Klein
 *
 * Classe respons�vel somente pelas regras de neg�cio dos objetos do tipo Login.
 * Intera��es com o usu�rio ou com a tela do dispositivo devem ser implementadas na respectiva View.
 */
public class PosicaoController {

    private PosicaoDAO posicaoDAO;
    public PosicaoController(Context context) {
        posicaoDAO = new PosicaoDAO(context);
    }

    public long inserir(String nome) {
        return posicaoDAO.inserir(nome);
    }

    public long inserirComId(int id, String nome) {
        return posicaoDAO.inserirComId(id, nome);
    }

    public long alterar(int id, String tipo) {
        return posicaoDAO.alterar(id, tipo);
    }

    public ArrayList<Posicao> selectPosicoes() {
        return posicaoDAO.selectPosicoes();
    }

    public ArrayList<Posicao> selectPosicaoPorId(int id_posicao) {
        return posicaoDAO.selectPosicaoPorId(id_posicao);
    }

    public void excluirTodasPosicoes() {
        posicaoDAO.excluirTodasPosicoes();
    }

    public void IniciarPosicoes() {
        ArrayList<Posicao> posicao = this.selectPosicoes();
        if (posicao.isEmpty()) {
            this.inserir("Goleiro");
            this.inserir("Zagueiro");
            this.inserir("Lateral direito");
            this.inserir("Lateral esquerdo");
            this.inserir("Meio-campo");
            this.inserir("Atacante");
        }
    }
}