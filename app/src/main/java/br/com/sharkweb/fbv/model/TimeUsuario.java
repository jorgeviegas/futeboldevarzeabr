package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class TimeUsuario {

    private int id;
    private int id_time;
    private int id_usuario;
    private int inativo;
    private String posicao;

    public TimeUsuario(int id, int id_time, int id_usuario, int inativo, String posicao) {
        this.id = id;
        this.id_time = id_time;
        this.id_usuario = id_usuario;
        this.inativo = inativo;
        this.posicao = posicao;
    }

    public TimeUsuario(int id_time, int id_usuario, int inativo, String posicao) {
        this.id_time = id_time;
        this.id_usuario = id_usuario;
        this.inativo = inativo;
        this.posicao = posicao;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_time() {
        return id_time;
    }

    public void setId_time(int id_time) {
        this.id_time = id_time;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getInativo() {
        return inativo;
    }

    public void setInativo(int inativo) {
        this.inativo = inativo;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }
}
