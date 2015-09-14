package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 08/09/2015.
 */
public class PosJogo {

    int id;
    int id_jogo;
    int qtd_gol_time1;
    int qtd_gol_time2;


    public PosJogo(int id_jogo, int qtd_gol_time1, int qtd_gol_time2) {
        this.id_jogo = id_jogo;
        this.qtd_gol_time1 = qtd_gol_time1;
        this.qtd_gol_time2 = qtd_gol_time2;
    }
    public PosJogo(int id,int id_jogo, int qtd_gol_time1, int qtd_gol_time2) {
        this.id = id;
        this.id_jogo = id_jogo;
        this.qtd_gol_time1 = qtd_gol_time1;
        this.qtd_gol_time2 = qtd_gol_time2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(int id_jogo) {
        this.id_jogo = id_jogo;
    }

    public int getQtd_gol_time1() {
        return qtd_gol_time1;
    }

    public void setQtd_gol_time1(int qtd_gol_time1) {
        this.qtd_gol_time1 = qtd_gol_time1;
    }

    public int getQtd_gol_time2() {
        return qtd_gol_time2;
    }

    public void setQtd_gol_time2(int qtd_gol_time2) {
        this.qtd_gol_time2 = qtd_gol_time2;
    }
}
