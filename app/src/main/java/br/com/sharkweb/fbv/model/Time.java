package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Time {

    private int id;
    private String nome;
    private String cidade;
    private int id_uf;
    private String id_parse;

    public Time(int id, String nome, String cidade, int id_uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.id_uf = id_uf;
        this.id = id;
    }

    public Time(String nome, String cidade, int id_uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.id_uf = id_uf;
    }

    public Time(String nome, String cidade, int id_uf, String id_parse) {
        this.nome = nome;
        this.cidade = cidade;
        this.id_uf = id_uf;
        this.id_parse = id_parse;
    }

    public Time() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getId_uf() {
        return id_uf;
    }

    public void setId_uf(int id_uf) {
        this.id_uf = id_uf;
    }

    public String getId_parse() {
        return id_parse;
    }

    public void setId_parse(String id_parse) {
        this.id_parse = id_parse;
    }
}
