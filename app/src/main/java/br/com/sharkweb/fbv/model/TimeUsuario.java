package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class TimeUsuario {

    private int id;
    private String id_time;
    private String id_usuario;
    private int inativo;
    private String posicao;
    private int id_tipo_usuario;
    private String id_parse;

    public TimeUsuario(int id, String id_time, String id_usuario, int inativo, String posicao, int id_tipo_usuario, String id_parse) {
        this.id = id;
        this.id_time = id_time;
        this.id_usuario = id_usuario;
        this.inativo = inativo;
        this.posicao = posicao;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_parse = id_parse;
    }

    public TimeUsuario(String id_time, String id_usuario, int inativo, String posicao, int id_tipo_usuario, String id_parse) {
        this.id_time = id_time;
        this.id_usuario = id_usuario;
        this.inativo = inativo;
        this.posicao = posicao;
        this.id_tipo_usuario = id_tipo_usuario;
        this.id_parse = id_parse;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_time() {
        return id_time;
    }

    public void setId_time(String id_time) {
        this.id_time = id_time;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
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

    public int getId_tipo_usuario() {
        return id_tipo_usuario;
    }

    public void setId_tipo_usuario(int id_tipo_usuario) {
        this.id_tipo_usuario = id_tipo_usuario;
    }

    public String getId_parse() {
        return id_parse;
    }

    public void setId_parse(String id_parse) {
        this.id_parse = id_parse;
    }
}
