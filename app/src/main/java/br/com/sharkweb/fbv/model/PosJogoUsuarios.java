package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 08/09/2015.
 */
public class PosJogoUsuarios {

    int id;
    int id_pos_jogo;
    int id_usuario;
    int qtd_gol;
    int qtd_cartao_amarelo;
    int qtd_cartao_vermelho;
    int nota;


    public PosJogoUsuarios(int id, int id_pos_jogo, int id_usuario, int qtd_gol, int qtd_cartao_amarelo, int qtd_cartao_vermelho, int nota) {
        this.id = id;
        this.id_pos_jogo = id_pos_jogo;
        this.id_usuario = id_usuario;
        this.qtd_gol = qtd_gol;
        this.qtd_cartao_amarelo = qtd_cartao_amarelo;
        this.qtd_cartao_vermelho = qtd_cartao_vermelho;
        this.nota = nota;
    }

    public PosJogoUsuarios(int id_pos_jogo, int id_usuario, int qtd_gol, int qtd_cartao_amarelo, int qtd_cartao_vermelho, int nota) {
        this.id_pos_jogo = id_pos_jogo;
        this.id_usuario = id_usuario;
        this.qtd_gol = qtd_gol;
        this.qtd_cartao_amarelo = qtd_cartao_amarelo;
        this.qtd_cartao_vermelho = qtd_cartao_vermelho;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_pos_jogo() {
        return id_pos_jogo;
    }

    public void setId_pos_jogo(int id_pos_jogo) {
        this.id_pos_jogo = id_pos_jogo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getQtd_gol() {
        return qtd_gol;
    }

    public void setQtd_gol(int qtd_gol) {
        this.qtd_gol = qtd_gol;
    }

    public int getQtd_cartao_amarelo() {
        return qtd_cartao_amarelo;
    }

    public void setQtd_cartao_amarelo(int qtd_cartao_amarelo) {
        this.qtd_cartao_amarelo = qtd_cartao_amarelo;
    }

    public int getQtd_cartao_vermelho() {
        return qtd_cartao_vermelho;
    }

    public void setQtd_cartao_vermelho(int qtd_cartao_vermelho) {
        this.qtd_cartao_vermelho = qtd_cartao_vermelho;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
