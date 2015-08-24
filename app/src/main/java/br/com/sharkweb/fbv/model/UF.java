package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class UF {

    private int id;
    private String nome;

    public UF() {
    }

    public UF(int id,String nome) {
        this.id = id;
        this.nome = nome;
    }

    public UF(String nome) {
        this.nome = nome;
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
}
