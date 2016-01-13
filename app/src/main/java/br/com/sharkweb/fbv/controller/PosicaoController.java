package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.PosicaoDAO;
import br.com.sharkweb.fbv.model.Posicao;

/**
 * @author Tiago Klein
 *         <p/>
 *         Classe respons�vel somente pelas regras de neg�cio dos objetos do tipo Login.
 *         Intera��es com o usu�rio ou com a tela do dispositivo devem ser implementadas na respectiva View.
 */
public class PosicaoController {

    private PosicaoDAO posicaoDAO;

    public PosicaoController(Context context) {
        posicaoDAO = new PosicaoDAO(context);
    }

    public long inserir(String nome, String abreviatura) {
        return posicaoDAO.inserir(nome, abreviatura);
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
    public ArrayList<Posicao> selectPosicaoPorCodigo(String abreviatura) {
        return posicaoDAO.selectPosicaoPorCodigo(abreviatura);
    }

    public void excluirTodasPosicoes() {
        posicaoDAO.excluirTodasPosicoes();
    }

    public void IniciarPosicoes() {
        ArrayList<Posicao> posicao = this.selectPosicoes();
        if (posicao.isEmpty()) {
            this.inserir("Goleiro","GK");
            this.inserir("Zagueiro Direito","RB");
            this.inserir("Zagueiro Central","CB");
            this.inserir("Zagueiro Esquerdo","LB");
            this.inserir("Líbero","SW");
            this.inserir("Lateral Direito","RWB");
            this.inserir("Lateral Esquerdo","LWB");
            this.inserir("Meio Campo Defensivo / Volante","CDM");
            this.inserir("Meia Central","CM");
            this.inserir("Meio Campo Ofensivo / Armador","CAM");
            this.inserir("Meia Ofensivo","OM");
            this.inserir("Meia Esquerda Ofensivo","LOM");
            this.inserir("Meia Direita Ofensivo","ROM");
            this.inserir("Meia Esquerda","LM");
            this.inserir("Meia Direita","RM");
            this.inserir("Meio Ala Esquerdo","LWM");
            this.inserir("Meio Ala Direito","RWM");
            this.inserir("Ala Direito","RW");
            this.inserir("Ala Esquerto","LW");
            this.inserir("Atacante Esquerdo","LF");
            this.inserir("Atacante Direito","RF");
            this.inserir("Atacante","ST");
            this.inserir("Centro Avante","CF");
            this.inserir("Atacante Direito","RS");
            this.inserir("Atacante Esquerdo","LS");
        }
    }
}