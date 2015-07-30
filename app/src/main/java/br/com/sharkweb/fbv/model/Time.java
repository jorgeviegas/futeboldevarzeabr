package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Time {

    private int id;
    private String nome;
    private String cidade;
    private String uf;

    public Time(int id, String nome, String cidade, String uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.uf = uf;
        this.id = id;
    }

    public Time(String nome, String cidade, String uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.uf = uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
